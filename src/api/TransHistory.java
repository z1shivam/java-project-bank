/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prana
 */
public class TransHistory {

    /**
     *
     * @return
     */
    public static List<String> getTransHistory() {
        root.SqlConnector x = new root.SqlConnector();
        List<String> transactions = new ArrayList<>();

        try {
            String accountNumber = root.OBI.envVars.get("accountNumber");

            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            String fetchTransactionsQuery = 
                "SELECT transactionId, tdate, remarks, deposit, withdraw, balance " +
                "FROM tr WHERE accountNumber = ? ORDER BY tdate DESC";  //, transactionId DESC LIMIT 5

            PreparedStatement fetchTransactionsStmt = x.connection.prepareStatement(fetchTransactionsQuery);
            fetchTransactionsStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchTransactionsStmt.executeQuery();

            while (resultSet.next()) {
                String transactionId = resultSet.getString("transactionId");
                Timestamp tdate = resultSet.getTimestamp("tdate");
                String remarks = resultSet.getString("remarks");
                double deposit = resultSet.getDouble("deposit");
                double withdraw = resultSet.getDouble("withdraw");
                double balance = resultSet.getDouble("balance");

                String transactionDetails = String.format("Transaction ID: %s, Date: %s, Remarks: %s, Deposit: %.2f, Withdraw: %.2f, Balance: %.2f",
                        transactionId, tdate.toString(), remarks, deposit, withdraw, balance);
                transactions.add(transactionDetails);
            }

        } catch (SQLException | IllegalArgumentException err) {
            System.out.println("Error: " + err.getMessage());
            return null; 
        } finally {
            try {
                if (x.connection != null) {
                    x.connection.close();
                }
            } catch (SQLException closeErr) {
                System.out.println("Error closing the connection: " + closeErr.getMessage());
            }
        }

        return transactions;  
    }
}
