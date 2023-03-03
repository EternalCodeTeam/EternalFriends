package com.eternalcode.friends.database;

import com.eternalcode.friends.invite.Invite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

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

public class InviteDatabaseService {

    private final DataSource dataSource;
    private final Plugin plugin;

    public InviteDatabaseService(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;

        initTable();
    }

    public void initTable() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS eternalfriends_invites(" +
                                    "id INT NOT NULL AUTO_INCREMENT, " +
                                    "uuid_from CHAR(36) NOT NULL, " +
                                    "uuid_to CHAR(36) NOT NULL, " +
                                    "expiration_date DATETIME NOT NULL, " +
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

    public void addInvite(Invite invite) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO eternalfriends_invites(uuid_from, uuid_to, expiration_date) VALUES (?, ?, ?);"
                    )
            ) {

                stmt.setString(1, invite.getFrom().toString());
                stmt.setString(2, invite.getTo().toString());
                stmt.setTimestamp(3, Timestamp.from(invite.getExpirationDate()));

                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeInvite(Invite invite) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {});
        try (
                Connection conn = dataSource.getConnection();

                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM eternalfriends_invites WHERE uuid_from = '" + invite.getFrom() + "' AND uuid_to = '" + invite.getTo()+"';"
                )
        ) {

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeInvite(UUID from, UUID to) {
        removeInvite(new Invite(from, to, Duration.ZERO));
    }

    public void load(Map<UUID, List<Invite>> receivedInvites, Map<UUID, List<Invite>> sentInvites) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (
                    Connection conn = dataSource.getConnection();

                    PreparedStatement stmt = conn.prepareStatement(
                            "SELECT uuid_from, uuid_to, expiration_date FROM eternalfriends_invites;"
                    )
            ) {

                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    UUID from = UUID.fromString(resultSet.getString(1));
                    UUID to = UUID.fromString(resultSet.getString(2));
                    Instant expirationDate = resultSet.getTimestamp(3).toInstant();

                    Invite invite = new Invite(from, to, expirationDate);

                    if (sentInvites.containsKey(from)) {
                        sentInvites.get(from).add(invite);
                    }
                    else {
                        sentInvites.put(from, new ArrayList<>(List.of(invite)));
                    }

                    if (receivedInvites.containsKey(to)) {
                        receivedInvites.get(to).add(invite);
                        continue;
                    }
                    receivedInvites.put(to, new ArrayList<>(List.of(invite)));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
