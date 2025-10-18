import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 20, 2025
 * EmployeeRepository.java
 * This class basically acts like a database for our Employee objects.
 * It stores them in a list and lets us do CRUD (Create, Read, Update, Delete).
 * For Phase 1, everything happens in memory - no database yet.
 */

public class EmployeeRepository {

    //The below is to keep all our employees in a list
    private List<Employee> employees = new ArrayList<>();

    //The below will help us assign a unique ID to every new employee
    private int nextId = 1;

    //The below acts to automatically give an employee ID when a new employee is added to the database
    public boolean addEmployee(Employee e) {
        e.setId(nextId++);
        employees.add(e);
        return true;
    }

    //The below returns all employees
    public List<Employee> getAllEmployees() {
        return employees;
    }

    //The below is used to find an employee by ID.  If it doesn't exist, returns null.
    public Employee getEmployeeById(int id) {
        for (Employee e : employees) {
            if (e.getId() != null && e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    //The below updates an employee by matching their ID.  Returns true if successful, false if the employee doesn't exist.
    public boolean updateEmployee(Employee updatedEmployee) {
        for (Employee e : employees) {
            if (Objects.equals(e.getId(), updatedEmployee.getId())) {
                e.setFirstName(updatedEmployee.getFirstName());
                e.setLastName(updatedEmployee.getLastName());
                e.setEmail(updatedEmployee.getEmail());
                e.setDepartment(updatedEmployee.getDepartment());
                e.setPhone(updatedEmployee.getPhone());
                e.setOfficeLocation(updatedEmployee.getOfficeLocation());
                e.setHireDate(updatedEmployee.getHireDate());
                return true;
            }
        }
        return false;
    }

    //The below deletes an employee by ID.  Returns true if deleted, false if not found.
    public boolean deleteEmployee(int id) {
        Employee toRemove = null;
        for (Employee e : employees) {
            if (Objects.equals(e.getId(), id)) {
                toRemove = e;
                break;
            }
        }
        if (toRemove != null) {
            employees.remove(toRemove);
            return true;
        }
        return false;
    }

    //The below searches for employees that match a keyword (name, email, department).
    public List<Employee> searchEmployees (String keyword) {
        List<Employee> results = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                    e.getLastName().toLowerCase().contains(keyword.toLowerCase()) ||
                    e.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                    e.getDepartment().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(e);
            }
        }
        return results;
    }

    //The below clears all employees (used if we ever want to reload data).
    public void clearAllEmployees() {
        employees.clear();
        nextId = 1;
    }
}
