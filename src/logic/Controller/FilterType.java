/** 
 * Stores sorting options.
 *
 * Using constants avoids hard coded strings throughout the program and reduces
 * spelling or typing errors.
 * 
 * @author Ganta Vikram Jairam Reddy

**/
package logic.Controller;

public final class FilterType {

    private FilterType() { }

    public static final String ALL = "All";
    public static final String STUDENTS = "Students Only";
    public static final String FACULTY = "Faculty Only";
    public static final String SORT_NAME = "Sort by Name";
    public static final String SORT_GPA = "Sort by GPA";
    public static final String SORT_SALARY = "Sort by Salary";
}
