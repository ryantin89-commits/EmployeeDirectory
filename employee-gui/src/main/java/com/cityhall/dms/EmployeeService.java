/**Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 20, 2025
 * com.cityhall.dms.EmployeeService.java
 * This class handles all the logic behind the scenes for the employee system.
 * It connects the main program with the com.cityhall.dms.EmployeeRepository and makes sure all input
 * is valid before anything gets saved or changed.
 */

package com.cityhall.dms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    //The below creates an instance of our com.cityhall.dms.EmployeeRepository so we can store and manage employees
    @Autowired
    private EmployeeRepository repo;

    //The below method adds a new employee after validating their information
    public Employee addEmployee(Employee employee) {
        if (isValidEmployee(employee)) {
            return repo.save(employee);
        } else {
            System.out.println("Error: Invalid employee information.");
            return null;
        }
    }

    //The below method returns all employees from the repository
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    //The below method retrieves one employee by their ID number
    public Employee getEmployeeById(int id) {
        Optional<Employee> emp = repo.findById(id);
        return emp.orElse(null);
    }

    //The below updates an employee if their information is valid and they exist
    public Employee updateEmployee(Employee employee) {
        if (employee.getId() == null || !repo.existsById(employee.getId())) {
            System.out.println("Error: Employee not found for update.");
            return null;
            }
            if (isValidEmployee(employee)) {
                return repo.save(employee);
            }
            System.out.println("Error: Invalid employee information.");
            return null;
        }

    //The below deletes an employee by ID number
    public boolean deleteEmployee(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        System.out.println("Error: Employee not found for deletion.");
        return false;
    }

    //The below marks an employee as inactive (soft delete)
    public boolean deactivateEmployee(int id) {
        Employee emp = getEmployeeById(id);
        if (emp == null) return false;
        emp.setActive(false);
        repo.save(emp);
        return true;
    }

    //The below brings an inactive employee back to active
    public boolean reactivateEmployee(int id) {
        Employee emp = getEmployeeById(id);
        if (emp == null) return false;
            emp.setActive(true);
            repo.save(emp);
            return true;
    }

    //The below method checks if employee data is valid (like no blank fields)
    private boolean isValidEmployee(Employee employee) {
        if (employee == null) return false;

        boolean namesOk =
                employee.getFirstName() != null && !employee.getFirstName().isBlank() &&
                employee.getLastName() != null && !employee.getLastName().isBlank();

        boolean deptOk =
                employee.getDepartment() != null && !employee.getDepartment().isBlank();

        boolean emailOk =
                validateEmailFormat(employee.getEmail());

        return namesOk && deptOk && emailOk;
    }

    //Search employees by name, department, or email
    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll(); //If nothing typed, show everyone
        }
        return repo.searchEmployees(keyword);
    }

    //The below method is an email validation that checks the email data is valid (looks like a regular email)
    public boolean validateEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            System.out.println("Error: Email cannot be blank.");
            return false;
        }

        //Basic pattern for email: text@text.domain
        boolean isValid = email.matches(".+@.+\\..+");

        if (!isValid) {
            System.out.println("Error: Invalid email format. Please use something like name@domain.com");
        }
        return isValid;
    }

    //The below counts employees per department.  This is used by the custom action and testing
    public java.util.Map<String, Integer> countByDepartment() {
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();

        for (Employee e : getAllEmployees()) {
            String dept = (e.getDepartment() == null || e.getDepartment().isBlank())
                    ? "(Unknown)"
                    : e.getDepartment().trim();
            counts.put(dept, counts.getOrDefault(dept, 0) + 1);
        }
        return counts;
    }
}