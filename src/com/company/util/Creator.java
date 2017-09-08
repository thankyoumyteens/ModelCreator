package com.company.util;

import java.util.List;

/**
 * Created by Admin on 2017/9/8.
 */
public abstract class Creator {
    protected String url; // 数据库地址
    protected String database; // 数据库名
    protected String username; // 用户名
    protected String password; // 密码

    protected Creator(String url, String database, String username, String password) {
        this.url = url;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public abstract void createEntityClass();
}
