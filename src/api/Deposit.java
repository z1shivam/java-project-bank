package api;

import java.sql.*;
import java.util.UUID;

public class Deposit {
    public static boolean deposit(double amount) {  // Changed from int to double
        
        root.SqlConnector x = new root.SqlConnector();
        
        try {
            String accountNumber = root.OBI.envVars.get("accountNumber");
            
            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            String transactionId = UUID.randomUUID().toString();

            String fetchBalanceQuery = "SELECT balance FROM tr WHERE accountNumber = ? ORDER BY tdate DESC";
            double currentBalance = 0;

            PreparedStatement fetchBalanceStmt = x.connection.prepareStatement(fetchBalanceQuery);
            fetchBalanceStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchBalanceStmt.executeQuery();

            if (resultSet.next()) {
                currentBalance = resultSet.getDouble("balance");
            }

            double newBalance = currentBalance + amount;  // New balance after deposit
            String insertTransactionQuery = 
                "INSERT INTO tr (transactionId, tdate, accountNumber, remarks, deposit, withdraw, balance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = x.connection.prepareStatement(insertTransactionQuery);
            insertStmt.setString(1, transactionId); // transactionId
            insertStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis())); // current date and time
            insertStmt.setString(3, accountNumber); // accountNumber
            insertStmt.setString(4, "DEPOSIT"); // remarks
            insertStmt.setDouble(5, amount); // deposit (fractional amount)
            insertStmt.setNull(6, Types.FLOAT); // withdraw (NULL for deposit transactions)
            insertStmt.setDouble(7, newBalance); // updated balance

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Failed to insert the deposit transaction.");
            }

            // Close the connection
            x.connection.close();

        } catch (SQLException | IllegalArgumentException err) {
            System.out.println(err);
            return false; 
        }
        return true;
    }
}
