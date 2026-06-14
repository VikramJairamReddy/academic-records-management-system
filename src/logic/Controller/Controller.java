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

package logic.Controller;

import java.util.Map;

import logic.Model.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.*;

public class Controller {

    //Using LinkedHashMap for quick add, remove and look up and order
    private Map<String, Person> directory;
    private static final String FILE_NAME = "Data.txt";
    private Path filePath;

    public Controller() {
        directory = new LinkedHashMap<>();
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
        if(!directory.containsKey(p.getId())) {
            directory.put(p.getId(), p);
            return true;
        }
        return false;
    }

    /**
     * Deletes a Person from the directory using their ID
     * 
     * This method first searches for the person in the directory.
     * If present, the object is deleted from the directory
     * 
     * @param id unique indentification of the person to be deleted
     * @return true if person successfully found and deleted, false otherwise
     * */
    public boolean deletePerson(String id) {
        if(directory.containsKey(id)) {
            directory.remove(id);
            return true;
        }
        return false;
    }
    /**
     * Searches for a single person by ID
     * 
     * 
     * @param term ID to search
     * @return The matching Person object if found, otherwise null
     * */
    public Person findPerson(String id) {
        return directory.get(id);
    }

    /**
     * This method searchs for a single person is an ID is entered,
     * else if Name is entered to search, then it will return the List of all Persons with that name.
     * 
     * @param input ID or name to search
     * @return the matching Person's object if found.
     * */
    public List<Person> searchPerson(String input) {

        List<Person> results = new ArrayList<>();
    
        if(input == null || input.trim().isEmpty()) {
            return results;
        }
        input = input.trim();

        if(input.toUpperCase().matches("A\\d{8}")) {
            // normalize ID lookup to uppercase so IDs like 'a123...' still match stored keys
            String lookupId = input.toUpperCase();
            Person p = directory.get(lookupId);
            if(p != null) {
                results.add(p);
            }
            return results;
        } 
        
        input = input.toLowerCase();
        for(Person p : directory.values()) {
            if(p.getName().toLowerCase().contains(input)) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Updates the name of the Person
     * 
     * @param id ID to search for the Person
     * @param newName the name to be updated with
     * */
    public void updateName(String id, String newName) {
        Person p = directory.get(id);
        if(p == null) {
            throw new IllegalArgumentException("Person not found.");
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
        Person p = directory.get(oldId);

        if(p == null) {
            throw new IllegalArgumentException("Person with ID " + oldId + " not found.");
        }

        if(directory.containsKey(newId)){
            throw new IllegalArgumentException( "ID " + newId + " already exists.");
        }

        p.setId(newId);
        directory.remove(oldId);
        directory.put(newId, p);
    }

    /**
     * Updates the major of a Student
     * 
     * @param id the ID of the student to be updated
     * @param newMajor the new major to be updated with
     * */
    public void updateMajor(String id, String newMajor) {
        Person p = directory.get(id);
        
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
        Person p = directory.get(id);
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
        Person p = directory.get(id);
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
        Person p = directory.get(id);
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
        if(directory.isEmpty()) {
            return "No records found in the database.";
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("--- Current Academic Records ---\n\n");
    
        for(Person p : directory.values()) {
            sb.append(p.getDetails()).append("\n");
            sb.append("--------------------------------\n");
        }
    
        return sb.toString();
    }

    /**
     * Method to return all the Persons in the directory
     * 
     * @return the List of All Persons
     * */
    public List<Person> getAllPersons() {
        return new ArrayList<>(directory.values());
    }

    /**
     * Returns a filtered or sorted list of records.
     *
     * options for filters:
     * - Students Only
     * - Faculty Only
     * - Sort by Name
     * - Sort by GPA
     * - Sort by Salary
     *
     * @param type filter option selected
     * @return filtered list of Person objects
     */
    public List<Person> getFilterList(String type) {

        List<Person> list = new ArrayList<>();
    
        switch(type) {
    
            case FilterType.STUDENTS:
                for(Person p : directory.values()) {
                    if(p instanceof Student) {
                        list.add(p);
                    }
                }
                break;
    
            case FilterType.FACULTY:
                for(Person p : directory.values()) {
                    if(p instanceof Faculty) {
                        list.add(p);
                    }
                }
                break;

            case FilterType.SORT_NAME:
                list.addAll(directory.values());
                list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
    
            case FilterType.SORT_GPA:
                // include only students then sort by GPA descending
                for(Person p : directory.values()) {
                    if(p instanceof Student) {
                        list.add(p);
                    }
                }
                list.sort((a, b) -> Double.compare(((Student)b).getGpa(), ((Student)a).getGpa()));
                break;
    
            case FilterType.SORT_SALARY:
                // include only faculty then sort by salary descending
                for(Person p : directory.values()) {
                    if(p instanceof Faculty) {
                        list.add(p);
                    }
                }
                list.sort((a, b) -> Double.compare(((Faculty)b).getSalary(), ((Faculty)a).getSalary()));
                break;
    
            default:
                list.addAll(directory.values());
                break;
        }

        return list;
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
    
    /**
     * Saves all records currently stored in memory into Data.txt file.
     *
     * Student records are saved as: S,name,id,major,gpa,courses
     *
     * Faculty records are saved as: F,name,id,department,salary
     */
    public void saveFile() {

        try {

            List<String> lines = new ArrayList<>();
        
            for(Person p : directory.values()) {
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

    /**
     * Loads the records from Data.txt into memory.
     *
     * current records are cleared before loading.
     * Invalid GPA or Salary values and duplicate IDs are skipped.
     */
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

                if(line == null || line.trim().isEmpty()) {
                    continue;
                }
                
                if(data[0].equalsIgnoreCase("s") && data.length >= 6) {
                    String name = data[1];
                    String id = data[2];
                    String major = data[3];
                    double gpa;
                    try {
                        gpa = Double.parseDouble(data[4]);
                    }
                    catch(NumberFormatException ex) {
                        continue;
                    }

                    Student std = new Student(name, id, gpa, major);
                    String[] courses = data[5].split(";");
                    for(String course: courses) {
                        if(!course.equalsIgnoreCase("none")) {
                            std.addCourses(course);
                        }
                    }
                    if(directory.containsKey(id)) {
                        System.err.println("Skipping duplicate record: " + id);
                        continue;
                    }
                    directory.put(id, std);
                }

                else if(data[0].equalsIgnoreCase("f") && data.length >= 5) {
                    String name = data[1];
                    String id = data[2];
                    String department = data[3];
                    double salary;
                    try {
                        salary = Double.parseDouble(data[4]);
                    }
                    catch(NumberFormatException ex) {
                        continue;
                    }

                    if(directory.containsKey(id)) {
                        System.err.println("Skipping duplicate record: " + id);
                        continue;
                    }
                    directory.put(id, new Faculty(name, id, salary, department));
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Error loading DATA: " + e.getMessage());
        }
        catch(Exception ex) {
            throw new RuntimeException("Error: " + ex.getMessage());
        }
    }
}
