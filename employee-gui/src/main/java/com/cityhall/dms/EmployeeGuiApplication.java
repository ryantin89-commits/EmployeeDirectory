/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 27, 2025
 * com.cityhall.dms.EmployeeGuiApplication.java
 *
 * This is the main entry point for the Employee Directory Management System.
 * It’s the class that actually starts the Spring Boot application.
 * Once it runs, it automatically scans the com.cityhall.dms package
 * for any components, controllers, and configurations.
 */

package com.cityhall.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication//Marks this as a Spring Boot app and triggers component scanning
@EnableJpaRepositories(basePackages = "com.cityhall.dms")
@EntityScan(basePackages = "com.cityhall.dms")

public class EmployeeGuiApplication {

    public static void main(String[] args) {
        //This line runs the Spring Boot app it sets up the server and loads everything
        SpringApplication.run(EmployeeGuiApplication.class, args);
    }
}