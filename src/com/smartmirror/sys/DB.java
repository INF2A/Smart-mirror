package com.smartmirror.sys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Erwin on 6/12/2017.
 */
public class DB {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");
                return connection;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    private DB() {}

}
