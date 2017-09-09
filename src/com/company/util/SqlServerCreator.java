package com.company.util;

import com.company.model.ColumnInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */
public class SqlServerCreator extends Creator {
    public SqlServerCreator(String url, String database, String username, String password) {
        super(url, database, username, password, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    @Override
    public void createEntityClass() throws SQLException, ClassNotFoundException, IOException {
        initConnection();

        String queryAllTables = "Select Name FROM SysObjects Where XType='U' orDER BY Name";

        List<String> tableNameList = getTableNameList(queryAllTables, false);

        String queryAllColumns = "select b.name,c.name from sysobjects a,syscolumns b,systypes c where a.id=b.id " +
                "and a.name=? and a.xtype='U' " +
                "and b.xtype=c.xtype";

        for (String tableName : tableNameList) {
            List<ColumnInfo> columnInfoList = getColumnInfoList(queryAllColumns, tableName);
            EntityClassMaker.makeEntityByTableName(tableName, columnInfoList);
        }
    }
}
