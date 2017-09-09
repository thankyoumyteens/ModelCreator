package com.company.util;

import com.company.model.ColumnInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */
public class OracleCreator extends Creator {

    public OracleCreator(String url, String database, String username, String password) {
        super(url, database, username, password, "oracle.jdbc.OracleDriver");
    }

    @Override
    public void createEntityClass() throws SQLException, ClassNotFoundException, IOException {
        initConnection();

        String queryAllTables = "select table_name from user_tables";

        List<String> tableNameList = getTableNameList(queryAllTables, false);

        String queryAllColumns = "select column_name,data_type,data_length from user_tab_columns where Table_Name = ?";

        for (String tableName : tableNameList) {
            List<ColumnInfo> columnInfoList = getColumnInfoList(queryAllColumns, tableName);
            EntityClassMaker.makeEntityByTableName(tableName, columnInfoList);
        }
    }
}
