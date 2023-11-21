package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    public static Connection getConnection() {
        try {
            String jdbcUrl = "jdbc:mysql://localhost:3306/department";
            String username = "root";
            String password = "pass";
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Не вдалося підключитися до бази даних!");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}