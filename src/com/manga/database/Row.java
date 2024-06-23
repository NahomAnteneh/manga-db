package com.manga.database;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Cell> row = new ArrayList<>();

    public Row(List<Cell> cells) {
        row = cells;
    }

    public List<Cell> getCells() {
        return row;
    }

    public static class Cell {
        enum DataType { INTEGER, VARCHAR }

        private final DataType dataType;
        private final String value;

        public Cell(DataType dataType, String value) {
            this.dataType = dataType;
            this.value = value;
        }


        public DataType getType() {
            return this.dataType;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return String.format("Cell[DataType=%s, Value='%s']", dataType, value);
        }
    }
}
