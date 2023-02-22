package com.eternalcode.friends.database;

import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.invite.Invite;
import com.eternalcode.friends.util.StatementBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DatabaseService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT).withZone(ZoneOffset.systemDefault());

    private final static String TABLE_INVITES_NAME = "invites";

    private final static String TABLE_INVITES = TABLE_INVITES_NAME +
            "(id INT NOT NULL AUTO_INCREMENT, " +
            "uuid_from VARCHAR(36) NOT NULL, " +
            "uuid_to VARCHAR(36) NOT NULL, " +
            "expiration_date DATETIME NOT NULL, " +
            "PRIMARY KEY (id)" +
            ");";

    private final static String TABLE_FRIENDS_NAME = "friends";

    private final static String TABLE_FRIENDS = TABLE_FRIENDS_NAME +
            "(id INT NOT NULL AUTO_INCREMENT, " +
            "uuid_one VARCHAR(36) NOT NULL, " +
            "uuid_two VARCHAR(36) NOT NULL, " +
            "PRIMARY KEY (id)" +
            ");";

    private final static String TABLE_IGNORED_NAME = "ignored_players";

    private final static String TABLE_INGNORED = TABLE_IGNORED_NAME +
            "(id INT NOT NULL AUTO_INCREMENT, " +
            "who_ignore VARCHAR(36) NOT NULL, " +
            "is_ignored VARCHAR(36) NOT NULL, " +
            "PRIMARY KEY (id)" +
            ");";

    private final PluginConfig pluginConfig;

    private DataSource dataSource;
    private final PluginConfig.Database databaseConfig;

    public DatabaseService(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
        this.databaseConfig = this.pluginConfig.database;

        this.databaseConfiguration();

        // tables creation
        this.executeStatements(new ArrayList<>(Arrays.asList(
                "CREATE TABLE IF NOT EXISTS " + this.databaseConfig.prefix + TABLE_INVITES,
                "CREATE TABLE IF NOT EXISTS " + this.databaseConfig.prefix + TABLE_FRIENDS,
                "CREATE TABLE IF NOT EXISTS " + this.databaseConfig.prefix + TABLE_INGNORED
        )));
    }

    public void saveNewFriends(UUID first, UUID second) {
        this.executeStatement(
                new StatementBuilder()
                        .insert(this.databaseConfig.prefix + TABLE_FRIENDS_NAME)
                        .values(null, first, second)
                        .asString()
        );
    }

    public void saveIgnoredPlayer(UUID ignore, UUID isIgnored) {
        this.executeStatement(
                new StatementBuilder()
                        .insert(this.databaseConfig.prefix + TABLE_IGNORED_NAME)
                        .values(null, ignore, isIgnored)
                        .asString()
        );
    }

    public void saveInvite(Invite invite) {
        this.executeStatement(
                new StatementBuilder()
                        .insert(this.databaseConfig.prefix + TABLE_INVITES_NAME)
                        .values(null, invite.getFrom(), invite.getTo(), dateTimeFormatter.format(invite.getExpirationDate()))
                        .asString()
        );
    }

    public void removeFriends(UUID first, UUID second) {
        this.executeStatement(
                new StatementBuilder()
                        .delete(this.databaseConfig.prefix + TABLE_FRIENDS_NAME)
                        .where()
                        .equal("uuid_one", first)
                        .and()
                        .equal("uuid_two", second)
                        .or()
                        .equal("uuid_one", second)
                        .and()
                        .equal("uuid_two", first)
                        .asString()
        );
    }

    public void removeIgnored(UUID ignore, UUID isIgnored) {
        this.executeStatement(
                new StatementBuilder()
                        .delete(this.databaseConfig.prefix + TABLE_IGNORED_NAME)
                        .where()
                        .equal("who_ignore", ignore)
                        .and()
                        .equal("is_ignored", isIgnored)
                        .asString()
        );
    }

    public void removeInvite(UUID from, UUID to) {
        this.executeStatement(
                new StatementBuilder()
                        .delete(this.databaseConfig.prefix + TABLE_INVITES_NAME)
                        .where()
                        .equal("uuid_from", from)
                        .and()
                        .equal("uuid_to", to)
                        .asString()
        );
    }

    public void databaseConfiguration() {
        HikariConfig hikariConfig = new HikariConfig();

        switch (databaseConfig.databaseType) {
            case MYSQL:
                hikariConfig.setJdbcUrl("jdbc:mysql://" + databaseConfig.host + ":" + databaseConfig.port + "/" + databaseConfig.database);
                hikariConfig.setUsername(databaseConfig.username);
                hikariConfig.setPassword(databaseConfig.password);

                break;

            case SQLITE:
                break;
        }

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public void executeStatement(String statement) {
        try {
            Connection connection = this.dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.execute();
            preparedStatement.close();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeStatements(List<String> statements) {
        try {
            Connection connection = this.dataSource.getConnection();

            // prepareStatement() takes sql statement so I gave first statement from list and remove him
            PreparedStatement preparedStatement = connection.prepareStatement(statements.get(0));
            statements.remove(0);

            for (String statement : statements) {
                preparedStatement.addBatch(statement);
            }

            preparedStatement.execute();
            preparedStatement.executeBatch();
            preparedStatement.close();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
