/**
 * This is a Controller class.
 * Manages the interactions between the model and view components.
 * 
 * Purpose:
 * This acts as the control layer in the MVC architecture.
 * It manages the interaction between the Model(Person, Student, Faculty) and
 * the view (GUI).
 * 
 * Responsibilities:
 * - Maintain a directory of Person objects (either student or Faculty)
 * - Add, delete, and searches for a person in records
 * - Updates the Value
 * - Displays formatted records
 * - Saves data to a file
 * - Loads data from stored files
 * 
 * @author Ganta Vikram Jairam Reddy
**/

package logic;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;

public class Controller {

    private List<Person> directory;
    private static final String FILE_NAME = "Data.txt";
    private Path filePath;

    public Controller() {
        directory = new ArrayList<>();
        this.filePath = Paths.get(FILE_NAME);

        fileExists();
        loadData();
    }

    /**
     * Adds a new Person to the directory
     * 
     * Before adding it ensures no duplicate ID exists before adding.
     * 
     * @param p Person object which is to be added
     * @return true if successfully added, false if the ID already exists
     * */
    public boolean addPerson(Person p) {
        if(findPerson(p.getId()) == null) {
            directory.add(p);
            return true;
        }
        return false;
    }

    /**
     * Deletes a new Person from the directory using their ID
     * 
     * This method first searches for thr personin the directory.
     * If present, the object is deleted from the ArrayList
     * 
     * @param id unique indentification of the person to be deleted
     * @return true if person successfully found and deleted, false otherwise
     * */
    public boolean deletePerson(String id) {
        Person p = findPerson(id);
        if(p != null) {
            directory.remove(p);
            return true;
        }
        return false;
    }
    /**
     * Searches for a person by ID or name (cases are ignored or case-insensitive)
     * 
     * 
     * @param term ID or name to search
     * @return The matching Person object if found, otherwise null
     * */
    public Person findPerson(String term) {
        for(Person p : directory) {
            if(p.getId().equalsIgnoreCase(term) || p.getName().equalsIgnoreCase(term)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Updates the name of the Person
     * 
     * @param id ID to search for the Person
     * @param newName the name to be updated with
     * */
    public void updateName(String id, String newName) {
        Person p = findPerson(id);
        if(p == null) {
            throw new IllegalArgumentException(
                "Person not found."
            );
        }

        p.setName(newName);
    }

    /**
     * Updates the ID of the Person
     * 
     * @param oldId the current ID of the Person
     * @param newId the new ID to be updated with
     * */
    public void updateId(String oldId, String newId) {
        Person p = findPerson(oldId);

        if(p == null) {
            throw new IllegalArgumentException("Person with ID " + oldId + " not found.");
        }

        if(findPerson(newId) != null){
            throw new IllegalArgumentException( "ID " + newId + " already exists.");
        }

        p.setId(newId);
    }

    /**
     * Updates the major of a Student
     * 
     * @param id the ID of the student to be updated
     * @param newMajor the new major to be updated with
     * */
    public void updateMajor(String id, String newMajor) {
        Person p = findPerson(id);
        
        if(p instanceof Student) {
           ((Student) p).setMajor(newMajor);
        }
        else {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
    }

    /**
     * Updates the GPA of a Student
     * 
     * @param id the ID of the student to be updated
     * @param gpa the new GPA to be updated with
     * */
    public void updateGpa(String id, double gpa) {
        Person p = findPerson(id);
        if(p instanceof Student) {
            ((Student) p).setGpa(gpa);
        } 
        else {
            throw new IllegalArgumentException("Student ID not found.");
        }
    }

    /**
     * Updates the department of a Faculty
     * 
     * @param id the ID of the faculty to be updated
     * @param department the new department to be updated with
     * */
    public void updateDepartment(String id, String department) {
        Person p = findPerson(id);
        if(p instanceof Faculty) {
            ((Faculty) p).setDepartment(department);
        } 
        else {
            throw new IllegalArgumentException("Faculty ID " + id + " not found.");
        }
    }    

    /**
     * Updates the salary of a Faculty
     * 
     * @param id the ID of the faculty to be updated
     * @param newSalary the new salary to be updated with
     * */
    public void updateSalary(String id, double newSalary) {
        Person p = findPerson(id);
        if(p instanceof Faculty) {
            ((Faculty) p).setSalary(newSalary);
        } 
        else {
            throw new IllegalArgumentException("Faculty ID " + id + " not found.");
        }
    }

    /**
     * Lists all the details of the records in a formatted manner
     * 
     * @return A string containing the formatted details of all records
     * */
    public String listAllDetails() {
        if (directory.isEmpty()) {
            return "No records found in the database.";
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("--- Current Academic Records ---\n\n");
    
        for(Person p : directory) {
            sb.append(p.getDetails()).append("\n");
            sb.append("--------------------------------\n");
        }
    
        return sb.toString();
    }

    // A method to ensure the data file exists, if not it creates a new one
    private void fileExists() {
        try {
            if(!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } 
        catch(IOException e) {
            throw new RuntimeException("Error creating file: " + e.getMessage());
        }
    }
    
    // A method to save the current data of the directory to a file
    public void saveFile() {

        try {

            ArrayList<String> lines = new ArrayList<>();
        
            for(Person p : directory) {
                if(p instanceof Student) {
                    Student s = (Student) p;
                    String coursesData = String.join(";", s.getCourses());
                    if(coursesData.isEmpty()) {
                        coursesData = "None";
                    }
                    lines.add("S," + s.getName() + "," + s.getId() + "," + s.getMajor() + "," + s.getGpa() + "," + coursesData);
                } 
                else if(p instanceof Faculty) {
                    Faculty f = (Faculty) p;
                    lines.add("F," + f.getName() + "," + f.getId() + "," + f.getDepartment() + "," + f.getSalary());
                }
            }
            // writing the data to the file, if the file already exists it will be overwritten
            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch(IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }
    }

    // A method to load the data from the file to the directory when the program starts
    public void loadData() {

        // Clear the current directory to avoid duplicates when loading
        directory.clear();

        try {
            
            if(!Files.exists(filePath)) {
                throw new RuntimeException("Data file not found.");
            }

            List<String> lines = Files.readAllLines(filePath);

            for(String line : lines) {
                String[] data = line.split(",", -1);
                
                if(data[0].equalsIgnoreCase("s") && data.length >= 6) {
                    String name = data[1];
                    String id = data[2];
                    String major = data[3];
                    double gpa = Double.parseDouble(data[4]);

                    Student std = new Student(name, id, gpa, major);
                    String[] courses = data[5].split(";");
                    for(String course: courses) {
                        if(!course.equalsIgnoreCase("none")) {
                            std.addCourses(course);
                        }
                    }
                    directory.add(std);
                }

                else if(data[0].equalsIgnoreCase("f") && data.length >= 5) {
                    String name = data[1];
                    String id = data[2];
                    String department = data[3];
                    double salary = Double.parseDouble(data[4]);
                    directory.add(new Faculty(name, id, salary, department));
                }
            }
        }
        catch (IOException e){
            throw new RuntimeException("Error loading DATA: " + e.getMessage());
        }
    }
}
