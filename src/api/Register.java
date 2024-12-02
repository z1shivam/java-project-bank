package api;

public class Register {

    public static boolean register(root.UserDetails userDetails) throws Exception {
        root.SqlConnector x = new root.SqlConnector();
        try {
            java.sql.ResultSet isExisting = x.connection
                    .createStatement()
                    .executeQuery("SELECT * FROM users WHERE username = '" + userDetails.username + "';");

            if (isExisting.next()) {
                throw new Exception("User " + userDetails.username + " already exists");
            }

            long min = 10000000000L;
            long max = 99999999999L;
            userDetails.accountNumber = min + (long) (Math.random() * (max - min + 1));

            x.connection
                    .createStatement()
                    .executeUpdate("INSERT INTO users VALUES ("
                            + "'" + userDetails.fullName + "', "
                            + "'" + userDetails.dob + "', "
                            + "'" + userDetails.address + "', "
                            + "'" + userDetails.pan + "', "
                            + "'" + userDetails.aadhar + "', "
                            + "'" + userDetails.phone + "', "
                            + (userDetails.isMarried ? 1 : 0) + ", "
                            + (userDetails.isEmp ? 1 : 0) + ", "
                            + "'" + userDetails.gender + "', "
                            + "'" + userDetails.email + "', "
                            + "'" + userDetails.username + "', "
                            + "'" + userDetails.password + "', "
                            + "'" + userDetails.accountNumber + "', "
                            + "'" + 0 + "',"
                            + "'" + 0 + "', " +
                            ");"
                    );

            String transactionId = java.util.UUID.randomUUID().toString();
            String insertTransactionQuery = 
                "INSERT INTO tr (transactionId, tdate, accountNumber, remarks, deposit, withdraw, balance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

            java.sql.PreparedStatement insertStmt = x.connection.prepareStatement(insertTransactionQuery);
            insertStmt.setString(1, transactionId);
            insertStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            insertStmt.setLong(3, userDetails.accountNumber);
            insertStmt.setString(4, "Account Created");
            insertStmt.setDouble(5, 0.0); 
            insertStmt.setNull(6, java.sql.Types.FLOAT);
            insertStmt.setDouble(7, 0.0); 

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new Exception("Failed to record the account creation transaction.");
            }

            // Close the connection
            x.connection.close();
        } catch (java.sql.SQLException err) {
            System.out.print(err);
            throw new Exception("Failed to register user: " + err);
        }
        return true;
    }
}
