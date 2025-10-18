import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 20, 2025
 * Employee.java
 * This class represents one Employee record in the system.  It just holds the data.
 * Each object has information like name, department, phone, etc.
 * The Service and Repository classes will actually interact with these objects.
 */

public class Employee {

    //Every employee receives a unique ID number
    private Integer id;

    //Basic information for the directory
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String phone;
    private String officeLocation;

    //Tracks if the employee is active in the system (soft delete)
    private boolean active = true;

    //Hire date is going to be used a LocalDate instead of a String
    private LocalDate hireDate;

    //Below is the default constructor (basically a blank employee)
    public Employee() {}

    //Below is the full constructor when we want to create an employee with all the details
    public Employee(Integer id, String firstName, String lastName, String email, String department, String phone, String
                    officeLocation, LocalDate hireDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.officeLocation = officeLocation;
        this.hireDate = hireDate;
    }

    //Below are my getters and setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getOfficeLocation() {
        return officeLocation;
    }
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    //The below are my helper methods
    //Just returns their full name so we don't repeat it everywhere
    public String fullName() {
        return (firstName + " " + lastName).trim();
    }

    //The below is used for searching (basically checks if the text matches any of these fields)
    public boolean matchesQuery(String query) {
        if (query == null || query.isBlank()) return false;
        String q = query.toLowerCase();
        return (fullName().toLowerCase().contains(q)
        || (department != null && department.toLowerCase().contains(q))
                || (email != null && email.toLowerCase().contains(q)));
    }

    //The below is how each employee will show up when we print them to the console.
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;
        return String.format(
                "Employee #%d\n" +
                        "Name: %s\n" +
                        "Department: %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n" +
                        "Office: %s\n" +
                        "Hire Date: %s\n",
                id,
                fullName(),
                department != null ? department : "N/A",
                email != null ? email : "N/A",
                phone != null ? phone : "N/A",
                officeLocation != null ? officeLocation : "N/A",
                hireDate != null ? hireDate.format(fmt) : "N/A");
    }

    //The below checks if two employees are the same person (same ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee e = (Employee) o;
        return Objects.equals(id, e.id);
    }

    //Adding the below so if two employees have the same ID, they should land in the same hash bucket
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
