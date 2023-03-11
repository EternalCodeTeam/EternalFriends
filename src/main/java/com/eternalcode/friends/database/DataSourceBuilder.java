package com.eternalcode.friends.database;

import com.eternalcode.friends.config.implementation.PluginConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;

public class DataSourceBuilder {

    public HikariDataSource buildHikariDataSource(PluginConfig.Database databaseConfig, File dataFolder) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);

        switch (databaseConfig.databaseType) {
            case MYSQL -> {
                hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
                hikariConfig.setJdbcUrl("jdbc:mysql://" + databaseConfig.host + ":" + databaseConfig.port + "/" + databaseConfig.database);
                hikariConfig.setUsername(databaseConfig.username);
                hikariConfig.setPassword(databaseConfig.password);
            }

            case SQLITE -> {
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl("jdbc:sqlite:" + dataFolder + "/database.db");
            }
        }

        return new HikariDataSource(hikariConfig);
    }

}
