package com.manga.database;

import java.util.List;

public class Table {
    private final String tableName;
    private List<Row> rows;

    public Table(String tableName, Row row) {
        this.tableName = tableName;
        rows.add(row);
    }

    public String getName() {
        return this.tableName;
    }

    public List<Row> getRows() {
        return this.rows;
    }
}