package com.company.util;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/9/8.
 */
public class MySqlCreator extends Creator {
    public MySqlCreator(String url, String database, String username, String password) {
        super(url, database, username, password);
    }

    @Override
    public void createEntityClass() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:MySQL://" +
                            this.url + "/" + this.database +
                            "?characterEncoding=utf8&useSSL=true",
                    this.username, this.password);

            String queryAllTables = "select table_name from information_schema.tables" +
                    " where table_schema=? and table_type='base table'";
            PreparedStatement preparedStatement = con.prepareStatement(queryAllTables);
            preparedStatement.setString(1, this.database);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> tableNameList = new ArrayList<>();

            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                tableNameList.add(tableName);
            }

            resultSet.close();
            preparedStatement.close();

            String queryAllColumns = "select column_name from information_schema.columns" +
                    " where table_schema=? and table_name=?";

            for (String tableName : tableNameList) {
                EntityClassMaker.makeEntityByTableName(con, tableName);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
