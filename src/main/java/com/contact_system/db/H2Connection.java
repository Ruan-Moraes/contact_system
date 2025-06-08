package com.contact_system.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class H2Connection {
    private static Connection connection;

    public static String getDB_URL() {
        Properties props = loadProperties();

        return props.getProperty("DB_URL") + ":" + props.getProperty("DB_NAME") + ";" + props.getProperty("DB_OPTIONS");
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(getDB_URL());

                connection.prepareStatement("CREATE TABLE IF NOT EXISTS tb_user (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "name VARCHAR(255), " +
                        "password VARCHAR(255))").executeUpdate();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS tb_telephone (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "user_id INT, " +
                        "number VARCHAR(14) UNIQUE, " +
                        "FOREIGN KEY (user_id) REFERENCES tb_user(id))").executeUpdate();
            }

            return connection;
        } catch (SQLException e) {
            throw new DbException("Failed to connect to the database", e.getCause());
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException("Failed to close the statement", e.getCause());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException("Failed to close the result set", e.getCause());
            }
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DbException("Failed to close the connection", e.getCause());
        }
    }

    public static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();

            props.load(fs);

            return props;
        } catch (IOException e) {
            throw new DbException("Failed to load the properties file", e.getCause());
        }
    }
}

