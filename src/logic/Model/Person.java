/**
 * This is an abstract class representing a person in the management system, 
 * it contains common attributes like name and OSU ID, along with validation logic.
 * It also defines an abstract method getDetails() that must be implemented by its subclasses.
 *
 * @author Ganta Vikram Jairam Reddy
 **/

package logic.Model;

public abstract class Person {
    private String name, id;

    public abstract String getDetails();
    
    public Person(String name, String id) {
        setName(name);
        setId(id);
    }

    // name setter with validation logic to ensure name is not empty
    public void setName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    // ID setter with validation logic to ensure ID is in the correct format
    public void setId(String id){
        if(id == null || !id.matches("A\\d{8}")) {
            throw new IllegalArgumentException("OSU ID must start with 'A' followed by 8 digits.");
        }
        this.id = id;
    }

    // Getters for name and ID
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
