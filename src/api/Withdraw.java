package api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Withdraw {
    public static boolean withdraw(double amount) {
        root.SqlConnector x = new root.SqlConnector();

        try {
            String accountNumber = root.OBI.envVars.get("accountNumber");

            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            String transactionId = UUID.randomUUID().toString();

            String fetchBalanceQuery = 
                "SELECT balance FROM tr WHERE accountNumber = ? ORDER BY tdate DESC, transactionId DESC LIMIT 1";
            double currentBalance = 0;

            PreparedStatement fetchBalanceStmt = x.connection.prepareStatement(fetchBalanceQuery);
            fetchBalanceStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchBalanceStmt.executeQuery();

            if (resultSet.next()) {
                currentBalance = resultSet.getDouble("balance");
            } else {
                throw new SQLException("No balance found for the given account number.");
            }

            double newBalance = currentBalance - amount;
            if (newBalance < 0) {
                System.out.println("Insufficient balance.");
                return false;
            }

            String insertTransactionQuery = 
                "INSERT INTO tr (transactionId, tdate, accountNumber, remarks, deposit, withdraw, balance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = x.connection.prepareStatement(insertTransactionQuery);
            insertStmt.setString(1, transactionId); // transactionId
            insertStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis())); // tdate with millisecond precision
            insertStmt.setString(3, accountNumber); // accountNumber
            insertStmt.setString(4, "WITHDRAWAL"); // remarks
            insertStmt.setNull(5, java.sql.Types.DECIMAL); // deposit is null for withdrawals
            insertStmt.setDouble(6, amount); // withdrawal amount
            insertStmt.setDouble(7, newBalance); // updated balance

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Failed to insert the withdrawal transaction.");
            }

            System.out.println("Withdrawal transaction successful.");
            return true;

        } catch (SQLException | IllegalArgumentException err) {
            System.out.println("Error: " + err.getMessage());
            return false;
        } finally {
            try {
                if (x.connection != null) {
                    x.connection.close();
                }
            } catch (SQLException closeErr) {
                System.out.println("Error closing the connection: " + closeErr.getMessage());
            }
        }
    }
}
