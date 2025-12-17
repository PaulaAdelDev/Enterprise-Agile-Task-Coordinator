package mile1.Main;

import mile1.entity.*;
import mile1.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final TaskService taskService = new TaskService();
    private static final SprintService sprintService = new SprintService();

    public static void main(String[] args) {
        System.out.println("=== Enterprise Agile Task Coordinator ===");
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. List Users");
            System.out.println("4. List Tasks");
            System.out.println("5. Create Sprint");
            System.out.println("6. View Sprints");
            System.out.println("7. View Task Hierarchy");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerUser();
                case "2" -> loginUser();
                case "3" -> userService.listUsers();
                case "4" -> taskService.listAllTasks();
                case "5" -> createSprint();
                case "6" -> sprintService.viewAllSprints();
                case "7" -> taskService.viewHierarchy();
                case "0" -> exit = true;
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }

        System.out.println("Exiting system. Goodbye!");
    }

    // -------------------------------
    // User Registration & Login
    // -------------------------------
    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("❌ Error: Username cannot be empty!");
            return;
        }
        if (userService.getUser(username) != null) {
            System.out.println("❌ Error: Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("❌ Error: Password cannot be empty!");
            return;
        }

        System.out.println("Select role: 1. Stakeholder 2. ScrumMaster 3. Developer 4. QAEngineer");
        String roleChoice = scanner.nextLine().trim();
        User user;

        switch (roleChoice) {
            case "1" -> user = new Stakeholder(username, password);
            case "2" -> user = new ScrumMaster(username, password);
            case "3" -> user = new Developer(username, password);
            case "4" -> user = new QAEngineer(username, password);
            default -> {
                System.out.println("❌ Invalid role selection.");
                return;
            }
        }

        userService.addUser(user);
        System.out.println("✔ User registered successfully: " + username + " (" + user.getRole() + ")");
    }

    private static void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("❌ Error: Username cannot be empty!");
            return;
        }

        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("❌ User not found!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("❌ Error: Password cannot be empty!");
            return;
        }

        if (!user.checkPassword(password)) {
            System.out.println("❌ Incorrect password!");
            return;
        }

        System.out.println("✔ Login successful! Welcome " + username + " (" + user.getRole() + ")");
        showUserMenu(user);
    }

    private static void showUserMenu(User user) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n--- " + user.getRole() + " Menu ---");
            System.out.println("1. View Tasks");
            System.out.println("2. Create Task");
            System.out.println("3. Assign/Manage Task");
            System.out.println("4. Start/Complete Task");
            System.out.println("5. QA Test Task");
            System.out.println("6. Break Task into Subtask");
            System.out.println("7. Finalize Task");
            System.out.println("8. Hierarchy Management");
            System.out.println("9. Sprint Management");
            System.out.println("0. Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> user.viewTasks();
                case "2" -> createTask(user);
                case "3" -> manageTask(user);
                case "4" -> developerActions(user);
                case "5" -> qaActions(user);
                case "6" -> breakTaskAction(user);
                case "7" -> finalizeTaskAction(user);
                case "8" -> hierarchyManagement();
                case "9" -> sprintManagement();
                case "0" -> logout = true;
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // -------------------------------
    // Task Creation
    // -------------------------------
    private static void createTask(User user) {
        System.out.println("\n--- Create Task ---");

        if (user instanceof Stakeholder) {
            System.out.println("1. Epic  2. Story  3. Bug  4. StandardTask");
        } else if (user instanceof Developer) {
            System.out.println("1. Bug  2. StandardTask");
        } else {
            System.out.println("❌ You don't have permission to create tasks.");
            return;
        }

        String choice = scanner.nextLine().trim();
        System.out.print("Enter task title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("❌ Task title cannot be empty!");
            return;
        }

        if (user instanceof Stakeholder s) {
            switch (choice) {
                case "1" -> s.createEpic(new Epic(title));
                case "2" -> s.createStory(new Story(title));
                case "3" -> taskService.addTask(new Bug(title));
                case "4" -> taskService.addTask(new StandardTask(title));
                default -> System.out.println("❌ Invalid choice!");
            }
        } else if (user instanceof Developer) {
            switch (choice) {
                case "1" -> taskService.addTask(new Bug(title));
                case "2" -> taskService.addTask(new StandardTask(title));
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // -------------------------------
    // Task Assignment/Management
    // -------------------------------
    private static void manageTask(User user) {
        if (!(user instanceof ScrumMaster sm)) {
            System.out.println("❌ Only ScrumMaster can assign tasks.");
            return;
        }

        System.out.print("Enter task title: ");
        Task task = taskService.getTask(scanner.nextLine().trim());
        if (task == null) {
            System.out.println("❌ Task not found!");
            return;
        }

        System.out.print("Enter assignee username: ");
        User assignee = userService.getUser(scanner.nextLine().trim());
        if (assignee == null) {
            System.out.println("❌ User not found!");
            return;
        }

        sm.assignTask(task, assignee);
        System.out.println("✔ Task assigned successfully.");
    }

    // -------------------------------
    // Developer actions
    // -------------------------------
    private static void developerActions(User user) {
        if (!(user instanceof Developer dev)) {
            System.out.println("❌ Only Developer can perform this action.");
            return;
        }

        System.out.print("Enter task title: ");
        Task task = taskService.getTask(scanner.nextLine().trim());
        if (task == null) {
            System.out.println("❌ Task not found!");
            return;
        }

        System.out.println("1. Start Task  2. Complete Task");
        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) dev.startTask(task);
        else if (choice.equals("2")) dev.completeTask(task);
        else System.out.println("❌ Invalid choice!");
    }

    // -------------------------------
    // QA actions
    // -------------------------------
    private static void qaActions(User user) {
        if (!(user instanceof QAEngineer qa)) {
            System.out.println("❌ Only QA Engineer can perform this action.");
            return;
        }

        System.out.print("Enter task title: ");
        Task task = taskService.getTask(scanner.nextLine().trim());
        if (task == null) {
            System.out.println("❌ Task not found!");
            return;
        }

        System.out.println("Enter QA result: Approved / Rejected / Tested");
        String result = scanner.nextLine().trim();
        if (result.isEmpty()) {
            System.out.println("❌ QA result cannot be empty!");
            return;
        }

        qa.testTask(task, result);
    }

    // -------------------------------
    // Break Task into SubTask
    // -------------------------------
    private static void breakTaskAction(User user) {
        if (!(user instanceof ScrumMaster sm)) {
            System.out.println("❌ Only ScrumMaster can break tasks.");
            return;
        }

        System.out.print("Enter Story title: ");
        Task task = taskService.getTask(scanner.nextLine().trim());
        if (!(task instanceof Story story)) {
            System.out.println("❌ ERROR: Can only break Stories into SubTasks!");
            return;
        }

        System.out.print("Enter SubTask title: ");
        String subTitle = scanner.nextLine().trim();
        if (subTitle.isEmpty()) {
            System.out.println("❌ SubTask title cannot be empty!");
            return;
        }

        sm.breakTask(story, new SubTask(subTitle));
    }

    // -------------------------------
    // Finalize Task
    // -------------------------------
    private static void finalizeTaskAction(User user) {
        if (!(user instanceof ScrumMaster sm)) {
            System.out.println("❌ Only ScrumMaster can finalize tasks.");
            return;
        }

        System.out.print("Enter task title to finalize: ");
        Task task = taskService.getTask(scanner.nextLine().trim());
        if (task == null) {
            System.out.println("❌ Task not found!");
            return;
        }

        sm.finalizeTask(task);
        System.out.println("✔ Task finalized successfully.");
    }

    // -------------------------------
    // Hierarchy Management
    // -------------------------------
    private static void hierarchyManagement() {
        System.out.println("\n1. Link Story to Epic  2. Link SubTask to Story  3. View Task Hierarchy  4. Back");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                System.out.print("Epic title: ");
                String epicTitle = scanner.nextLine().trim();
                System.out.print("Story title: ");
                String storyTitle = scanner.nextLine().trim();
                taskService.addStoryToEpic(epicTitle, storyTitle);
            }
            case "2" -> {
                System.out.print("Story title: ");
                String storyTitle = scanner.nextLine().trim();
                System.out.print("SubTask title: ");
                String subTitle = scanner.nextLine().trim();
                taskService.addSubTaskToStory(storyTitle, subTitle);
            }
            case "3" -> taskService.viewHierarchy();
            case "4" -> {}
            default -> System.out.println("❌ Invalid choice!");
        }
    }

    // -------------------------------
    // Sprint Management
    // -------------------------------
    private static void sprintManagement() {
        System.out.println("\n1. Add Task  2. Add Epic with hierarchy  3. Add Story with hierarchy  4. View Sprint hierarchy  5. Back");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter Sprint objective: ");
        Sprint sprint = sprintService.getSprint(scanner.nextLine().trim());
        if (sprint == null) {
            System.out.println("❌ Sprint not found!");
            return;
        }

        switch (choice) {
            case "1" -> {
                System.out.print("Enter Task title: ");
                Task task = taskService.getTask(scanner.nextLine().trim());
                if (task != null) sprintService.addTaskToSprint(sprint, task);
                else System.out.println("❌ Task not found!");
            }
            case "2" -> {
                System.out.print("Enter Epic title: ");
                sprintService.addEpicToSprint(sprint, scanner.nextLine().trim());
            }
            case "3" -> {
                System.out.print("Enter Story title: ");
                sprintService.addStoryToSprint(sprint, scanner.nextLine().trim());
            }
            case "4" -> sprintService.viewSprintWithHierarchy(sprint);
            case "5" -> {}
            default -> System.out.println("❌ Invalid choice!");
        }
    }

    // -------------------------------
    // Create Sprint
    // -------------------------------
    private static void createSprint() {
        try {
            System.out.print("Enter sprint objective: ");
            String obj = scanner.nextLine().trim();
            System.out.print("Enter start date (dd-MM-yyyy): ");
            Date start = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine().trim());
            System.out.print("Enter end date (dd-MM-yyyy): ");
            Date end = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine().trim());
            sprintService.createSprint(obj, start, end);
            System.out.println("✔ Sprint created successfully!");
        } catch (Exception e) {
            System.out.println("❌ Invalid date format!");
        }
    }
}
