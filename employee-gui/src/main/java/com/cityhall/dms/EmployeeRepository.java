/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 17, 2025
 * com.cityhall.dms.EmployeeRepository.java
 *
 * This repository interface connects the Employee entity to the SQLite database
 * using Spring Data JPA.  It provides built-in CRUD operations such as saving,
 * updating, deleting, and retrieving employees.
 *
 * This version also defines a custom query that searches employees by
 * ID, first name, last name, email, or department.
 */

package com.cityhall.dms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Employee entity operations.
 * Extends JPARepository to provide standard CRUD functionality and
 * includes a custom search query.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Searches for employees whose ID, first name, last name, email,
     * or department contains the given keyword.  The search is case-insensitive.
     *
     * @param keyword the text used to filter employee records
     * @return a list of employees whose fields match the keyword
     */
    @Query("""
                SELECT e FROM Employee e
                WHERE 
                CAST(e.id AS string) LIKE %:keyword%
                OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Employee> searchEmployees(@Param("keyword") String keyword);
}