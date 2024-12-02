/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prana
 */
public class LoanStatus {
    public static List<String> getLoanStatus() {
        root.SqlConnector x = new root.SqlConnector();
        List<String> loanStatus = new ArrayList<>();

        try {
            String accountNumber = root.OBI.envVars.get("accountNumber");

            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            String fetchTransactionsQuery = 
                "SELECT accountNumber, loanId, amount, rate, time, si, loanAmount" +
                "FROM loan WHERE accountNumber = ?";

            PreparedStatement fetchTransactionsStmt = x.connection.prepareStatement(fetchTransactionsQuery);
            fetchTransactionsStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchTransactionsStmt.executeQuery();

            while (resultSet.next()) {
                String accNumber = resultSet.getString("accountNumber");
                String loanId = resultSet.getString("loanId");
                double amount = resultSet.getDouble("amount");
                double rate = resultSet.getDouble("rate");
                double time = resultSet.getDouble("time");
                double si = resultSet.getDouble("si");
                double lAmount = resultSet.getDouble("loanAmount");

                String loanDetails = String.format("Account No.: %s, LoanID: %s, Amount: %.2f, Rate: %.2f, Time: %.2f, Simple Interest: %.2f, Principle Amount: %.2f",
                        accNumber, loanId, amount, rate, time, si, lAmount);
                loanStatus.add(loanDetails);
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

        return loanStatus;  
    }
}
