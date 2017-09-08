package com.company.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成实体类文件
 */
public class EntityClassMaker {

    /**
     * 创建一张表的实体类文件
     * @param connection
     * @param tableName
     * @throws SQLException
     * @throws IOException
     */
    public static void makeEntityByTableName(Connection connection, String tableName)
            throws SQLException, IOException {
        boolean isDateTime = false;

        String sql = "select * from " + tableName;

        // java命名规范
        tableName = convertToJavaStyle(tableName);
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);

        List<String> columnNameList = new ArrayList<>();
        List<String> columnTypeList = new ArrayList<>();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(sql);
        ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnName = resultSetMetaData.getColumnName(i + 1);
            String columnTypeName = resultSetMetaData.getColumnTypeName(i + 1);

            if (columnTypeName.equalsIgnoreCase("datetime")) {
                isDateTime = true;
            }
            columnNameList.add(columnName);
            columnTypeList.add(columnTypeName);
        }

        String content = makeEntityContent(tableName, isDateTime, columnNameList, columnTypeList);

        String dir = "entities";
        String fileName = tableName + ".java";
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

    /**
     * 将数据库中有下划线的名称转换为驼峰式名称
     * @param string 数据库中的字段
     * @return 转换后的名称
     */
    private static String convertToJavaStyle(String string) {
        if (string.contains("_")) {
            String[] strings = string.split("_");
            StringBuilder stringBuilder = new StringBuilder(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                String item = strings[i];
                stringBuilder.append(item.substring(0, 1).toUpperCase()).append(item.substring(1));
            }
            return stringBuilder.toString();
        } else {
            return string;
        }
    }

    /**
     * 生成实体类的文件内容
     * @param tableName
     * @param isDateTime
     * @param columnNameList
     * @param columnTypeList
     * @return
     */
    private static String makeEntityContent(String tableName, boolean isDateTime, List<String> columnNameList, List<String> columnTypeList) {
        StringBuilder sb = new StringBuilder();
        sb.append("package entities;\r\n");
        //判断是否导入工具包
        if (isDateTime) {
            sb.append("import java.util.Date;\r\n");
        }
        sb.append("\r\n");
        sb.append("\r\n");
        //类
        sb.append("public class ").append(tableName).append(" {");
        sb.append("\r\n");
        // 属性
        for (String column : columnNameList) {
            column = convertToJavaStyle(column);
            sb.append("\tprivate ").append(sqlType2JavaType(column)).append(" ").append(column).append(";");
            sb.append("\r\n");
        }
        sb.append("\r\n");
        // getter和setter
        for (int i = 0; i < columnNameList.size(); i++) {
            String name = columnNameList.get(i);
            String type = columnTypeList.get(i);
            name = convertToJavaStyle(name);

            sb.append("\tpublic void set").append(firstLetterToUpperCase(name)).append("(").append(sqlType2JavaType(type)).append(" ").append(name).append(") {\r\n");
            sb.append("\t\tthis.").append(name).append(" = ").append(name).append(";\r\n");
            sb.append("\t}\r\n");
            sb.append("\r\n");
            sb.append("\tpublic ").append(sqlType2JavaType(type)).append(" get").append(firstLetterToUpperCase(name)).append("() {\r\n");
            sb.append("\t\treturn ").append(name).append(";\r\n");
            sb.append("\t}\r\n");
            sb.append("\r\n");
        }
        sb.append("}");
        sb.append("\r\n");

        return sb.toString();
    }

    private static String firstLetterToUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private static String sqlType2JavaType(String sqlType) {

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
