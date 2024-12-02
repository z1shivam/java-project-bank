package api;

import java.sql.*;
import java.util.UUID;

public class Deposit {
    public static boolean deposit(double amount) {  
        
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

            double newBalance = currentBalance + amount;  
            String insertTransactionQuery = 
                "INSERT INTO tr (transactionId, tdate, accountNumber, remarks, deposit, withdraw, balance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = x.connection.prepareStatement(insertTransactionQuery);
            insertStmt.setString(1, transactionId); 
            insertStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis())); 
            insertStmt.setString(3, accountNumber);
            insertStmt.setString(4, "DEPOSIT"); 
            insertStmt.setDouble(5, amount); 
            insertStmt.setNull(6, Types.FLOAT); 
            insertStmt.setDouble(7, newBalance); 

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Failed to insert the deposit transaction.");
            }

            x.connection.close();

        } catch (SQLException | IllegalArgumentException err) {
            System.out.println(err);
            return false; 
        }
        return true;
    }
}
