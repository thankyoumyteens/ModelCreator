package com.company.enums;

/**
 * Created by Admin on 2017/9/8.
 */
public enum DatabaseType {
    MySql(1), Oracle(2), SqlServer(3);

    private int value;

    private DatabaseType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
