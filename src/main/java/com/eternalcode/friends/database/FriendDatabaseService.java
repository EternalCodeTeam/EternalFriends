package com.eternalcode.friends.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FriendDatabaseService{

    private final Plugin plugin;
    private final DataSource dataSource;

    public FriendDatabaseService(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;

        initTable();
    }

    private void initTable() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS eternalfriends_friends(" +
                                    "id INT NOT NULL AUTO_INCREMENT, " +
                                    "uuid_one VARCHAR(36) NOT NULL, " +
                                    "uuid_two VARCHAR(36) NOT NULL, " +
                                    "PRIMARY KEY (id)" +
                                    ");"
                    )
            ) {

                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void add(UUID uuidOne, UUID uuidTwo) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO eternalfriends_friends(uuid_one, uuid_two) VALUES (?, ?);"
                    )
            ) {

                stmt.setString(1, uuidOne.toString());
                stmt.setString(2, uuidTwo.toString());

                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(UUID uuidOne, UUID uuidTwo) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM eternalfriends_friends WHERE uuid_one = '" + uuidOne + "' AND uuid_two = '" + uuidTwo + "';"
                    )
            ) {

                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void load(Map<UUID, List<UUID>> friends) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "SELECT uuid_one, uuid_two FROM eternalfriends_friends;"
                    )
            ) {

                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    UUID uuidOne = UUID.fromString(resultSet.getString(1));
                    UUID uuidTwo = UUID.fromString(resultSet.getString(2));

                    if (!friends.containsKey(uuidOne)) {
                        List<UUID> newFriends = new ArrayList<>();
                        newFriends.add(uuidTwo);
                        friends.put(uuidOne, newFriends);
                    }
                    else {
                        friends.get(uuidOne).add(uuidTwo);
                    }

                    if (!friends.containsKey(uuidTwo)) {
                        List<UUID> newFriends = new ArrayList<>();
                        newFriends.add(uuidOne);
                        friends.put(uuidTwo, newFriends);
                        continue;
                    }
                    friends.get(uuidTwo).add(uuidOne);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
