package com.example.mysqlbinlog.mode;

public class TableColumInfo {

    private String columnName;
    private int ordinalPosition;
    private String tableName;

    public TableColumInfo tableNameToLower(){
        setTableName(getTableName().toLowerCase());

        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
