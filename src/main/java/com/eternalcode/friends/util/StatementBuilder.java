package com.eternalcode.friends.util;

import org.bukkit.Bukkit;

public class StatementBuilder {

    private final StringBuilder builder;

    public StatementBuilder() {
        this.builder = new StringBuilder();
    }

    public StatementBuilder createTableIfNotExist() {
        this.builder.append("CREATE TABLE IF NOT EXISTS ");

        return this;
    }

    public StatementBuilder table(String tableName) {
        this.builder.append(tableName + " ");

        return this;
    }

    public StatementBuilder insert(String tableName) {
        this.builder.append("INSERT INTO " + tableName + " ");

        return this;
    }

    public StatementBuilder delete(String tableName) {
        this.builder.append("DELETE FROM " + tableName + " ");

        return this;
    }

    public StatementBuilder where() {
        this.builder.append("WHERE ");

        return this;
    }

    public StatementBuilder and() {
        this.builder.append("AND ");

        return this;
    }

    public StatementBuilder or() {
        this.builder.append("OR ");

        return this;
    }

    public StatementBuilder equal(String columnId, Object object) {
        this.builder.append(columnId +"='" + object + "' ");

        return this;
    }

    public StatementBuilder values(Object... values) {
        this.builder.append("VALUES (");

        for (Object value : values) {
            if (value == null) {
                this.builder.append(value + ", ");
                continue;
            }

            this.builder.append("'" + value + "'" + ", ");
        }

        // removing last ', '
        this.builder.deleteCharAt(this.builder.length()-1);
        this.builder.deleteCharAt(this.builder.length()-1);

        this.builder.append(")");

        return this;
    }

    public String asString() {
        this.builder.append(";");
        Bukkit.getLogger().info("Statement preview: " + builder);
        return this.builder.toString();
    }








}
