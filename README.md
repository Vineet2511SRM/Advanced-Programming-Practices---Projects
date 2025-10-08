# 💻 Advanced Programming Practices - Project

A collection of advanced Java-based projects built under the **Advanced Programming Practices** course at **SRM Institute of Science and Technology (SRMIST)**.  
These projects demonstrate **Object-Oriented Programming**, **GUI Design**, **Database Connectivity**, and **Algorithmic Logic** using **Java** and **MySQL**.

---

## 🧮 Project 1: CalculatorApp

### 🔹 Overview
A modern, minimalistic calculator GUI built using **Java Swing**, featuring a **custom stack-based expression evaluator**.  
It supports arithmetic operations, parentheses, decimals, and interactive button control for a smooth calculation experience.

### 🔹 Key Features
- Stack-based expression evaluation (`+`, `-`, `*`, `/`, and `()`)
- Real-time input display (all expressions visible at once)
- Backspace (`←`) and Clear (`C`) functionalities
- Clean, realistic layout using `GridLayout`
- Error handling for invalid expressions and divide-by-zero cases

### 🔹 Folder Structure
CalculatorApp/
│
├── src/
│ └── CalculatorApp.java


### 🔹 How to Run
bash
cd CalculatorApp/src
javac CalculatorApp.java
java CalculatorApp


🗄️ Project 2: StudentDBApp
🔹 Overview

A Java + MySQL based database project that performs CRUD (Create, Read, Update, Delete) operations on student data.
It demonstrates how to connect Java programs to MySQL databases using the JDBC API and MySQL Connector/J driver.

🔹 Key Features

Connects Java with MySQL seamlessly

Allows adding, updating, and deleting student records

Displays student data in a readable format

Uses prepared statements for secure SQL queries

Handles exceptions and database connectivity errors gracefully

🔹 Folder Structure
StudentDBApp/
│
├── src/
│   └── StudentDBApp.java
│
├── lib/
│   └── mysql-connector-j-9.4.0.jar
│
└── database/
    └── studentdb.sql

🔹 Database Setup

Open your MySQL terminal or Workbench.

Run the following commands:

CREATE DATABASE StudentDB;
USE StudentDB;


Import the SQL script file studentdb.sql inside the database/ folder to create the table:

SOURCE path/to/studentdb.sql;

🔹 MySQL Connector Setup

Ensure the MySQL Connector/J (mysql-connector-j-9.4.0.jar) is added to your classpath.

If you are compiling via terminal:

Windows (CMD):

cd StudentDBApp/src
javac -cp .;../lib/mysql-connector-j-9.4.0.jar StudentDBApp.java
java -cp .;../lib/mysql-connector-j-9.4.0.jar StudentDBApp


Mac/Linux (Terminal):

cd StudentDBApp/src
javac -cp .:../lib/mysql-connector-j-9.4.0.jar StudentDBApp.java
java -cp .:../lib/mysql-connector-j-9.4.0.jar StudentDBApp

🧠 Concepts Demonstrated
Concept	Description
Object-Oriented Programming	Core logic implemented using classes and methods
GUI Development	Implemented using Java Swing (CalculatorApp)
Event Handling	Button actions, input handling, and dynamic evaluation
JDBC Database Connectivity	Connection between Java and MySQL (StudentDBApp)
Algorithm Design	Custom infix expression evaluator (CalculatorApp)
Error Handling	Exception control in both applications
🛠️ Tools & Technologies

Java 8 or above

MySQL Server

MySQL Connector/J (v9.4.0)

Swing (AWT)

VS Code / IntelliJ IDEA / Eclipse

📂 Folder Overview
Advanced-Programming-Practices-Project/
│
├── CalculatorApp/
│   └── src/
│       └── CalculatorApp.java
│
├── StudentDBApp/
│   ├── src/
│   │   └── StudentDBApp.java
│   ├── lib/
│   │   └── mysql-connector-j-9.4.0.jar
│   └── database/
│       └── studentdb.sql
│
└── README.md

🧑‍💻 Author

Vineet Seth
🎓 B.Tech CSE (Cyber Security) @ SRMIST, Chennai
📍 From Prayagraj, Uttar Pradesh
💬 Passionate about full-stack development, cybersecurity, and creative problem-solving.


⭐ Support

If you found this project helpful or inspiring, please consider starring 🌟 the repository on GitHub!
It motivates and supports more open-source contributions.

© 2025 Vineet Seth | Advanced Programming Practices @ SRMIST


 name)?  
I can make it SEO-friendly and professional.
