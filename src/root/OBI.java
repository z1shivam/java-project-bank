package root;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import java.util.HashMap;

public class OBI {

    public static HashMap<String, String> envVars = new HashMap<>();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.invokeLater(() -> {
                UI.LoginPane l = new UI.LoginPane();
                l.setVisible(true);
                l.setLocationRelativeTo(null);
            });
        } catch (UnsupportedLookAndFeelException e) {
        }
    }
}
