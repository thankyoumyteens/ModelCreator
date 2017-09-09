package com.company.util;

import com.company.model.ColumnInfo;

import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 * Created by Admin on 2017/9/8.
 */
public class MySqlCreator extends Creator {


    public MySqlCreator(String url, String database, String username, String password) {
        super(url, database, username, password, "com.mysql.jdbc.Driver");
    }

    @Override
    public void createEntityClass() throws SQLException, ClassNotFoundException, IOException {
        initConnection();

        String queryAllTables = "select table_name from information_schema.tables" +
                " where table_schema=? and table_type='base table'";

        List<String> tableNameList = getTableNameList(queryAllTables, true);

        String queryAllColumns = "SELECT column_name,data_type FROM information_schema.columns WHERE table_name=?";

        for (String tableName : tableNameList) {
            List<ColumnInfo> columnInfoList = getColumnInfoList(queryAllColumns, tableName);
            EntityClassMaker.makeEntityByTableName(tableName, columnInfoList);
        }
    }
}
