package api;

import javax.swing.SwingUtilities;

public class Login {

    public static boolean login(String username, String password) {
        root.SqlConnector x = new root.SqlConnector();

        try {
            java.sql.ResultSet r = x.connection.createStatement()
                    .executeQuery("SELECT * FROM users where username = '"
                            + username + "' and password='" + password + "';");
            if (r.next()) {
                root.OBI.envVars.put("username", username);
                root.OBI.envVars.put("password", password);
                root.OBI.envVars.put("fullName", r.getString("fullName"));
                root.OBI.envVars.put("phoneNumber", r.getString("phone"));
                root.OBI.envVars.put("accountNumber", r.getString("accountNumber"));
                root.OBI.envVars.put("panNumber", r.getString("pan"));
                root.OBI.envVars.put("aadharNumber", r.getString("aadhar"));
                root.OBI.envVars.put("isFreeze", r.getString("isFreezed"));
                return true;
            }

            x.connection.close();
        } catch (java.sql.SQLException err) {
            SwingUtilities.invokeLater(() -> {
                javax.swing.JFrame r = new UI.Dialog(err.toString());
                r.setVisible(true);
                r.setTitle("Login Error!!");
                r.setLocationRelativeTo(null);
            });
        }

        return false;
    }
}
