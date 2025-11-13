/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 17, 2025
 * com.cityhall.dms.Employee.java
 *
 * This class represents one com.cityhall.dms.Employee record in the system.
 * It now includes JPA annotations to integrate with the SQLite database.
 */

package com.cityhall.dms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The below class represents a single city employee
 * in the directory, including contact information,
 * department, and employment status.
 */
@Entity
@Table(name = "employee")
public class Employee {

    /**
     * Every employee receives a unique ID number.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The employee's first name.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * The employee's last name.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * The employee's work email address.
     */
    @Column(nullable = false)
    private String email;

    /**
     * The department where the employee works.
     */
    @Column
    private String department;

    /**
     * The employee's phone number.
     */
    @Column
    private String phone;

    /**
     * The office location assigned to the employee.
     */
    @Column(name = "office_location")
    private String officeLocation;

    /**
     * Indicates whether the employee is active in the system.
     * Used for soft deletes (true = active, false = inactive).
     */
    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private boolean active = true;

    /**
     * The employee's hire date.
     * Stored as a LocalDate using the format yyyy-MM-dd.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "hire_date", columnDefinition = "TEXT")
    private LocalDate hireDate;

    /**
     * Default constructor that creates an empty Employee instance.
     */
    public Employee() {
    }

    /**
     * Full constructor used to create an Employee with all available details.
     *
     * @param id the unique identifier of the employee
     * @param firstName the employee's first name
     * @param lastName the employee's last name
     * @param email the employee's email address
     * @param department the department where the employee works
     * @param phone the employee's phone number
     * @param officeLocation the employee's assigned office location
     * @param hireDate the date the employee was hired     *
     */
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

    /**
     * Gets the employee's ID.
     *
     * @return the employee ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the employee's ID.
     *
     * @param id the ID to assign
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the employee's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the employee's first name.
     *
     * @param firstName the first name to assign
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the employee's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee's last name.
     *
     * @param lastName the last name to assign
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the employee's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the employee's email address.
     *
     * @param email the email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the employee's department.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the employee's department.
     *
     * @param department the department to assign
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the employee's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the employee's phone number.
     *
     * @param phone the phone number to assign
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the employee's office location.
     *
     * @return the office location
     */
    public String getOfficeLocation() {
        return officeLocation;
    }

    /**
     * Sets the employee's office location.
     *
     * @param officeLocation the office location to assign
     */
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    /**
     * Gets the employee's hire date.
     *
     * @return the hire date as a LocalDate
     */
    public LocalDate getHireDate() {
        return hireDate;
    }

    /**
     * Sets the employee's hire date.
     *
     * @param hireDate the hire date to assign
     */
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Checks whether the employee is currently active.
     *
     * @return true if the employee is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the employee is active.
     * This supports soft-delete functionality.
     *
     * @param active true if the employee should be marked active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Builds and returns the employee's full name.
     *
     * @return the full name in the "First Last" format
     */
    public String fullName() {
        return (firstName + " " + lastName).trim();
    }

    /**
     * Checks whether the given query matches the employee's
     * name, department, or email.  Used for simple search filtering.
     *
     * @param query the text to search for
     * @return true if the query matches one of the searchable fields
     */
    public boolean matchesQuery(String query) {
        if (query == null || query.isBlank()) return false;
        String q = query.toLowerCase();
        return (fullName().toLowerCase().contains(q)
                || (department != null && department.toLowerCase().contains(q))
                || (email != null && email.toLowerCase().contains(q)));
    }

    /**
     * Converts the employee's data to a formatted string.
     *
     * @return a formatted summary of the employee's information
     */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;
        return String.format(
                "com.cityhall.dms.Employee #%d\n" +
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

    /**
     * Compares this employee to another object to determine if they are equal.
     * Two employees are considered equal if they share the same ID.
     *
     * @param o the reference object with which to compare.
     * @return true if the objects represent the same employee
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee e = (Employee) o;
        return Objects.equals(id, e.id);
    }

    /**
     * Generates a hash code for the employee.
     * Employees with the same ID produce the same hash value.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}