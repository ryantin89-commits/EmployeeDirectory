/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 17, 2025
 * com.cityhall.dms.EmployeeGuiApplication.java
 *
 * This is the main entry point for the Employee Directory Management System.
 * It starts the Spring Boot application and initializes component scanning,
 * entity scanning, and repository detection in the com.cityhall.dms package.
 */

package com.cityhall.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Main application class for launching the Spring Boot Employee Directory System.
 * The annotations below enable component scanning, JPA repository support,
 * and entity detection within the application package.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.cityhall.dms")
@EntityScan(basePackages = "com.cityhall.dms")
public class EmployeeGuiApplication {

    /**
     * The main method that launches the Spring Boot application.
     * It configures the Spring environment, starts the embedded server,
     * and loads all components, controllers, and services.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EmployeeGuiApplication.class, args);
    }
}