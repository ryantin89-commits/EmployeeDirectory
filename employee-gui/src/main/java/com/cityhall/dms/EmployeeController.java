/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 27, 2025
 * com.cityhall.dms.EmployeeController.java
 *
 * This is the main controller for the Employee Directory Management System.
 * It handles all the web requests — like showing the employee list, adding,
 * editing, searching, and deleting employees. It connects the backend logic
 * (EmployeeService) with the front-end templates (Thymeleaf pages).
 *
 * Basically, this class is what lets users interact with the system through
 * the web browser; every button or form eventually runs through here.
 */

package com.cityhall.dms;

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

@Controller
public class EmployeeController {

    private final EmployeeService service = new EmployeeService();

    //Home Page: Displays the list of all employees
    @GetMapping("/")
    public String listEmployees(Model model) {
        model.addAttribute("employees", service.getAllEmployees());
        return "employees"; //Loads employees.html
    }

    //Search Employees (called when the search form is submitted)
    @GetMapping("/search")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        List<Employee> results = service.searchEmployees(keyword);
        model.addAttribute("employees", results);
        model.addAttribute("keyword", keyword);
        return "employees";
    }

    //Show Add Employee Form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("formTitle", "Add Employee");
        model.addAttribute("action", "/add"); //Tells the form where to submit
        return "employee-form";
    }

    //Process Add Employee form submission
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        service.addEmployee(employee);
        return "redirect:/"; //Goes back to main list
    }

    //Show Edit Form for a specific employee
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Employee existing = service.getEmployeeById(id);
        if (existing == null) return "redirect:/"; //Redirect if employee doesn’t exist
        model.addAttribute("employee", existing);
        model.addAttribute("formTitle", "Edit Employee");
        model.addAttribute("action", "/update");
        return "employee-form";
    }

    //Process Edit Employee form submission
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee) {
        employee.setActive(Boolean.parseBoolean(String.valueOf(employee.isActive())));
        service.updateEmployee(employee);
        return "redirect:/";
    }

    //Delete an employee by ID
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {
        service.deleteEmployee(id);
        return "redirect:/";
    }

    //Upload Employee Data from a .txt file
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

    //Helper method: Converts each line from the uploaded text file into an Employee object
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