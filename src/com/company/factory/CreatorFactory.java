package com.company.factory;

import com.company.enums.DatabaseType;
import com.company.util.Creator;
import com.company.util.MySqlCreator;

/**
 * Created by Admin on 2017/9/8.
 */
public class CreatorFactory {
    public static Creator getCreatorInstance(String typeStr, String url, String dbName,
                                             String user, String password) {
        int type = Integer.parseInt(typeStr);

        switch (type) {
            case 1: // mysql
                return new MySqlCreator(url, dbName, user, password);
            case 2: // oracle
                return new MySqlCreator(url, dbName, user, password);
            case 3: // sqlserver
                return new MySqlCreator(url, dbName, user, password);
            default:
                return null;
        }
    }
}
