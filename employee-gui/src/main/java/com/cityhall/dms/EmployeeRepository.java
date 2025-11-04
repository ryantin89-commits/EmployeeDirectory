/** Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 1, 2025
 * com.cityhall.dms.EmployeeRepository.java
 * This repository uses Spring Data JPA to connect our Employee class to the SQLite database.
 * It automatically handles CRUD (Create, Read, Update, Delete) operations.
 * This version includes a custom query that allows searching by ID, name, email, or department.
 */

package com.cityhall.dms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

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
