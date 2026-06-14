/**
 * This is a Frame class that extends JFrame and serves as the main window for the Academic Records & Registrar System
 * It contains a Controller instance, a DisplayPanel for showing information, and an InputPanel for user input
 * The class sets up the GUI components and provides methods to refresh the display and input panels.
 *
 * @author Ganta Vikram Jairam Reddy
 **/

package logic.View;

import logic.Controller.Controller;
import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private Controller controller;
    private DisplayPanel displayPanel;
    private InputPanel inputPanel;

    public Frame(Controller controller) {
        this.controller = controller;
        setTitle("Academic Records & Registrar System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.inputPanel = new InputPanel();
        this.displayPanel = new DisplayPanel(this.controller);
        
        MainPanel basePanel = new MainPanel(this.controller, this.displayPanel, this.inputPanel);
        
        setJMenuBar(new MenuOptions(this, this.controller));

        add(inputPanel, BorderLayout.WEST);
        add(displayPanel, BorderLayout.CENTER);
        add(basePanel, BorderLayout.SOUTH);

        displayPanel.updateView();
        setSize(800, 350);
        setLocationRelativeTo(null);
    }

    // Method to refresh the display panel with the latest data from the controller
    public void refreshDisplay() {
        if(displayPanel != null) {
            displayPanel.updateView();
        }
    }

    // method to clear the input fields in the input panel
    public void refreshInput() {
        if(inputPanel != null) {
            inputPanel.clear();
        }
    }
}
