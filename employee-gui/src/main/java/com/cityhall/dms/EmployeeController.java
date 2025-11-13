/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 17, 2025
 * com.cityhall.dms.EmployeeController.java
 *
 * This is the main controller for the Employee Directory Management System.
 * It handles all the web requests — like showing the employee list, adding,
 * editing, searching, and deleting employees. It connects the backend logic
 * (EmployeeService) with the front-end templates (Thymeleaf pages).
 *
 * This class allows users to interact with the system through the
 * web browser; nearly every button or form action routes through
 * this controller.
 */

package com.cityhall.dms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main controller responsible for handling all web routes related
 * to viewing, managing, and importing employee information.
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    /**
     * Displays the home page containing a list of all employees.
     *
     * @param model the model to pass data to the view
     * @return the employees.html template
     */
    @GetMapping("/")
    public String listEmployees(Model model) {
        model.addAttribute("employees", service.getAllEmployees());
        return "employees";
    }

    /**
     * Searches for employees by a given keyword.  Matches are based
     * on name, department, or email.
     *
     * @param keyword the text to search for
     * @param model the model used to pass search results to the view
     * @return the employees.html template with filtered results
     */
    @GetMapping("/search")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        List<Employee> results = service.searchEmployees(keyword);
        model.addAttribute("employees", results);
        model.addAttribute("keyword", keyword);
        return "employees";
    }

    /**
     * Shows the form for adding a new employee.
     *
     * @param model the model used to pass an empty Employee object to the form
     * @return the employee-form.html template
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("formTitle", "Add Employee");
        model.addAttribute("action", "/add");
        return "employee-form";
    }

    /**
     * Processes the submission of the "Add Employee" form.
     *
     * @param employee the new employee data submitted from the form
     * @return a redirect back to the home page
     */
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        service.addEmployee(employee);
        return "redirect:/";
    }

    /**
     * Displays the form for editing an existing employee.
     *
     * @param id the ID of the employee to edit
     * @param model the model used to pass employee data to the form
     * @return the employee-form.html template or a redirect if ID doesn't exist
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Employee existing = service.getEmployeeById(id);
        if (existing == null) return "redirect:/"; //Redirect if employee doesn’t exist
        model.addAttribute("employee", existing);
        model.addAttribute("formTitle", "Edit Employee");
        model.addAttribute("action", "/update");
        return "employee-form";
    }

    /**
     * Processes the submission of the "Edit Employee" form.
     *
     * @param employee the updated employee data
     * @return a redirect back to the home page
     */
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee) {
        employee.setActive(employee.isActive());
        service.updateEmployee(employee);
        return "redirect:/";
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete
     * @return a redirect back to the home page
     */
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {
        service.deleteEmployee(id);
        return "redirect:/";
    }

    /**
     * Imports employee data from a text file.  Each line in the file must be in
     * comma-separated format and will be converted into a new Employee record.
     *
     * @param file the uploaded text file containing employee data
     * @return a redirect back to the home page
     */
    @PostMapping("/upload")
    public String uploadEmployees(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return "redirect:/";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Employee e = parseEmployeeTxt(line);
                if (e != null) service.addEmployee(e);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "redirect:/";
    }

    /**
     * Converts a comma-separated text line into an Employee object.
     * Expected format:
     * firstName,lastName,email,department,phone,officeLocation,hireDate
     *
     * @param line one line of text from the uploaded file
     * @return an Employee object, or null if the format is invalid
     */
    private Employee parseEmployeeTxt(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 7) return null;

        Employee e = new Employee();
        e.setFirstName(parts[0].trim());
        e.setLastName(parts[1].trim());
        e.setEmail(parts[2].trim());
        e.setDepartment(parts[3].trim());
        e.setPhone(parts[4].trim());
        e.setOfficeLocation(parts[5].trim());
        e.setHireDate(LocalDate.parse(parts[6].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        e.setActive(true); //Default new employees to active
        return e;
    }
}