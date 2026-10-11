package gym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:src/main/resources/database/gym.db";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                System.out.println("✅ Połączono z bazą danych: " + URL);
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Błąd: Brak sterownika SQLite JDBC!");
                throw new SQLException("Driver not found", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Zamknięto połączenie z bazą danych.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }

    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Test połączenia: SUKCES!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Test połączenia: BŁĄD - " + e.getMessage());
        }
    }
}