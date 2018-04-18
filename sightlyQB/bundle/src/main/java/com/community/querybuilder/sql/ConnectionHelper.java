package com.community.querybuilder.sql;

/**
 * Created by ishbaner1 on 4/4/18.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper
{
    private String url;
    private static ConnectionHelper instance;
    private ConnectionHelper()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            url = "jdbc:mysql://localhost:3306/cq";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new ConnectionHelper();
        }
        try {
            return DriverManager.getConnection(instance.url, "root", "mypass");
        }
        catch (SQLException e) {
            throw e;
        }
    }
    public static void close(Connection connection)
    {
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
