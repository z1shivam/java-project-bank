package api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Balance {
    public static double balance() {
        root.SqlConnector x = new root.SqlConnector();
        String accountNumber = root.OBI.envVars.get("accountNumber");

        try {
            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalArgumentException("Account number is not available in environment variables.");
            }

            String fetchBalanceQuery = 
                "SELECT balance FROM tr WHERE accountNumber = ? ORDER BY tdate DESC, transactionId DESC LIMIT 1";

            PreparedStatement fetchBalanceStmt = x.connection.prepareStatement(fetchBalanceQuery);
            fetchBalanceStmt.setString(1, accountNumber);
            ResultSet resultSet = fetchBalanceStmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                throw new SQLException("No balance record found for the given account number.");
            }
        } catch (SQLException | IllegalArgumentException err) {
            System.out.println("Error fetching balance: " + err.getMessage());
            return -1; 
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
