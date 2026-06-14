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

package logic.View;

import java.util.List;
import javax.swing.*;
import logic.Controller.*;
import logic.Model.*;

public class MainPanel extends JPanel {
    private Controller controller;
    private DisplayPanel dispPanel;
    private InputPanel inputPanel;

    private JButton addButton, searchButton, deleteButton, updateButton;
    private JComboBox<String> filters;

    public MainPanel(Controller controller, DisplayPanel dispPanel, InputPanel inputPanel) {
        this.controller = controller;
        this.dispPanel = dispPanel;
        this.inputPanel = inputPanel;

        addButton = new JButton("Add");
        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete ID");
        updateButton = new JButton("Update ID");
        filters = new JComboBox<>(new String[] {
            FilterType.ALL, FilterType.STUDENTS, FilterType.FACULTY, 
            FilterType.SORT_NAME, FilterType.SORT_GPA, FilterType.SORT_SALARY});


        // ADD
        addButton.addActionListener(e -> {
            try {
                String name = this.inputPanel.getNameText();
                String id = this.inputPanel.getIdText();
                if(id != null) id = id.toUpperCase();
                String type = this.inputPanel.getType();

                if(type.equalsIgnoreCase("Student")) {
                    double gpa = Double.parseDouble(this.inputPanel.getGpaSalaryText());
                    String major = this.inputPanel.getMajorDeptText();

                    boolean added;
                    if(!this.inputPanel.getCoursesText().isEmpty()) {
                        String[] rawCourses = this.inputPanel.getCoursesText().split(",");
                        StringBuilder courses = new StringBuilder();

                        for(String course : rawCourses) {
                            String c = course.trim();
                            if(!c.isEmpty()) {
                                if(courses.length() > 0) courses.append(";");
                                courses.append(c);
                            }
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
            String input = JOptionPane.showInputDialog("Enter ID or Name to Search:");

            if(input == null || input.trim().isEmpty()) {
                return;
            }

            List<Person> results = this.controller.searchPerson(input.trim());

            if(results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No records found.");
            } 
            else if(results.size() == 1) {
                dispPanel.showSingle(results.get(0));
            } 
            else {
                StringBuilder sb = new StringBuilder();
                sb.append("--- SEARCH RESULTS ---\n\n");

                for(Person p : results) {
                    sb.append(p.getDetails()).append("\n");
                    sb.append("--------------------------------\n");
                }

                dispPanel.dispText(sb.toString());
            }
        });

        // DELETE
        deleteButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter ID to delete:");
            if(id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID cannot be empty.");
                return;
            }
            id = id.trim().toUpperCase();
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
            if(id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID cannot be empty.");
                return;
            }

            id = id.trim().toUpperCase();
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
                    s.clearCourses();;

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
                            String newId = newValue.trim().toUpperCase();
                            if(!newId.equalsIgnoreCase(currentId) && controller.findPerson(newId) != null) {
                                JOptionPane.showMessageDialog(this, "Error: This new ID is already taken");
                                continue;
                            }
                            controller.updateId(currentId, newId);
                            currentId = newId; 
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

        filters.addActionListener(e -> {

            String choice = (String) filters.getSelectedItem();
            List<Person> list = controller.getFilterList(choice);
        
            StringBuilder sb = new StringBuilder();
        
            if(list.isEmpty()) {
                sb.append("No records found.");
            } 
            else {
                sb.append("--- FILTER RESULTS ---\n\n");
                for(Person p : list) {
                    sb.append(p.getDetails());
                    sb.append("\n--------------------------------\n");
                }
            }
        
            dispPanel.dispText(sb.toString());
        });

        add(addButton); 
        add(updateButton);
        add(searchButton); 
        add(deleteButton);
        add(new JLabel("Filter: "));
        add(filters);
    }
}
