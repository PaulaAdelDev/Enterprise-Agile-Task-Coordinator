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
            System.out.println("7. View Task Hierarchy"); // NEW
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> registerUser();
                case "2" -> loginUser();
                case "3" -> userService.listUsers();
                case "4" -> taskService.listAllTasks();
                case "5" -> createSprint();
                case "6" -> sprintService.viewAllSprints(); // FIXED: This method now exists
                case "7" -> viewTaskHierarchy(); // NEW
                case "0" -> exit = true;
                default -> System.out.println("Invalid choice! Try again.");
            }
        }

        System.out.println("Exiting system. Goodbye!");
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if(userService.getUser(username) != null) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Select role: 1. Stakeholder 2. ScrumMaster 3. Developer 4. QAEngineer");
        String roleChoice = scanner.nextLine();
        User user;
        switch (roleChoice) {
            case "1" -> user = new Stakeholder(username, password);
            case "2" -> user = new ScrumMaster(username, password);
            case "3" -> user = new Developer(username, password);
            case "4" -> user = new QAEngineer(username, password);
            default -> {
                System.out.println("Invalid role selection.");
                return;
            }
        }

        userService.addUser(user);
        System.out.println("User registered successfully: " + username + " (" + user.getRole() + ")");
    }

    private static void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!user.checkPassword(password)) {
            System.out.println("Incorrect password!");
            return;
        }

        System.out.println("Login successful! Welcome " + username + " (" + user.getRole() + ")");
        showUserMenu(user);
    }

    private static void showUserMenu(User user) {
        boolean logout = false;
        while(!logout) {
            System.out.println("\n--- " + user.getRole() + " Menu ---");
            System.out.println("1. View Tasks");
            System.out.println("2. Create Task");
            System.out.println("3. Assign Task / Manage Task");
            System.out.println("4. Start / Complete Task");
            System.out.println("5. QA Test Task");
            System.out.println("6. Break Task into Subtask");
            System.out.println("7. Finalize Task");
            System.out.println("8. Manage Hierarchy"); // NEW
            System.out.println("9. Sprint Management"); // NEW
            System.out.println("0. Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch(choice) {
                case "1" -> user.viewTasks();
                case "2" -> createTask(user);
                case "3" -> manageTask(user);
                case "4" -> developerActions(user);
                case "5" -> qaActions(user);
                case "6" -> breakTaskAction(user);
                case "7" -> finalizeTaskAction(user);
                case "8" -> hierarchyManagement(user); // NEW
                case "9" -> sprintManagement(user); // NEW
                case "0" -> logout = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // NEW: Hierarchy Management Menu
    private static void hierarchyManagement(User user) {
        System.out.println("\n--- Hierarchy Management ---");
        System.out.println("1. Link Story to Epic");
        System.out.println("2. Link SubTask to Story");
        System.out.println("3. View Task Hierarchy");
        System.out.println("4. Back");
        System.out.print("Select: ");
        String choice = scanner.nextLine();

        switch(choice) {
            case "1" -> linkStoryToEpic();
            case "2" -> linkSubTaskToStory();
            case "3" -> viewTaskHierarchy();
            case "4" -> { return; }
            default -> System.out.println("Invalid choice!");
        }
    }

    // NEW: Link Story to Epic
    private static void linkStoryToEpic() {
        System.out.print("Enter Epic title: ");
        String epicTitle = scanner.nextLine();
        System.out.print("Enter Story title: ");
        String storyTitle = scanner.nextLine();

        // Check if tasks exist
        Task epic = taskService.getTask(epicTitle);
        Task story = taskService.getTask(storyTitle);

        if (epic == null || story == null) {
            System.out.println("One or both tasks not found!");
            return;
        }

        if (!(epic instanceof Epic)) {
            System.out.println(epicTitle + " is not an Epic!");
            return;
        }

        if (!(story instanceof Story)) {
            System.out.println(storyTitle + " is not a Story!");
            return;
        }

        System.out.println("NOTE: First implement hierarchy in Epic/Story classes");
        System.out.println("Then add taskService.addStoryToEpic() method");
    }

    // NEW: Link SubTask to Story
    private static void linkSubTaskToStory() {
        System.out.print("Enter Story title: ");
        String storyTitle = scanner.nextLine();
        System.out.print("Enter SubTask title: ");
        String subTaskTitle = scanner.nextLine();

        // Check if tasks exist
        Task story = taskService.getTask(storyTitle);
        Task subTask = taskService.getTask(subTaskTitle);

        if (story == null || subTask == null) {
            System.out.println("One or both tasks not found!");
            return;
        }

        if (!(story instanceof Story)) {
            System.out.println(storyTitle + " is not a Story!");
            return;
        }

        if (!(subTask instanceof SubTask)) {
            System.out.println(subTaskTitle + " is not a SubTask!");
            return;
        }

        System.out.println("NOTE: First implement hierarchy in Story/SubTask classes");
        System.out.println("Then add taskService.addSubTaskToStory() method");
    }

    // NEW: View Task Hierarchy
    private static void viewTaskHierarchy() {
        System.out.println("NOTE: Implement taskService.viewHierarchy() method");
        System.out.println("\nCurrent Tasks (Flat List):");
        taskService.listAllTasks();
    }

    // NEW: Sprint Management Menu
    private static void sprintManagement(User user) {
        System.out.println("\n--- Sprint Management ---");
        System.out.println("1. Add Task to Sprint");
        System.out.println("2. Add Epic to Sprint (with hierarchy)");
        System.out.println("3. Add Story to Sprint (with hierarchy)");
        System.out.println("4. View Sprint with Hierarchy");
        System.out.println("5. View Sprint Tasks");
        System.out.println("6. Back");
        System.out.print("Select: ");
        String choice = scanner.nextLine();

        switch(choice) {
            case "1" -> addTaskToSprint();
            case "2" -> addEpicToSprint();
            case "3" -> addStoryToSprint();
            case "4" -> viewSprintWithHierarchy();
            case "5" -> viewSprintTasks();
            case "6" -> { return; }
            default -> System.out.println("Invalid choice!");
        }
    }

    // UPDATED: Add Task to Sprint - using your existing method
    private static void addTaskToSprint() {
        System.out.print("Enter Sprint objective: ");
        String sprintObj = scanner.nextLine();
        Sprint sprint = sprintService.getSprint(sprintObj);
        if (sprint == null) {
            System.out.println("Sprint not found!");
            return;
        }

        System.out.print("Enter Task title: ");
        String taskTitle = scanner.nextLine();
        Task task = taskService.getTask(taskTitle);
        if (task == null) {
            System.out.println("Task not found!");
            return;
        }

        // Use the simple method (no hierarchy)
        sprintService.addTaskToSprint(sprint, task);
    }

    // NEW: Add Epic to Sprint with hierarchy
    private static void addEpicToSprint() {
        System.out.print("Enter Sprint objective: ");
        String sprintObj = scanner.nextLine();
        Sprint sprint = sprintService.getSprint(sprintObj);
        if (sprint == null) {
            System.out.println("Sprint not found!");
            return;
        }

        System.out.print("Enter Epic title: ");
        String epicTitle = scanner.nextLine();

        // This method exists in SprintService
        sprintService.addEpicToSprint(sprint, epicTitle);
    }

    // NEW: Add Story to Sprint with hierarchy
    private static void addStoryToSprint() {
        System.out.print("Enter Sprint objective: ");
        String sprintObj = scanner.nextLine();
        Sprint sprint = sprintService.getSprint(sprintObj);
        if (sprint == null) {
            System.out.println("Sprint not found!");
            return;
        }

        System.out.print("Enter Story title: ");
        String storyTitle = scanner.nextLine();

        // This method exists in SprintService
        sprintService.addStoryToSprint(sprint, storyTitle);
    }

    // NEW: View Sprint with Hierarchy
    private static void viewSprintWithHierarchy() {
        System.out.print("Enter Sprint objective: ");
        String sprintObj = scanner.nextLine();
        Sprint sprint = sprintService.getSprint(sprintObj);
        if (sprint == null) {
            System.out.println("Sprint not found!");
            return;
        }

        // This method exists in SprintService
        sprintService.viewSprintWithHierarchy(sprint);
    }

    // NEW: View Sprint Tasks
    private static void viewSprintTasks() {
        System.out.print("Enter Sprint objective: ");
        String sprintObj = scanner.nextLine();
        Sprint sprint = sprintService.getSprint(sprintObj);
        if (sprint == null) {
            System.out.println("Sprint not found!");
            return;
        }

        System.out.println("\nTasks in Sprint: " + sprint.getObjective());
        for (Task task : sprint.getTasks()) {
            System.out.println("  - " + task.getTitle() +
                    " [" + task.getClass().getSimpleName() +
                    "] Status: " + task.getStatus());
        }
        System.out.println("Total tasks: " + sprint.getTasks().size());
        System.out.println("Completion: " + sprint.getCompletionPercentage() + "%");
    }

    // UPDATED: Create Task with more options
    private static void createTask(User user) {
        System.out.println("\n--- Create Task ---");

        if(user instanceof Stakeholder) {
            System.out.println("1. Create Epic");
            System.out.println("2. Create Story");
            System.out.println("3. Create Bug");
            System.out.println("4. Create StandardTask");
        } else if (user instanceof Developer) {
            System.out.println("Only Stakeholders can create Epics/Stories.");
            System.out.println("1. Create Bug");
            System.out.println("2. Create StandardTask");
        } else {
            System.out.println("You don't have permission to create tasks.");
            return;
        }

        String choice = scanner.nextLine();
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();

        if(user instanceof Stakeholder) {
            switch(choice) {
                case "1" -> ((Stakeholder)user).createEpic(new Epic(title));
                case "2" -> ((Stakeholder)user).createStory(new Story(title));
                case "3" -> createBug(title);
                case "4" -> createStandardTask(title);
                default -> System.out.println("Invalid choice!");
            }
        } else if (user instanceof Developer) {
            switch(choice) {
                case "1" -> createBug(title);
                case "2" -> createStandardTask(title);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // NEW: Create Bug
    private static void createBug(String title) {
        Bug bug = new Bug(title);
        taskService.addTask(bug);
        System.out.println("Bug created: " + title);
    }

    // NEW: Create StandardTask
    private static void createStandardTask(String title) {
        StandardTask task = new StandardTask(title);
        taskService.addTask(task);
        System.out.println("StandardTask created: " + title);
    }

    private static void manageTask(User user) {
        if(user instanceof ScrumMaster) {
            System.out.print("Enter task title to assign: ");
            String taskTitle = scanner.nextLine();
            Task task = taskService.getTask(taskTitle);
            if(task == null) {
                System.out.println("Task not found!");
                return;
            }

            System.out.print("Enter assignee username: ");
            String assigneeName = scanner.nextLine();
            User assignee = userService.getUser(assigneeName);
            if(assignee == null) {
                System.out.println("User not found!");
                return;
            }

            ((ScrumMaster)user).assignTask(task, assignee);
        } else {
            System.out.println("Only ScrumMaster can assign tasks.");
        }
    }

    // UPDATED: Break Task with better validation
    private static void breakTaskAction(User user) {
        if(user instanceof ScrumMaster) {
            System.out.print("Enter Story title to break into SubTasks: ");
            String title = scanner.nextLine();
            Task task = taskService.getTask(title);

            if(task == null) {
                System.out.println("Task not found!");
                return;
            }

            if(!(task instanceof Story)) {
                System.out.println("ERROR: Can only break Stories into SubTasks!");
                System.out.println(task.getTitle() + " is a " + task.getClass().getSimpleName());
                return;
            }

            System.out.print("Enter SubTask title: ");
            String subTitle = scanner.nextLine();
            ((ScrumMaster)user).breakTask((Story)task, new SubTask(subTitle));
        } else {
            System.out.println("Only ScrumMaster can break tasks.");
        }
    }

    private static void developerActions(User user) {
        if(user instanceof Developer) {
            System.out.print("Enter task title: ");
            String title = scanner.nextLine();
            Task task = taskService.getTask(title);
            if(task == null) {
                System.out.println("Task not found!");
                return;
            }
            System.out.println("1. Start Task  2. Complete Task");
            String choice = scanner.nextLine();
            if(choice.equals("1")) {
                ((Developer)user).startTask(task);
            } else if(choice.equals("2")) {
                ((Developer)user).completeTask(task);
            } else {
                System.out.println("Invalid choice!");
            }
        } else {
            System.out.println("Only Developer can perform this action.");
        }
    }

    private static void qaActions(User user) {
        if(user instanceof QAEngineer) {
            System.out.print("Enter task title to test: ");
            String title = scanner.nextLine();
            Task task = taskService.getTask(title);
            if(task == null) {
                System.out.println("Task not found!");
                return;
            }
            System.out.println("Test result options: Approved, Rejected, Tested");
            System.out.print("Enter QA result: ");
            String result = scanner.nextLine();
            ((QAEngineer)user).testTask(task, result);
        } else {
            System.out.println("Only QA Engineer can perform this action.");
        }
    }

    private static void finalizeTaskAction(User user) {
        if(user instanceof ScrumMaster) {
            System.out.print("Enter task title to finalize: ");
            String title = scanner.nextLine();
            Task task = taskService.getTask(title);
            if(task == null) {
                System.out.println("Task not found!");
                return;
            }
            ((ScrumMaster)user).finalizeTask(task);
        } else {
            System.out.println("Only ScrumMaster can finalize tasks.");
        }
    }

    private static void createSprint() {
        try {
            System.out.print("Enter sprint objective: ");
            String obj = scanner.nextLine();
            System.out.print("Enter start date (dd-MM-yyyy): ");
            Date start = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
            System.out.print("Enter end date (dd-MM-yyyy): ");
            Date end = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
            sprintService.createSprint(obj, start, end);
        } catch (Exception e) {
            System.out.println("Invalid date format!");
        }
    }
}