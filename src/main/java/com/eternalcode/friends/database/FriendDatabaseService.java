package com.eternalcode.friends.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FriendDatabaseService {

    private final DataSource dataSource;

    public FriendDatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;

        initTable();
    }

    private void initTable() {
        try (
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS eternalfriends_friends("
                    + "uuid_one VARCHAR(36) NOT NULL, "
                    + "uuid_two VARCHAR(36) NOT NULL "
                    + ");"
            )
        ) {

            statement.execute();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(UUID uuidOne, UUID uuidTwo) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO eternalfriends_friends VALUES (?, ?);"
                )
            ) {

                statement.setString(1, uuidOne.toString());
                statement.setString(2, uuidTwo.toString());

                statement.execute();

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(UUID uuidOne, UUID uuidTwo) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM eternalfriends_friends "
                        + "WHERE uuid_one = '" + uuidOne + "' AND uuid_two = '" + uuidTwo + "' "
                        + "OR uuid_one = '" + uuidTwo + "' AND uuid_two = '" + uuidOne + "';"
                )
            ) {

                statement.execute();

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void load(Map<UUID, List<UUID>> friends) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "SELECT uuid_one, uuid_two FROM eternalfriends_friends;"
                )
            ) {

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    UUID uuidOne = UUID.fromString(resultSet.getString(1));
                    UUID uuidTwo = UUID.fromString(resultSet.getString(2));

                    if (!friends.containsKey(uuidOne)) {
                        List<UUID> newFriends = new ArrayList<>();
                        newFriends.add(uuidTwo);
                        friends.put(uuidOne, newFriends);
                    } else {
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

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
