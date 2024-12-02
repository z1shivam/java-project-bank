/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

/**
 *
 * @author aksha
 */
public class BankTransfer {

    public static boolean bt(String acn2, double amount, String ifsc, String remark) {
        try {
            root.SqlConnector x = new root.SqlConnector();
            String accountNumber = root.OBI.envVars.get("accountNumber");

            if (accountNumber == null || accountNumber.isEmpty() || acn2 == null || acn2.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            String transactionId = UUID.randomUUID().toString();

            String fetchBalanceQuery = "SELECT balance FROM tr WHERE accountNumber = ? ORDER BY tdate DESC";
            double currentBalance = 0;

            String fetchBalanceQuery2 = "SELECT balance FROM tr WHERE accountNumber = ? ORDER BY tdate DESC";
            double currentBalance2 = 0;

            PreparedStatement fetchBalanceStmt = x.connection.prepareStatement(fetchBalanceQuery);
            fetchBalanceStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchBalanceStmt.executeQuery();

            PreparedStatement fetchBalanceStmt2 = x.connection.prepareStatement(fetchBalanceQuery2);
            fetchBalanceStmt2.setString(1, acn2);
            ResultSet resultSet2 = fetchBalanceStmt2.executeQuery();

            if (resultSet.next()) {
                currentBalance = resultSet.getDouble("balance");
            }

            if (resultSet2.next()) {
                currentBalance2 = resultSet2.getDouble("balance");
            }

            double newBalance = currentBalance - amount;
            double newBalance2 = currentBalance2 + amount;

            String insertTransactionQuery
                    = "INSERT INTO tr (transactionId, tdate, accountNumber, remarks, deposit, withdraw, balance) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = x.connection.prepareStatement(insertTransactionQuery);
            insertStmt.setString(1, transactionId);
            insertStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            insertStmt.setString(3, accountNumber);
            insertStmt.setString(4, "DEPOSIT");
            insertStmt.setDouble(5, amount);
            insertStmt.setNull(6, Types.FLOAT);
            insertStmt.setDouble(7, newBalance2);

            String insertTransactionQuery1
                    = "INSERT INTO tr (transactionId, tdate, accountNumber, remarks, deposit, withdraw, balance) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt1 = x.connection.prepareStatement(insertTransactionQuery1);
            insertStmt1.setString(1, transactionId);
            insertStmt1.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            insertStmt1.setString(3, accountNumber);
            insertStmt1.setString(4, "DEPOSIT");
            insertStmt1.setNull(5, Types.FLOAT);
            insertStmt1.setDouble(6, amount);
            insertStmt1.setDouble(7, newBalance);

            String insertTransactionQuery2
                    = "INSERT INTO bt (tid, tdate, acn1, acn2, ifsc, remarks, amountTransfered)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt2 = x.connection.prepareStatement(insertTransactionQuery2);
            insertStmt2.setString(1, transactionId);
            insertStmt2.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            insertStmt2.setString(3, accountNumber);
            insertStmt2.setString(4, acn2);
            insertStmt2.setString(5, ifsc);
            insertStmt2.setString(6, remark);
            insertStmt2.setDouble(7, amount);

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Failed to complete the transaction.");
            }

            x.connection.close();

        } catch (SQLException | IllegalArgumentException err) {
            System.out.println(err);
            return false;
        }
        return true;
    }
}
