package com.company.util;

import com.company.model.ColumnInfo;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/9/8.
 */
public abstract class Creator {
    private String url; // 数据库地址
    private String database; // 数据库名
    private String username; // 用户名
    private String password; // 密码
    private String driver; // 数据库链接驱动
    private Connection connection;

    Creator(String url, String database, String username, String password, String driver) {
        this.url = url;
        this.database = database;
        this.username = username;
        this.password = password;
        this.driver = driver;
    }

    /**
     * 初始化连接
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    void initConnection() throws SQLException, ClassNotFoundException {
        Class.forName(this.driver);
        String urlStr = "";
        if (this.driver.contains("mysql")) {
            urlStr = "jdbc:MySQL://" +
                    this.url + "/" + this.database +
                    "?characterEncoding=utf8&useSSL=true";
        }
        if (this.driver.contains("oracle")) {
            urlStr = "jdbc:oracle:thin:@" + this.url + ":" + this.database;
        }
        if (this.driver.contains("sqlserver")) {
            urlStr = "jdbc:sqlserver://" + this.url + ";databaseName=" + this.database;
        }
        this.connection = DriverManager.getConnection(urlStr, this.username, this.password);
    }

    /**
     * 获取数据库中的所有表名
     * @param queryAllTables
     * @return
     * @throws SQLException
     */
    List<String> getTableNameList(String queryAllTables, boolean hasParam) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(queryAllTables);
        if (hasParam) {
            preparedStatement.setString(1, this.database);
        }
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> tableNameList = new ArrayList<>();

        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            tableNameList.add(tableName);
        }

        resultSet.close();
        preparedStatement.close();
        return tableNameList;
    }

    /**
     * 获取表的所有字段名和类型
     * @param queryAllColumns
     * @param tableName
     * @return
     * @throws SQLException
     */
    List<ColumnInfo> getColumnInfoList(String queryAllColumns, String tableName) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(queryAllColumns);
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<ColumnInfo> columnInfoList = new ArrayList<>();

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            String type = resultSet.getString(2);
            columnInfoList.add(new ColumnInfo(name, type));
        }

        resultSet.close();
        preparedStatement.close();
        return columnInfoList;
    }

    public abstract void createEntityClass() throws SQLException, ClassNotFoundException, IOException;
}
