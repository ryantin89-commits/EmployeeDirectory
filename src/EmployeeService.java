import java.util.List;

/**Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 13, 2025
 * EmployeeServicejava
 * This class handles all the logic behind the scenes for the employee system.
 * It connects the main program with the EmployeeRepository and makes sure all input
 * is valid before anything gets saved or changed.
 */

public class EmployeeService {

    //The below creates an instance of our EmployeeRepository so we can store and manage employees
    private EmployeeRepository repo =  new EmployeeRepository();

    //The below method adds a new employee after validating their information
    public boolean addEmployee(Employee employee) {
        if (isValidEmployee(employee)) {
            repo.addEmployee(employee);
            return true;
        } else {
            System.out.println("Error: Invalid employee information.");
            return false;
        }
    }

    //The below method returns all employees from the repository
    public List<Employee> getAllEmployees() {
        return repo.getAllEmployees();
    }

    //The below method retrieves one employee by their ID number
    public Employee getEmployeeById(int id) {
        return repo.getEmployeeById(id);
    }

    //The below updates an employee if their information is valid and they exist
    public boolean updateEmployee(Employee employee) {
        if (isValidEmployee(employee)) {
            boolean success = repo.updateEmployee(employee);
            if (!success) {
                System.out.println("Error: Employee not found for update.");
            }
            return success;
        } else {
            System.out.println("Error: Invalid employee information.");
            return false;
        }
    }

    //The below deletes an employee by ID number
    public boolean deleteEmployee(int id) {
        boolean success = repo.deleteEmployee(id);
        if (!success) {
            System.out.println("Error: Employee not found for deletion.");
        }
        return success;
    }

    //The below searches for employees that match a keyword (like a name or a department)
    public void clearAllEmployees() {
        repo.clearAllEmployees();
    }

    //The below method checks if employee data is valid (like no blank fields)
    private boolean isValidEmployee(Employee employee) {
        return employee.getFirstName() != null && !employee.getFirstName().isBlank() &&
                employee.getLastName() != null && !employee.getLastName().isBlank() &&
                employee.getEmail() != null && !employee.getEmail().isBlank() &&
                employee.getDepartment() != null && !employee.getDepartment().isBlank();
    }
}