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

public class IgnoredPlayerDatabaseService {

    private final Plugin plugin;
    private final DataSource dataSource;

    public IgnoredPlayerDatabaseService(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;

        initTable();
    }

    public void initTable() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS eternalfriends_ignored_players(" +
                                    "id INT NOT NULL AUTO_INCREMENT, " +
                                    "who_ignore CHAR(36) NOT NULL, " +
                                    "is_ignored CHAR(36) NOT NULL, " +
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

    public void addIgnoredPlayer(UUID ignore, UUID ignored) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO eternalfriends_ignored_players(who_ignore, is_ignored) VALUES (?, ?);"
                    )
            ) {

                stmt.setString(1, ignore.toString());
                stmt.setString(2, ignored.toString());

                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeIgnoredPlayer(UUID ignore, UUID ignored) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM eternalfriends_ignored_players WHERE who_ignore = '" + ignore + "' AND is_ignored = '" + ignored + "';"
                    )
            ) {

                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void load(Map<UUID, List<UUID>> ignoredPlayers) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "SELECT who_ignore, is_ignored FROM eternalfriends_ignored_players;"
                    )
            ) {

                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    UUID ignore = UUID.fromString(resultSet.getString(1));
                    UUID ignored = UUID.fromString(resultSet.getString(2));

                    if (!ignoredPlayers.containsKey(ignore)) {
                        List<UUID> newIgnoredPlayers = new ArrayList<>();
                        newIgnoredPlayers.add(ignored);
                        ignoredPlayers.put(ignore, newIgnoredPlayers);
                    }
                    else {
                        ignoredPlayers.get(ignore).add(ignored);
                    }

                    if (!ignoredPlayers.containsKey(ignored)) {
                        List<UUID> newIgnoredPlayers = new ArrayList<>();
                        newIgnoredPlayers.add(ignore);
                        ignoredPlayers.put(ignored, newIgnoredPlayers);
                        continue;
                    }
                    ignoredPlayers.get(ignored).add(ignore);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
