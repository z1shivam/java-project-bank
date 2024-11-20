package root;

public class SqlConnector {
    public java.sql.Connection connection;
    public SqlConnector() {
        String dbUrl = "jdbc:mysql://localhost:3306/tmkc";
        try {
            this.connection = java.sql.DriverManager.getConnection(dbUrl, "root", "secret");
        } catch (java.sql.SQLException e) {
            System.out.println(e);
        }
    }

}
