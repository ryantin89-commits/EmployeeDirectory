import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * October 13, 2025
 * EmployeeApp.java
 * This is the main entry point for Phase 1 (logic and input validation).
 * It's a simple, menu based console app where I can add/update/delete/search employees in my Employee
 * Directory DMS.
 * Everything runs in memory at this point, no database until a future phase.
 */

public class EmployeeApp {

    //Service = The "brain" that talks to the memory repository
    private final EmployeeService service = new EmployeeService();

    //One scanner for the whole app to avoid weird newline issues
    private final Scanner scanner = new Scanner(System.in);

    //Date format to keep all the dates looking the same (same format)
    private final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        new EmployeeApp().run();
    }

    //The below is the main loop that shows the menu until the user decides to Exit.
    private void run() {
        boolean running = true;
        while (running) {
            printMenu(); //Shows the menu each time
            int choice = readInt("Choose an option (1 - 11): ");
            switch (choice) {
                case 1: loadFromTxt(); break;
                case 2: displayAll(); break;
                case 3: addEmployee(); break;
                case 4: updateEmployee(); break;
                case 5: deleteEmployee(); break;
                case 6: searchEmployee(); break;
                case 7: customActionCountByDepartment(); break;
                case 8: deactivateEmployee(); break;
                case 9: reactivateEmployee(); break;
                case 10: clearAll(); break;
                case 11:
                    System.out.println("Exiting Employee Directory...Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid menu option.  Please choose 1 - 9");
                    break;
            }
            System.out.println(); //Adding a blank line for spacing
        }
    }

    //The below is the menu and helpers
    private void printMenu() {
        System.out.println("Employee Directory Management System - Phase 1");
        System.out.println("1) Load Employee from TXT file (Read Data)");
        System.out.println("2) Display All Employees");
        System.out.println("3) Add Employee     (Create)");
        System.out.println("4) Update Employee  (Update)");
        System.out.println("5) Delete Employee  (Delete)");
        System.out.println("6) Search Employee  (Name/Email/Department)");
        System.out.println("7) Custom: Count Employees by Department");
        System.out.println("8) De-Activate Employee (soft Delete)");
        System.out.println("9) Re-Activate Employee");
        System.out.println("10) Clear all Employees");
        System.out.println("11) Exit");
    }

    //The below keeps the app from crashing if someone types letters instead of numbers
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw  = scanner.nextLine();
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException nfe) {
                System.out.println(prompt + " is not a valid number.  Please type a valid number.");
            }
        }
    }

    //The below forces the user to actually type something.  No blank inputs.
    private String promptNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println(prompt + " this field can't be empty.");
        }
    }

    //The below makes sure the dates are in the right format
    @SuppressWarnings("SameParameterValue")
    private LocalDate promptDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String raw = scanner.nextLine().trim();
            try {
                return LocalDate.parse(raw, DATE_FMT);
            } catch (DateTimeParseException dte) {
                System.out.println(prompt + " is not a valid date, try again.");
            }
        }
    }

    //The below are the features
    //(1)The below is for the load employees from text file
    //Format per line: FirstName, LastName, Email, Department, Phone, OfficeLocation, Hire Date
    private void loadFromTxt() {
        String path = promptNonEmpty("Enter TXT file path: ");
        int loaded = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            //The below reads each line one at a time
            while ((line = br.readLine()) !=null) {
                Employee e = parseEmployeeTxt(line);
                //only add if we successfully created a valid employee
                if (e != null && service.addEmployee(e)) {
                    loaded++;
                }
            }

            //Once we're done, show how many employees were added
            System.out.println("Loaded " + loaded + " employees from text file.");

        } catch (IOException ioe) {
            //The below runs if the file path is wrong or can't be opened
            System.out.println("Could not read the text file: " + ioe.getMessage());
        }
    }

    //The below turns one line of text into an Employee object
    private Employee parseEmployeeTxt(String line) {
        //each field is separated by a comma
        String[] parts = line.split(",", -1);

        //To make sure that there are enough columns in the line
        if (parts.length < 7) {
            System.out.println("Skipped line: not enough fields at " + line);
            return null;
        }

        try {
            //Create a new employee and fill in the information from the text file (lines)
            Employee e = new Employee();
            e.setFirstName(parts[0].trim());
            e.setLastName(parts[1].trim());
            e.setEmail(parts[2].trim());
            e.setDepartment(parts[3].trim());
            e.setPhone(parts[4].trim());
            e.setOfficeLocation(parts[5].trim());
            e.setHireDate(LocalDate.parse(parts[6].trim(), DATE_FMT));
            return e;

        } catch (Exception ex) {
            //If something goes wrong (like a bad date format), skips that line
            System.out.println("Skipped line: bad data format at " + line);
            return null;
        }
    }

    //(2)The below displays all employees
    private void displayAll() {
        List<Employee> all = service.getAllEmployees();
        if (all.isEmpty()) {
            System.out.println("No employees yet.  Try loading a text file or adding one.");
            return;
        }
        System.out.println("\n--- Employee List (" + all.size() + ") ---");
        for (Employee e : all) {
            System.out.println(formatEmployee(e));
        }
    }

    //(3)The below adds a new employee (manually)
    private void addEmployee() {
        System.out.println("Add New Employee");
        Employee e = new Employee();

        e.setFirstName(promptNonEmpty("First Name: "));
        e.setLastName(promptNonEmpty("Last Name: "));

        //The below loops keeps prompting the user until a valid email is entered.
        String email;
        do {
            email = promptNonEmpty("Email: ");
        } while (!service.validateEmailFormat(email));
        e.setEmail(email);

        e.setDepartment(promptNonEmpty("Department: "));
        e.setPhone(promptNonEmpty("Phone: "));
        e.setOfficeLocation(promptNonEmpty("Office Location: "));
        e.setHireDate(promptDate("Hire Date: "));

        boolean ok = service.addEmployee(e);
        System.out.println(ok ? "New Employee Added!" : "Not added (validation failed).");
    }

    //(4)The below updates an existing employee
    private void updateEmployee() {
        int id = readInt("Enter Employee ID to update: ");
        Employee existing = service.getEmployeeById(id);
        if (existing == null) {
            System.out.println("Employee with ID " + id + " not found.");
            return;
        }

        System.out.println("Editing: " + formatEmployee(existing));

        String first = promptKeep("First name", existing.getFirstName());
        String last = promptKeep("Last name", existing.getLastName());
        String email = promptKeep("Email", existing.getEmail());
        String department = promptKeep("Department", existing.getDepartment());
        String phone = promptKeep("Phone", existing.getPhone());
        String office = promptKeep("Office Location", existing.getOfficeLocation());
        LocalDate hire = promptKeepDate("Hire Date", existing.getHireDate());

        Employee updated = new Employee();
        updated.setId(existing.getId()); //Keeps the same ID
        updated.setFirstName(first);
        updated.setLastName(last);
        updated.setEmail(email);
        updated.setDepartment(department);
        updated.setPhone(phone);
        updated.setOfficeLocation(office);
        updated.setHireDate(hire);

        boolean ok = service.updateEmployee(updated);
        System.out.println(ok ? "Updated!" : "Not updated (validation failed).");
    }

    //Lets the user edit a field but keep the old value by pressing Enter
    private String promptKeep(String label, String current) {
        System.out.print(label + "[" + current + "] (Enter to keep, type to update): ");
        String raw = scanner.nextLine();
        return raw.isBlank() ? current : raw.trim();
    }

    //Same as the promptKeep but for date.  Let the user edit the field but keeps the old value by pressing Enter
    @SuppressWarnings("SameParameterValue")
    private LocalDate promptKeepDate(String label, LocalDate current) {
        System.out.print(label + "[" + current + "] (Enter to keep, type to update): ");
        String raw = scanner.nextLine().trim();
        if (raw.isBlank()) return current;
        try {
            return LocalDate.parse(raw, DATE_FMT);
        } catch (DateTimeParseException dte) {
            System.out.println("Invalid date.  Keeping old one.");
            return current;
        }
    }

    //(5)The below deletes employees by ID
    private void deleteEmployee() {
        int id = readInt("Enter Employee ID to delete: ");
        Employee existing = service.getEmployeeById(id);
        if (existing == null) {
            System.out.println("Employee with ID " + id + " not found.");
            return;
        }

        System.out.println("You are about to delete: " + formatEmployee(existing));
        if (confirm("Are you sure? (y/n): ")) {
            boolean ok = service.deleteEmployee(id);
            System.out.println(ok ? "Deleted!" : "Not deleted (validation failed).");
        } else {
            System.out.println("Cancelled.");
        }
    }

    //Simple yes/no confirmation (used when deleting or clearing)
    private boolean confirm (String prompt) {
        System.out.print(prompt);
        String raw = scanner.nextLine().trim().toLowerCase();
        return raw.startsWith("y");
    }

    //(6)The below searches employees by keyword (name/email/department)
    private void searchEmployee() {
        String keyword = promptNonEmpty("Enter search keyword (Name/Email/Department): ");
        List<Employee> hits = service.searchEmployees(keyword);
        if (hits.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("Search Results " + hits.size() + " employees.");
        for (Employee e : hits) {
            System.out.println(formatEmployee(e));
        }
    }

    //(7)Custom action: count employees by department
    private void customActionCountByDepartment() {
        List<Employee> all = service.getAllEmployees();
        if (all.isEmpty()) {
            System.out.println("No employees found to count.");
            return;
        }

        //Count employees per department
        Map<String, Integer> counts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Employee e : all) {
            String dept = (e.getDepartment() == null || e.getDepartment().isBlank())
                ? "(Unknown)"
                : e.getDepartment().trim();
            counts.put(dept, counts.getOrDefault(dept, 0) + 1);
        }

        System.out.println("Employees by Department");
        counts.forEach((dept, count) -> System.out.println(dept + ": " + count));
    }

    //

    //(8)The below is for the soft delete: Mark as Inactive
    private void deactivateEmployee() {
        int id = readInt("Enter Employee ID to deactivate (0 to cancel): ");
        if (id <= 0) {
            System.out.println("Cancelled.");
            return;
        }

        Employee e =  service.getEmployeeById(id);
        if (e == null) {
            System.out.println("Employee with ID " + String.format("%04d", id) + " not found.");
            return;
        }

        System.out.println("About to deactivate: " + formatEmployee(e));
        if (!confirm("Are you sure? (y/n): ")) {
            System.out.println("Cancelled.");
            return;
        }

        boolean ok = service.deactivateEmployee(id);
        System.out.println(ok ? "Employee Deactivated!" : "Not deactivated (validation failed).");
    }

    //(9)The below brings back an inactive employee
    private void reactivateEmployee() {
        int id = readInt("Enter Employee ID to reactivate (0 to cancel): ");
        if (id <= 0) {
            System.out.println("Cancelled.");
            return;
        }

        Employee e =  service.getEmployeeById(id);
        if (e == null) {
            System.out.println("Employee with ID " + String.format("%04d", + id) + " not found.");
            return;
        }

        boolean ok = service.reactivateEmployee(id);
        System.out.println(ok ? "Employee reactivated!" : "Not reactivated (validation failed).");
    }


    //(10)The below clears everything from memory (created this for testing purposes)
    private void clearAll() {
        if(confirm("Clear all employees from memory? (y/n): ")) {
            service.clearAllEmployees();
            System.out.println("All employees have been cleared.");
        } else {
            System.out.println("Cancelled");
        }
    }

    private String formatEmployee(Employee e) {
        String status = e.isActive() ? "Active" : "Inactive";
        return String.format(
                "ID=%04d | %s %s | Email: %s | Department: %s | Phone Ext.: %s | Office Location: %s | Hire Date: %s | Status: %s",
                e.getId(),
                safe(e.getFirstName()),
                safe(e.getLastName()),
                safe(e.getEmail()),
                safe(e.getDepartment()),
                safe(e.getPhone()),
                safe(e.getOfficeLocation()),
                e.getHireDate() == null ? "N/A" : e.getHireDate().toString(),
                status
        );
    }

    private String safe(String s) {
        return s == null ? "N/A" : s;
    }
}