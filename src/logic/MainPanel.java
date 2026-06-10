/**
 * This class is the main panel of the application,
 * with buttons for adding, updating, searching, and deleting records.
 * It interacts with the Controller to perform these operations and updates the DisplayPanel accordingly.
 * 
 * Responsibilities:
 * - Provide a user interface for interacting with the application
 * - Handle user input and trigger appropriate actions in the Controller
 * 
 * @author Ganta Vikram Jairam Reddy
 **/

package logic;

import javax.swing.*;

public class MainPanel extends JPanel {
    private Controller controller;
    private DisplayPanel dispPanel;
    private InputPanel inputPanel;

    public MainPanel(Controller controller, DisplayPanel dispPanel, InputPanel inputPanel) {
        this.controller = controller;
        this.dispPanel = dispPanel;
        this.inputPanel = inputPanel;

        JButton addButton = new JButton("Add");
        JButton searchButton = new JButton("Search ID");
        JButton deleteButton = new JButton("Delete ID");
        JButton updateButton = new JButton("Update ID");

        // ADD
        addButton.addActionListener(e -> {
            try {
                String name = this.inputPanel.getNameText();
                String id = this.inputPanel.getIdText();
                String type = this.inputPanel.getType();

                if(type.equalsIgnoreCase("Student")) {
                    double gpa = Double.parseDouble(this.inputPanel.getGpaSalaryText());
                    String major = this.inputPanel.getMajorDeptText();

                    boolean added;
                    if(!this.inputPanel.getCoursesText().isEmpty()) {
                        String[] rawCourses = this.inputPanel.getCoursesText().split(",");
                        StringBuilder courses = new StringBuilder();

                        for(String course : rawCourses) {
                            courses.append(course.trim()).append(";");
                        }

                        added = controller.addPerson(new Student(name, id, gpa, major, courses.toString()));
                    }
                    else {
                        added = controller.addPerson(new Student(name, id, gpa, major));
                    }

                    if(!added) {
                        throw new IllegalArgumentException("ID already exists.");
                    }
                } 
                else {
                    double salary = Double.parseDouble(this.inputPanel.getGpaSalaryText());
                    String dept = this.inputPanel.getMajorDeptText();

                    boolean added = controller.addPerson(new Faculty(name, id, salary, dept));

                    if(!added) {
                        throw new IllegalArgumentException("ID already exists.");
                    }
                }
                this.dispPanel.updateView();
                this.controller.saveFile();
                this.inputPanel.clear();
            } 
            catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // SEARCH 
        searchButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter ID to Search:");
            Person p = this.controller.findPerson(id);
            if(p != null) { 
                dispPanel.showSingle(p);
            }
            else {
                JOptionPane.showMessageDialog(this, "Not Found");
            }
        });

        // DELETE
        deleteButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter ID to delete:");
            if(this.controller.deletePerson(id)) {
                dispPanel.updateView();
                controller.saveFile();
                JOptionPane.showMessageDialog(this, "Deleted!");
            } 
            else {
                JOptionPane.showMessageDialog(this, "ID not found.");
            }
        });

        // UPDATE
        updateButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter ID to update:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID cannot be empty.");
                return;
            }

            Person p = this.controller.findPerson(id);
            if(p == null) {
                JOptionPane.showMessageDialog(this, "Record not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // update options based on type
            String[] choices;
            if (p instanceof Student) {
                choices = new String[]{"Name", "ID", "Major", "GPA", "Add Course", "Clear All Courses", "Done Updating"};
            } else {
                choices = new String[]{"Name", "ID", "Department", "Salary", "Done Updating"};
            }

            boolean isUpdated = false;
            boolean changesMade = false;
            String currentId = id;

            while(!isUpdated) {
                String currentInfo = "Editing: " + p.getName() + " (ID: " + currentId + ")";
                if (p instanceof Student) {
                    Student s = (Student) p;
                    currentInfo += "\nCurrent Courses: " + String.join(", ", s.getCourses());
                }

                String choice = (String) JOptionPane.showInputDialog(this, 
                    currentInfo + "\nSelect a field to update:", "Multi-Update for ID: " + currentId, 
                    JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

                if(choice == null || choice.equalsIgnoreCase("Done Updating")) {
                    isUpdated = true;
                    continue;
                } 

                if(choice.equalsIgnoreCase("Clear All Courses")) {
                    Student s = (Student) p;
                    s.getCourses().clear();

                    this.controller.saveFile();
                    this.dispPanel.updateView();
                    JOptionPane.showMessageDialog(this, "All courses removed!");
                    changesMade = true;
                    continue;
                }

                String text = (choice.equalsIgnoreCase("Add Course"))?
                             "Enter course to add:" : "Enter your new " + choice + ":";

                String newValue = JOptionPane.showInputDialog(this, text);
                if(newValue == null || newValue.trim().isEmpty()) {
                    continue;
                }
                
                // Performs the update based on the choice
                try {
                    switch (choice) {
                        case "Name": controller.updateName(currentId, newValue);
                                    break;
                        case "ID":
                            if(!newValue.equalsIgnoreCase(currentId) && controller.findPerson(newValue) != null) {
                                JOptionPane.showMessageDialog(this, "Error: This new ID is already taken");
                                continue;
                            }
                            controller.updateId(currentId, newValue);
                            currentId = newValue; 
                            break;
                        case "Major": controller.updateMajor(currentId, newValue);
                            break;
                        case "GPA": controller.updateGpa(currentId, Double.parseDouble(newValue));
                            break;
                        case "Add Course":
                            Student s = (Student) p;
                            s.addCourses(newValue.trim());
                            break;
                        case "Department": controller.updateDepartment(currentId, newValue);
                            break;
                        case "Salary": controller.updateSalary(currentId, Double.parseDouble(newValue));
                            break;
                    }

                    changesMade = true;
                    JOptionPane.showMessageDialog(this, choice + " updated successfully!");

                    this.dispPanel.updateView();
                    this.controller.saveFile();
                    this.inputPanel.clear();
            
                } 
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Please enter a valid number for your GPA or Salary.");
                } 
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
            if(changesMade) {
                JOptionPane.showMessageDialog(this, 
                    "All changes have been saved into file successfully!");
            }
        });

        add(addButton); 
        add(updateButton);
        add(searchButton); 
        add(deleteButton);
    }
}
