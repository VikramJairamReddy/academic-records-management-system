/**
 * This is a subclass that extends the Person class and represents a faculty member
 * It contains additional attributes such as department and salary
 * The class also overrides the getDetails method to provide a formatted string representation of the faculty member's details.
 * 
 * @author Ganta Vikram Jairam Reddy
 **/

package logic;

public class Faculty extends Person {
    private String department;
    private double salary;

    public Faculty(String name, String id, double salary, String department) {
        super(name, id);
        setDepartment(department);
        setSalary(salary);
    }

    // Setters with validation
    public void setDepartment(String department) {
        if(department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a valid department");
        }
        
        this.department = department; 
    }

    public void setSalary(double salary) {
        if(salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        
        this.salary = salary; 
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    // Override's the getDetails method to provide a formatted string 
    // representation of the faculty member
    @Override
    public String getDetails() {
        return "Faculty | Name: " + getName() + "| ID: " + getId() + " | Department: " + getDepartment() + 
        " | Salary: " + getSalary();
    }
}
