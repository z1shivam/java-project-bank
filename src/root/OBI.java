package root;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import java.util.HashMap;
import java.sql.*;

public class OBI {

    public static HashMap<String, String> envVars = new HashMap<>();

    public static void setupDatabase() {
        SqlConnector x = new SqlConnector();
        Connection connection = x.connection;
        
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "fullName VARCHAR(50), " +
                "dob VARCHAR(10), " +
                "address VARCHAR(100), " +
                "pan VARCHAR(10), " +
                "aadhar VARCHAR(12), " +
                "phone VARCHAR(10), " +
                "isMarried INT, " +
                "isEmp INT, " +
                "gender VARCHAR(20), " +
                "email VARCHAR(50), " +
                "username VARCHAR(50), " +
                "password VARCHAR(100), " +
                "accountNumber BIGINT UNIQUE " +
                ");";
        
        String createTransactionsTableSQL = "CREATE TABLE IF NOT EXISTS tr (" +
                "transactionId VARCHAR(36) PRIMARY KEY, " +
                "tdate DATETIME(3), " +
                "accountNumber BIGINT, " +
                "remarks VARCHAR(255), " +
                "deposit DECIMAL(10,2), " +
                "withdraw FLOAT, " +
                "balance FLOAT " +
                ");";
        
        try {
            if (!isTableExists(connection, "users")) {
                System.out.println("Users table not found. Creating the table...");
                dropTableIfExists(connection, "users");
                createTable(connection, createUsersTableSQL);
            }
            
            if (!isTableExists(connection, "tr")) {
                System.out.println("Transactions table not found. Creating the table...");
                dropTableIfExists(connection, "tr");
                createTable(connection, createTransactionsTableSQL);
            }

            System.out.println("Database setup complete.");
        } catch (SQLException e) {
            System.out.println("Error setting up the database: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }

    private static boolean isTableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next();
    }

    private static void dropTableIfExists(Connection connection, String tableName) throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(dropTableSQL);
        System.out.println(tableName + " table dropped.");
    }

    private static void createTable(Connection connection, String createTableSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(createTableSQL);
        System.out.println("Table created successfully.");
    }
    
    public static void main(String[] args) {
        setupDatabase();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.invokeLater(() -> {
                UI.LoginPane l = new UI.LoginPane();
                l.setVisible(true);
                l.setLocationRelativeTo(null);
            });
        } catch (UnsupportedLookAndFeelException e) {
        }
    }
}
