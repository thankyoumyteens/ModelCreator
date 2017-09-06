package com.company;

import com.company.util.MySqlHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String url;
        String dbName;
        String tableName;

        System.out.println("注意:输入1表示使用默认值");

        System.out.println("请输入数据库地址(例:127.0.0.1:3306)");
        url = scanner.nextLine();
        if ("1".equals(url)) {
            url = "127.0.0.1";
        }
        System.out.println("请输入数据库用户名");
        String user = scanner.nextLine();
        if ("1".equals(user)) {
            user = "root";
        }
        System.out.println("请输入数据库密码");
        String password = scanner.nextLine();
        if ("1".equals(password)) {
            password = "";
        }

        dbName = getDbName(scanner);

        while (true) {
            tableName = getTableName(scanner);

            try {
                new MySqlHelper(url, dbName, tableName, user, password);
                System.out.println("完成(文件目录:./entities)");
            } catch (SQLException e) {
                System.out.println("数据库相关信息打错了吧");
            } catch (ClassNotFoundException e) {
                System.out.println("找不到mysql驱动");
            } catch (IOException e) {
                System.out.println("磁盘不存在或者没有写入权限");
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
            if ("1".equals(dbName)) {
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
            if ("1".equals(tableName)) {
                System.out.println("表名你还想默认!!重来");
            } else {
                break;
            }
        }
        return tableName;
    }
}
