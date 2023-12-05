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

public class IgnoredPlayerDatabaseService {

    private final DataSource dataSource;

    public IgnoredPlayerDatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;

        initTable();
    }

    public void initTable() {
        try (
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS eternalfriends_ignored_players("
                    + "who_ignore CHAR(36) NOT NULL, "
                    + "is_ignored CHAR(36) NOT NULL "
                    + ");"
            )
        ) {

            statement.execute();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addIgnoredPlayer(UUID ignore, UUID ignored) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO eternalfriends_ignored_players VALUES (?, ?);"
                )
            ) {

                statement.setString(1, ignore.toString());
                statement.setString(2, ignored.toString());

                statement.execute();

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeIgnoredPlayer(UUID ignore, UUID ignored) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM eternalfriends_ignored_players WHERE who_ignore = '" + ignore + "' AND is_ignored = '" + ignored + "';"
                )
            ) {

                statement.execute();

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void load(Map<UUID, List<UUID>> ignoredPlayers) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "SELECT who_ignore, is_ignored FROM eternalfriends_ignored_players;"
                )
            ) {

                ResultSet resultSet = statement.executeQuery();

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
                }

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
