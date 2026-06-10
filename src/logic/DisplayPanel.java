/**
 * This is a DisplayPanel class.
 *
 * Purpose:
 * This acts as the view layer in the MVC architecture.
 * It displays the data to the user in a formatted manner.
 *
 * @author Ganta Vikram Jairam Reddy
 **/
package logic;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {
    
    private JTextArea textArea;
    private Controller controller;

    public DisplayPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Database Records"));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    // Method to update the display with the latest data from the controller
    public void updateView() {
        textArea.setText(controller.listAllDetails());
        textArea.revalidate();
        textArea.repaint();
    }

    // Method to display a single person's details in the text area
    public void showSingle(Person p) {
        textArea.setText("--- SEARCH RESULT ---\n\n" + p.getDetails());
    }
}
