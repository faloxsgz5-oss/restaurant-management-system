package Restaurant;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection conn = null;

    // เชื่อมต่อฐานข้อมูล
    public static Connection connect() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }

    // ใช้สำหรับ SELECT
    public static ResultSet executeQuery(String sql) throws SQLException {
        Statement stmt = connect().createStatement();
        return stmt.executeQuery(sql);
    }

    // ใช้สำหรับ INSERT, UPDATE, DELETE
    public static int executeUpdate(String sql) throws SQLException {
        Statement stmt = connect().createStatement();
        return stmt.executeUpdate(sql);
    }

    // ปิดการเชื่อมต่อ
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}