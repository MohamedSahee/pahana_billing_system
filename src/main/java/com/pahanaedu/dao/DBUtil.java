package com.pahanaedu.util;

import java.sql.*;
import java.util.Properties;

public class DBUtil {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Properties props = new Properties();
                props.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        props.getProperty("jdbc.url"),
                        props.getProperty("jdbc.user"),
                        props.getProperty("jdbc.password")
                );
            } catch (Exception e) {
                throw new SQLException("Database connection failed", e);
            }
        }
        return connection;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignored */ }
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { /* ignored */ }
        try { if (conn != null) conn.close(); } catch (SQLException e) { /* ignored */ }
    }
}