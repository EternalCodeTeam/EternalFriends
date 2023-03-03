package com.eternalcode.friends.database;

import com.eternalcode.friends.config.implementation.PluginConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceBuilder {

    public HikariDataSource buildHikariDataSource(PluginConfig.Database databaseConfig) {
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

        return new HikariDataSource(hikariConfig);
    }

}
