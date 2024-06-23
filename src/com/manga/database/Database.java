package com.manga.database;

import java.util.HashMap;

public class Database {
    private HashMap<String, Table> tables = new HashMap<>();
    private final String dbName;

    public Database(String dbName) {
        this.dbName = dbName;
    }

    public String getName() {
        return this.dbName;
    }

    public void createTable(Table table) {
        tables.put(table.getName(), table);
    }

    public void insertTable(Table table) {
        if (tables.containsKey(table.getName())) {
            System.out.println("Table already exists");
        } else {
            tables.put(table.getName(), table);
        }
    }
}