package com.company.util;

import com.company.model.ColumnInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 * 生成实体类文件
 */
public class EntityClassMaker {

    /**
     * 创建一张表的实体类文件
     *
     * @param columnInfoList
     * @param tableName
     * @throws SQLException
     * @throws IOException
     */
    public static void makeEntityByTableName(String tableName, List<ColumnInfo> columnInfoList)
            throws SQLException, IOException {
        boolean isDateTime = false;

        for (ColumnInfo columnInfo : columnInfoList) {
            if (columnInfo.getColumnType().contains("date")) {
                isDateTime = true;
            }
        }

        // java命名规范
        tableName = convertToJavaStyle(tableName);
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);

        String content = makeEntityContent(tableName, isDateTime, columnInfoList);

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
     *
     * @param string 数据库中的字段
     * @return 转换后的名称
     */
    private static String convertToJavaStyle(String string) {
        string = string.toLowerCase();
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
     *
     * @param tableName
     * @param isDateTime
     * @param columnInfoList
     * @return
     */
    private static String makeEntityContent(String tableName, boolean isDateTime, List<ColumnInfo> columnInfoList) {
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
        for (ColumnInfo columnInfo : columnInfoList) {
            String column = convertToJavaStyle(columnInfo.getColumnName());
            String type = convertToJavaStyle(columnInfo.getColumnType());
            sb.append("\tprivate ").append(sqlType2JavaType(type)).append(" ").append(column).append(";");
            sb.append("\r\n");
        }
        sb.append("\r\n");
        // getter和setter
        for (ColumnInfo columnInfo : columnInfoList) {
            String name = columnInfo.getColumnName();
            String type = columnInfo.getColumnType();
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

    /**
     * 返回和数据库类型对应的java类型
     *
     * @param sqlType
     * @return
     */
    private static String sqlType2JavaType(String sqlType) {
        sqlType = sqlType.toLowerCase();

        switch (sqlType) {
            case "bit":
                return  "boolean";
            case "tinyint":
                return  "byte";
            case "smallint":
                return  "short";
            case "int":
                return  "int";
            case "bigint":
            case "number":
                return  "long";
            case "float":
                return  "float";
            case "decimal":
            case "numeric":
            case "real":
            case "money":
            case "smallmoney":
                return  "double";
            case "varchar":
            case "varchar2":
            case "char":
            case "nvarchar":
            case "nchar":
            case "text":
                return  "String";
            case "date":
            case "datetime":
                return  "Date";
            case "image":
                return  "Blod";
            default:
                return  "String";
        }
    }
}
