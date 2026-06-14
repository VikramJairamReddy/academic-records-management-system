/**
 * This is an InputPanel class that extends JPanel to add new members
 * It contains text fields for name, ID, GPA/Salary, and Major/Department, 
 * as well as a combo box for selecting the type of member (Student or Faculty)
 *
 * @author Ganta Vikram Jairam Reddy
 **/

package logic.View;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {

    private JTextField nameField, idField, gpaField, majorField, coursesField;
    private JComboBox<String> typeBox;

    private JLabel gpaSalaryLabel;
    private JLabel majorDeptLabel;
    private JLabel courseLabel;

    public InputPanel() {
        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Registration Form"));

        add(new JLabel("Member Type:"));
        typeBox = new JComboBox<>(new String[]{"Student", "Faculty"});
        typeBox.addActionListener(e -> updateLabels());
        add(typeBox);


        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("OSU ID (A...):"));
        idField = new JTextField();
        add(idField);

        gpaSalaryLabel = new JLabel("GPA:");
        add(gpaSalaryLabel);
        gpaField = new JTextField();
        add(gpaField);

        majorDeptLabel = new JLabel("Major:");
        add(majorDeptLabel);
        majorField = new JTextField();
        add(majorField);

        courseLabel = new JLabel("Courses Enrolled:");
        add(courseLabel);
        coursesField = new JTextField();
        add(coursesField);

        gpaSalaryLabel.setText("GPA:");
        majorDeptLabel.setText("Major:");
        courseLabel.setVisible(true);
        coursesField.setEditable(true);
        coursesField.setEnabled(true);
    }

    private void updateLabels() {

        if(typeBox.getSelectedItem().equals("Student")) {
            gpaSalaryLabel.setText("GPA:");
            majorDeptLabel.setText("Major:");
            courseLabel.setVisible(true);
            coursesField.setVisible(true);
            coursesField.setEditable(true);
            coursesField.setEnabled(true);
        }
        else {
            gpaSalaryLabel.setText("Salary:");
            gpaField.setText("");
            majorDeptLabel.setText("Department:");
            courseLabel.setVisible(false);
            coursesField.setVisible(false);
            coursesField.setText("");
            coursesField.setEditable(false);
            coursesField.setEnabled(false);
        }
    }

    // Getter methods to get the text from the input fields
    public String getNameText() 
    { 
        return nameField.getText().trim(); 
    }
    public String getIdText() 
    { 
        return idField.getText().trim(); 
    }
    public String getGpaSalaryText() 
    { 
        return gpaField.getText().trim(); 
    }
    public String getMajorDeptText() 
    { 
        return majorField.getText().trim(); 
    }
    public String getType() 
    { 
        return (String) typeBox.getSelectedItem(); 
    }
    public String getCoursesText() 
    { 
        return coursesField.getText().trim();
    }
    
    // Method to clear the input fields after adding a new member
    public void clear() {
        nameField.setText(""); 
        idField.setText("");
        gpaField.setText(""); 
        majorField.setText("");
        coursesField.setText("");
    }
}
