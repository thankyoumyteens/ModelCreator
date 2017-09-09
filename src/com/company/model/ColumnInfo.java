package com.company.model;

/**
 * Created by Admin on 2017/9/9.
 */
public class ColumnInfo {
    private String columnName;
    private String columnType;

    public ColumnInfo() {
    }

    public ColumnInfo(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
