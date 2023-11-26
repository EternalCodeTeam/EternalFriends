package com.eternalcode.friends.database;

import com.eternalcode.friends.invite.Invite;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class InviteDatabaseService {

    private final DataSource dataSource;

    public InviteDatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;

        initTable();
    }

    public void initTable() {
        try (
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS eternalfriends_invites("
                    + "uuid_from CHAR(36) NOT NULL, "
                    + "uuid_to CHAR(36) NOT NULL, "
                    + "expiration_date DATETIME NOT NULL "
                    + ");"
            )
        ) {

            statement.execute();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addInvite(Invite invite) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO eternalfriends_invites VALUES (?, ?, ?);"
                )
            ) {

                statement.setString(1, invite.getFrom().toString());
                statement.setString(2, invite.getTo().toString());
                statement.setTimestamp(3, Timestamp.from(invite.getExpirationDate()));

                statement.execute();

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeInvite(Invite invite) {
        CompletableFuture.runAsync(() -> {
        });
        try (
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM eternalfriends_invites WHERE uuid_from = '" + invite.getFrom() + "' AND uuid_to = '" + invite.getTo() + "';"
            )
        ) {

            statement.execute();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeInvite(UUID from, UUID to) {
        removeInvite(new Invite(from, to, Duration.ZERO));
    }

    public void load(Map<UUID, List<Invite>> receivedInvites, Map<UUID, List<Invite>> sentInvites) {
        CompletableFuture.runAsync(() -> {
            try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                    "SELECT uuid_from, uuid_to, expiration_date FROM eternalfriends_invites;"
                )
            ) {

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    UUID from = UUID.fromString(resultSet.getString(1));
                    UUID to = UUID.fromString(resultSet.getString(2));
                    Instant expirationDate = resultSet.getTimestamp(3).toInstant();

                    Invite invite = new Invite(from, to, expirationDate);

                    if (sentInvites.containsKey(from)) {
                        sentInvites.get(from).add(invite);
                    } else {
                        sentInvites.put(from, new ArrayList<>(List.of(invite)));
                    }

                    if (receivedInvites.containsKey(to)) {
                        receivedInvites.get(to).add(invite);
                        continue;
                    }
                    receivedInvites.put(to, new ArrayList<>(List.of(invite)));
                }

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
