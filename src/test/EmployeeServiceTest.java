import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;

    //The below runs before every single test
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        service = new EmployeeService();
        service.clearAllEmployees(); //To start clean every test
    }

    //The below is a quick helper method to create a valid employee object that can be reused in multiple tests.
    private Employee sample() {
        Employee e = new Employee();
        e.setFirstName("Robert");
        e.setLastName("Yantin");
        e.setEmail("robert.yantin@cityhall.com");
        e.setDepartment("Human Resources");
        e.setPhone("8452");
        e.setOfficeLocation("HR-113");
        e.setHireDate(LocalDate.of(2024, 1, 16));
        return e;
    }

    //Add Employee Test
    //We add one person and make sure it's saved in the list.
    @org.junit.jupiter.api.Test
    void addEmployee_succeedsWithValidData() {
        assertTrue(service.addEmployee(sample()));
        assertEquals(1, service.getAllEmployees().size());
    }

    //Adding someone with a bad email should fail.
    //We test that the method rejects it and the list stays empty.
    @org.junit.jupiter.api.Test
    void addEmployee_failsWithInvalidEmail() {
        Employee bad = sample();
        bad.setEmail("Not an email");
        assertFalse(service.addEmployee(bad));
        assertEquals(0, service.getAllEmployees().size());
    }

    //Update Employee Test
    //Update an employee's department and check that it actually changes.
    @org.junit.jupiter.api.Test
    void updateEmployee_updatesFields() {
        Employee e = sample(); //sample() uses "Human Resources" as department
        assertTrue(service.addEmployee(e));

        Employee existing = service.getAllEmployees().get(0);
        int id = existing.getId();
        assertEquals("Human Resources", existing.getDepartment());

        existing.setDepartment("Parks & Recreation");
        assertTrue(service.updateEmployee(existing));

        assertEquals("Parks & Recreation", service.getEmployeeById(id).getDepartment());
    }

    //The below tries to update a non-existent employee and should fail.
    @org.junit.jupiter.api.Test
    void updateEmployee_returnsFalseWhenNotFound() {
        Employee ghost = sample();
        ghost.setId(9999);
        assertFalse(service.updateEmployee(ghost));
    }

    //Delete Employee Test (Hard Delete)
    //After deleting, we verify the employee is gone from the system.
    @org.junit.jupiter.api.Test
    void deleteEmployee_removesById() {
        assertTrue(service.addEmployee(sample()));
        int id = service.getAllEmployees().get(0).getId();

        assertTrue(service.deleteEmployee(id));
        assertNull(service.getEmployeeById(id));
        assertEquals(0, service.getAllEmployees().size());
    }

    //Below trying to delete someone who doesn't exist should fail
    @org.junit.jupiter.api.Test
    void deleteEmployee_returnsFalseWhenNotFound() {

        //No one added yet, so this ID 9999 shouldn't exist
        assertFalse(service.deleteEmployee(9999));
        assertEquals(0, service.getAllEmployees().size());
    }

    //Custom Action: Count by Department Test
    //The below checks that the countByDepartment() method works properly.
    @org.junit.jupiter.api.Test
    void countByDepartment_countsEmployeesCorrectly() {
        Employee e1 = sample();
        e1.setDepartment("Police");

        Employee e2 = sample();
        e2.setFirstName("Carlos");
        e2.setEmail("Carlos.Rodriguez@cityhall.com");
        e2.setDepartment("Police");

        Employee e3 = sample();
        e3.setFirstName("Amy");
        e3.setEmail("Amy.Smith@cityhall.com");
        e3.setDepartment("Finance");

        assertTrue(service.addEmployee(e1));
        assertTrue(service.addEmployee(e2));
        assertTrue(service.addEmployee(e3));

        Map<String, Integer> counts = service.countByDepartment();

        //Make sure "Police" exists in the result
        assertTrue(counts.containsKey("Police"), "Counts should include 'Police' key");

        //Police should have 2 people, Finance should have 1
        assertEquals(2,counts.get("Police"));
        assertEquals(1,counts.get("Finance"));

        //Should only have two departments total
        assertEquals(2, counts.size());
    }

    //Count with no employees.  The count map should be empty
    @org.junit.jupiter.api.Test
    void countByDepartment_returnsEmptyWhenNoEmployees() {
        var counts = service.countByDepartment();
        assertTrue(counts.isEmpty(), "If there are no employees, there should be any department counts");
    }
}