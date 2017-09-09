package com.company;

import com.company.factory.CreatorFactory;
import com.company.util.Creator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String url;
        String dbName;

        System.out.println("注意:不输入表示使用默认值");

        System.out.println("请输入数据库地址(127.0.0.1:3306)");
        url = scanner.nextLine();
        if ("".equals(url)) {
            url = "127.0.0.1";
        }
        System.out.println("请输入数据库用户名(root)");
        String user = scanner.nextLine();
        if ("".equals(user)) {
            user = "root";
        }
        System.out.println("请输入数据库密码");
        String password = scanner.nextLine();
        if ("".equals(password)) {
            password = "";
        }
        System.out.println("请输入数据库类别: 1:MySql,2:Oracle,3:SqlServer");
        String dbType = scanner.nextLine();
        if ("".equals(dbType)) {
            dbType = "1";
        }
//        System.out.println("请输入编程语言: 1:Java,2:C#,3:php");
//        String language = scanner.nextLine();
//        if ("".equals(language)) {
//            language = "1";
//        }

        dbName = getDbName(scanner);

        Creator creator = CreatorFactory.getCreatorInstance(dbType, url, dbName, user, password);

        System.out.println("正在执行, 请稍等...");

        while (true) {
            if (creator != null) {
                try {
                    creator.createEntityClass();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("完成");
            }
            System.out.println("继续? y/n");
            String q = scanner.nextLine();
            if ("n".equals(q)) {
                break;
            }
            else if ("y".equals(q)) {
            }
            else {
                System.out.println(q + " 是啥? 算了, 继续");
            }
        }

        System.out.println("拜拜");
    }

    private static String getDbName(Scanner scanner) {
        String dbName;
        while (true) {
            System.out.println("请输入数据库名称");
            dbName = scanner.nextLine();
            if ("".equals(dbName)) {
                System.out.println("数据库名你还想默认!!重来");
            } else {
                break;
            }
        }
        return dbName;
    }

    private static String getTableName(Scanner scanner) {
        String tableName;
        while (true) {
            System.out.println("请输入表名");
            tableName = scanner.nextLine();
            if ("".equals(tableName)) {
                System.out.println("表名你还想默认!!重来");
            } else {
                break;
            }
        }
        return tableName;
    }
}
