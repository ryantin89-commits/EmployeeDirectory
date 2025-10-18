# 🧑‍💼 Employee Directory – DMS Project

**Author:** Robert Yantin Jr.  
**Course:** CEN 3024 – Software Development I  
**Date:** October 2025

---

## 📝 Overview
This project is **Phase 1 of the Database Management System (DMS) Project**.  
I built a console-based **Employee Directory** program using **Java** in **IntelliJ IDEA**.

The main goal of this phase is to design a working in-memory system that allows basic employee management (CRUD) while handling input validation and file importing.  
Future phases will focus on connecting the system to a real database.

---

## ⚙️ Features
The Employee Directory app lets users:

- **Load employees from a text file** (`.txt`)
- **Display all employees** currently stored in memory
- **Add** new employees manually
- **Update/Edit** existing employee information
- **Delete** employees by ID
- **Search** for employees by name, email, or department
- **Run a custom action** that counts employees by department
- **Clear all** employees from memory (for testing)
- **Exit** safely from the menu without crashing

Each feature includes **input validation**, so the program won’t crash if someone types letters instead of numbers or enters bad data (like an invalid date format).

---

## 🧠 How It Works
- Employee data is stored in an **ArrayList** within an in-memory repository (`EmployeeRepository.java`).
- The program runs entirely in memory — **no external database yet**.
- A text-based menu (`EmployeeApp.java`) handles user interaction.
- `EmployeeService.java` validates and manages logic between the user and repository.
- The program loads data from a simple `.txt` file with this format:
FirstName,LastName,Email,Department,Phone,OfficeLocation,HireDate

