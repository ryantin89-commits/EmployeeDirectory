import java.util.ArrayList;
import java.util.List;

/**Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 13, 2025
 * EmployeeRepository.java
 * This class basically acts like a fake database for our Employee objects.
 * It stores them in a list and lets us do CRUD (create, Read, Updated, Delete).
 * For Phase 1, everything happens in memory - no database yet.
 */

public class EmployeeRepository {

    //The below is to keep all our employees in a list
    private List<Employee> employees = new ArrayList<>();

    //The below will help us assign a unique ID to every new employee
    private int nextID = 1;

    //The below acts to automatically give an employee ID when a new employee is added to the database
    public void addEmployee(Employee employee) {
        employee.setId(nextID++);
        employees.add(employee);
    }

    //The below returns all employees
    public List<Employee> getAllEmployees() {
        return employees;
    }

    //The below find an employee by ID.  If it doesn't exist, returns null.
    public Employee getEmployee(int id) {
        for (Employee ee : employees) {
            if (ee.getId() == id) {
                return ee;
            }
        }
        return null;
    }

    //The below updates an employee by matching their ID.  Returns true if successful, false if the employee doesn't exist.
    public boolean updateEmployee(Employee updatedEmployee) {
        for (Employee ee : employees) {
            if (ee.getId() == updatedEmployee.getId()) {
                ee.setFirstName(updatedEmployee.getFirstName());
                ee.setLastName(updatedEmployee.getLastName());
                ee.setEmail(updatedEmployee.getEmail());
                ee.setPhone(updatedEmployee.getPhone());
                ee.setDepartment(updatedEmployee.getDepartment());
                ee.setPhone(updatedEmployee.getPhone());
                ee.setOfficeLocation(updatedEmployee.getOfficeLocation());
                ee.setHireDate(updatedEmployee.getHireDate());
                return true;
            }
        }
        return false;
    }

    //The below deletes an employee by ID.  Returns true if deleted, false if not found.
    public boolean deleteEmployee(int id) {
        Employee toRemove = null;
        for (Employee ee : employees) {
            if (ee.getId() == id) {
                toRemove = ee;
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
        for (Employee ee : employees) {
            if (ee.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                    ee.getLastName().toLowerCase().contains(keyword.toLowerCase()) ||
                    ee.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                    ee.getDepartment().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(ee);
            }
        }
        return results;
    }

    //The below clears all employees (used if we ever want to reload data).
    public void clearAllEmployees() {
        employees.clear();
        nextID = 1;
    }
}
