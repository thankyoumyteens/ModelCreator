package com.company.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class MySqlHelper {
    private String[] colNames; // 列名数组
    private String[] colTypes; //列名类型数组
    private boolean f_util = false; // 是否需要导入包java.util.*
    private boolean f_sql = false; // 是否需要导入包java.sql.*

    public MySqlHelper(String url, String dbName, String tableName, String user, String password) throws SQLException, ClassNotFoundException, IOException {
        //创建连接
        Connection con;
        //查要生成实体类的表
        String sql = "select * from " + tableName;
        PreparedStatement preparedStatement = null;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:MySQL://" + url + "/" + dbName + "?characterEncoding=utf8&useSSL=true",
                user, password);
        preparedStatement = con.prepareStatement(sql);
        ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
        int size = resultSetMetaData.getColumnCount();   //统计列
        colNames = new String[size];
        colTypes = new String[size];
        int[] colSizes = new int[size];
        for (int i = 0; i < size; i++) {
            colNames[i] = resultSetMetaData.getColumnName(i + 1);
            colTypes[i] = resultSetMetaData.getColumnTypeName(i + 1);

            if (colTypes[i].equalsIgnoreCase("datetime")) {
                f_util = true;
            }
            if (colTypes[i].equalsIgnoreCase("image") ||
                    colTypes[i].equalsIgnoreCase("text")) {
                f_sql = true;
            }
            colSizes[i] = resultSetMetaData.getColumnDisplaySize(i + 1);
        }

        String content = makeEntityContent(tableName);

        String dir = "entities";
        String fileName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1) + ".java";
        String path = dir + "/" + fileName;
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(path, false);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    private String makeEntityContent(String tableName) {
        StringBuffer sb = new StringBuffer();

        sb.append("package model;\r\n");

        //判断是否导入工具包
        if (f_util) {
            sb.append("import java.util.Date;\r\n");
        }
        if (f_sql) {
            sb.append("import java.sql.*;\r\n");
        }
        sb.append("\r\n");
        //注释部分
//        sb.append("   /**\r\n");
//        sb.append("    * " + tableName + " 实体类\r\n");
//        sb.append("    */ \r\n");
        //实体部分
        sb.append("\r\npublic class " + initcap(tableName) + " {\r\n");
        processAllAttrs(sb);//属性
        sb.append("\r\n");
        processAllMethod(sb);//get set方法
        sb.append("}\r\n");

        return sb.toString();
    }

    /**
     * 功能：生成所有属性
     *
     * @param sb
     */
    private void processAllAttrs(StringBuffer sb) {

        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + colNames[i] + ";\r\n");
        }

    }

    /**
     * 功能：生成所有方法
     *
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {

        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tpublic void set" + initcap(colNames[i]) + "(" + sqlType2JavaType(colTypes[i]) + " " +
                    colNames[i] + ") {\r\n");
            sb.append("\t\tthis." + colNames[i] + " = " + colNames[i] + ";\r\n");
            sb.append("\t}\r\n");
            sb.append("\r\n");
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(colNames[i]) + "() {\r\n");
            sb.append("\t\treturn " + colNames[i] + ";\r\n");
            sb.append("\t}\r\n");
            sb.append("\r\n");
        }

    }

    /**
     * 功能：将输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    /**
     * 功能：获得列的数据类型
     *
     * @param sqlType
     * @return
     */
    private String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }

        return "String";
    }
}
