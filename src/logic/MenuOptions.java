/**
 * This class creates a menu bar for the application, providing options for saving or loading data, 
 * clearing the form, and deleting records.
 * It interacts with the Controller and updates the Frame accordingly.
 * 
 * @author Ganta Vikram Jairam Reddy
 **/
package logic;

import javax.swing.*;

public class MenuOptions extends JMenuBar {
    private Controller controller;
    private Frame frame;

    public MenuOptions(Frame frame, Controller controller) {
        this.frame = frame;
        this.controller = controller;
        
        JMenu fileMenu = new JMenu("Files");
        JMenu editMenu = new JMenu("Edit");

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem exitItem = new JMenuItem("Exit");

        JMenuItem clearItem = new JMenuItem("Clear Form");
        JMenuItem refreshView = new JMenuItem("Refresh Display");
        JMenuItem deleteItem = new JMenuItem("Delete Record");

        saveItem.addActionListener(e -> handleSave());
        
        loadItem.addActionListener(e -> handleLoad());

        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to exit?", "Exit System", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        clearItem.addActionListener(e -> {
            frame.refreshInput(); 
        });

        refreshView.addActionListener(e -> {
            frame.refreshDisplay(); 
        });

        deleteItem.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter OSU ID to Delete (A12345678):");
            if(id != null) {
                if(controller.deletePerson(id)) {
                    frame.refreshDisplay();
                    controller.saveFile();
                    JOptionPane.showMessageDialog(frame, "Record Deleted Successfully.");
                } 
                else {
                    JOptionPane.showMessageDialog(frame, "Error: ID not found.");
                }
            }
        });

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        editMenu.add(clearItem);
        editMenu.add(refreshView);
        editMenu.addSeparator();
        editMenu.add(deleteItem);
        
        this.add(fileMenu);
        this.add(editMenu);
    }

    // Method to handle saving data through the controller and showing success or error messages
    private void handleSave() {
        try {
            controller.saveFile();
            JOptionPane.showMessageDialog(frame, "Data Saved successfully!");
        } 
        catch(Exception ex) {
            JOptionPane.showMessageDialog(frame, "Save Error: " + ex.getMessage());
        }
    }

    // Method to handle loading data through the controller and refreshing the display
    private void handleLoad() {
        try {
            controller.loadData();
            frame.refreshDisplay(); 
            JOptionPane.showMessageDialog(frame, "Data Loaded successfully!");
        } 
        catch(Exception ex) {
            JOptionPane.showMessageDialog(frame, "Load Error: " + ex.getMessage());
        }
    }
}
