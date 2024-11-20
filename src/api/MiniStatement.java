package api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MiniStatement {

    public static List<String> getMiniStatement() {
        root.SqlConnector x = new root.SqlConnector();
        List<String> transactions = new ArrayList<>();

        try {
            // Fetch account number from environment variables
            String accountNumber = root.OBI.envVars.get("accountNumber");

            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            // Query to fetch the last 5 transactions for the account
            String fetchTransactionsQuery = 
                "SELECT transactionId, tdate, remarks, deposit, withdraw, balance " +
                "FROM tr WHERE accountNumber = ? ORDER BY tdate DESC, transactionId DESC LIMIT 5";

            PreparedStatement fetchTransactionsStmt = x.connection.prepareStatement(fetchTransactionsQuery);
            fetchTransactionsStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchTransactionsStmt.executeQuery();

            // Iterate over the results and build the transaction details
            while (resultSet.next()) {
                String transactionId = resultSet.getString("transactionId");
                Timestamp tdate = resultSet.getTimestamp("tdate");
                String remarks = resultSet.getString("remarks");
                double deposit = resultSet.getDouble("deposit");
                double withdraw = resultSet.getDouble("withdraw");
                double balance = resultSet.getDouble("balance");

                // Format the transaction details as a string and add to the list
                String transactionDetails = String.format("Transaction ID: %s, Date: %s, Remarks: %s, Deposit: %.2f, Withdraw: %.2f, Balance: %.2f",
                        transactionId, tdate.toString(), remarks, deposit, withdraw, balance);
                transactions.add(transactionDetails);
            }

        } catch (SQLException | IllegalArgumentException err) {
            System.out.println("Error: " + err.getMessage());
            return null; // Return null or handle error as needed
        } finally {
            try {
                if (x.connection != null) {
                    x.connection.close();
                }
            } catch (SQLException closeErr) {
                System.out.println("Error closing the connection: " + closeErr.getMessage());
            }
        }

        return transactions;  // Return the list of transactions
    }
}
