/**
 * Main class to launch the Academic Records Management System application.
 * It initializes the GUI frame, passing a Controller instance.
 * 
 * SwingUtilities.invokeLater is used to ensure that the 
 * GUI is created and updated on the Event Dispatch Thread, 
 * so that the application does not freeze and remains responsive.
 * 
 * @author Ganta Vikram Jairam Reddy
 **/

import logic.*;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame(new Controller());
            frame.setVisible(true);
        });
    }
}