/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 17, 2025
 * com.cityhall.dms.EmployeeService.java
 *
 * This service class contains the business logic for the Employee Directory
 * Management System. It acts as the middle layer between the controller and
 * the repository, validating data and ensuring that all operations are handled
 * safely before interacting with the database.
 */

package com.cityhall.dms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for managing employees, validating input,
 * and coordinating operations with the EmployeeRepository.
 */
@Service
public class EmployeeService {

    /**
     * The repository used to perform CRUD operations on Employee entities.
     */
    @Autowired
    private EmployeeRepository repo;

    /**
     * Adds a new employee to the system after validating their information.
     *
     * @param employee the employee to add
     * @return the saved Employee object, or null if validation fails
     */
    public Employee addEmployee(Employee employee) {
        if (isValidEmployee(employee)) {
            return repo.save(employee);
        } else {
            System.out.println("Error: Invalid employee information.");
            return null;
        }
    }

    /**
     * Retrieves all employees stored in the repository.
     *
     * @return a list of all employees
     */
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    /**
     * Retrieves a single employee by their ID.
     *
     * @param id the employee's ID
     * @return the Employee object, or null if not found
     */
    public Employee getEmployeeById(int id) {
        Optional<Employee> emp = repo.findById(id);
        return emp.orElse(null);
    }

    /**
     * Updates an existing employee if their ID exists and the data is valid.
     *
     * @param employee the updated employee information
     * @return the saved Employee object, or null if validation fails or the ID does not exist
     */
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

    /**
     * Deletes an employee by ID.
     *
     * @param id the ID of the employee to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteEmployee(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        System.out.println("Error: Employee not found for deletion.");
        return false;
    }

    /**
     * Marks an employee as inactive (soft delete).
     *
     * @param id the ID of the employee to deactivate
     * @return true if the employee was deactivated, false if not found
     */
    public boolean deactivateEmployee(int id) {
        Employee emp = getEmployeeById(id);
        if (emp == null) return false;
        emp.setActive(false);
        repo.save(emp);
        return true;
    }

    /**
     * Marks an employee as active again.
     *
     * @param id the ID of the employee to reactivate
     * @return true if the employee was reactivated, false if not found
     */
    public boolean reactivateEmployee(int id) {
        Employee emp = getEmployeeById(id);
        if (emp == null) return false;
        emp.setActive(true);
        repo.save(emp);
        return true;
    }

    /**
     * Validates employee data, ensuring required fields are not blank.
     *
     * @param employee the employee to validate
     * @return true if the employee is valid, false otherwise
     */
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

    /**
     * Searches employees by name, department, email, or ID.
     *
     * @param keyword the text to search for
     * @return a filtered list of employees matching the keyword
     */
    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll(); //If nothing typed, show everyone
        }
        return repo.searchEmployees(keyword);
    }

    /**
     * Validates whether the given string is in a basic email format.
     *
     * @param email the email address to validate the correct format
     * @return true if the email format is valid, false otherwise
     */
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

    /**
     * Counts how many employees belong to each department.
     * Used by the custom action and for testing.
     *
     * @return a map where keys are department names and values are employee counts
     */
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