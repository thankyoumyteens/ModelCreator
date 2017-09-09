package com.company.util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Admin on 2017/9/8.
 */
public class MySqlCreatorTest {
    @Test
    public void createClassContentTest() throws Exception {
        String str = "jdbc:oracle:thin:@localhost:1521:orcl";

        Class.forName("oracle.jdbc.OracleDriver");
        Connection connect = DriverManager.getConnection(str, "ricky", "honglang");
        PreparedStatement preState =
                connect.prepareStatement("select column_name,data_type,data_length from user_tab_columns where Table_Name = ?");
//        preState.setInt(1, 2);
        preState.setString(1, "USPTOTEST");
        ResultSet resultSet = preState.executeQuery();
        while (resultSet.next())
        {
//            int id = resultSet.getInt("id");
            String name = resultSet.getString("column_name");
//            String city = resultSet.getString("city");
            System.out.println(name);  //打印输出结果集
        }

        System.out.println("OK");
    }

    @Test
    public void mysqlTest() throws Exception {
        MySqlCreator creator = new MySqlCreator("127.0.0.1", "groupon", "root", "honglang");
        creator.createEntityClass();
        System.out.println("OK");
    }

    @Test
    public void oracleTest() throws Exception {
        OracleCreator creator = new OracleCreator("localhost:1521", "orcl", "ricky", "honglang");
        creator.createEntityClass();
        System.out.println("OK");
    }


}
