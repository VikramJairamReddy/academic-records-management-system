/**
 * This is a subclass that extends the Person class and represents a student in the management system.
 * It includes attributes for students like
 *  major, GPA, and enrolled courses, along with methods to manage these attributes.
 *
 * @author Ganta Vikram Jairam Reddy
 **/

package logic;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String major;
    private double gpa;
    private List<String> enrolledCourses;

    // Constructor to initialize a student with basic details and an empty course list
    public Student(String name, String id, double gpa, String major) {
        super(name, id);
        setMajor(major);
        setGpa(gpa);
        enrolledCourses = new ArrayList<>();
    }

    // another constructor to handle courses input as a semicolon-separated string
    public Student(String name, String id, double gpa, String major, String courses) {
        super(name, id);
        setMajor(major);
        setGpa(gpa);
        enrolledCourses = new ArrayList<>();
        
        if (courses != null && !courses.equalsIgnoreCase("none") && !courses.trim().isEmpty()) {
            for (String course : courses.split(";")) {
                addCourses(course.trim());
            }
        }
    }

    // Setters with validation to ensure valid input for major and GPA
    public void setMajor(String major) {
        if(major == null || major.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a valid Major");
        }
        
        this.major = major; 
    }

    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        
        this.gpa = gpa; 
    }

    // Method to add a course to the enrolled courses list with validation
    public void addCourses(String course) {
        if(course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a valid course");
        }
        enrolledCourses.add(course);
    }

    // Method to remove a course from the enrolled courses list with validation
    public void removeCourses(String course) {
        if(course == null || course.trim().isEmpty() || !enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Enter a valid course");
        }
        enrolledCourses.remove(course);
    }

    // Getters for major, GPA, and enrolled courses
    public String getMajor() {
        return major;
    }

    public double getGpa() {
        return gpa;
    }

    public List<String> getCourses() {
        return enrolledCourses;
    }

    // Override's the getDetails method to provide a formatted string representation 
    // of the student's details
    @Override
    public String getDetails(){
        return "Student | Name: " + getName() + 
        "| ID: " + getId() + 
        " | Major: " + getMajor() + 
        " | GPA: " + getGpa() +
        " | Courses: " + String.join(", ", enrolledCourses);
    }
}
