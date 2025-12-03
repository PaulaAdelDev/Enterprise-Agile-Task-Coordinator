📌 Enterprise Agile Task Coordinator
Java OOP – Ain Shams University – CSE241 – Fall 2025/2026
Milestone 1 Submission
📖 Project Overview

Enterprise Agile Task Coordinator is a Java-based object-oriented management system designed to organize projects, tasks, employees, and roles in an agile-friendly structure.
This project demonstrates core OOP concepts including:

Classes & Objects

Inheritance

Composition

Encapsulation

Polymorphism

Exception handling

Packages & modular design

This repository contains Milestone 1 implementation following the project description provided by the Faculty of Engineering, Ain Shams University.

🧩 Features Implemented (Milestone 1)
✔ System Entities

Project

Task

Employee

Role

Manager

Developer

Tester

Team

✔ OOP Structure

Clear class hierarchy

Inherited roles (Manager → Employee)

Composition (Projects contain tasks, tasks assigned to employees)

Data validation and custom error handling

UML-based class architecture

✔ Main Functionalities

Create Projects

Add employees

Assign roles

Add tasks to projects

Assign tasks to team members

Display status summaries

Store all data inside appropriate package structure

📂 Project Structure
src/
 └─ main/
     └─ java/
         └─ enterprise/
             ├─ entity/
             │   ├─ Employee.java
             │   ├─ Manager.java
             │   ├─ Developer.java
             │   ├─ Tester.java
             │   ├─ Role.java
             │   ├─ Task.java
             │   └─ Project.java
             │
             ├─ service/
             │   ├─ ProjectService.java
             │   ├─ TaskService.java
             │   ├─ EmployeeService.java
             │   └─ TeamService.java
             │
             ├─ database/
             │   └─ DataStore.java
             │
             └─ Main.java
