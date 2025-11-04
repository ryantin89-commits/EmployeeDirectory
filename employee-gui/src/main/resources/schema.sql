--This is the shema for my table which defines the structure of the database

DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    department TEXT,
    phone TEXT,
    office_location TEXT,
    hire_date TEXT,
    active INTEGER DEFAULT 1
);