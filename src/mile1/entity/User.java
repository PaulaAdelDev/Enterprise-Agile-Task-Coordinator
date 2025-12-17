package mile1.entity;

import java.util.ArrayList;

public abstract class User {

    protected String username;
    protected String password;
    protected String role;
    protected ArrayList<Task> assignedTasks = new ArrayList<>();

    // Main Constructor
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;

        // prevent null or empty role
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty or null.");
        }
        this.role = role;
    }

    // FIXED: Secondary constructor now properly initializes fields
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "Unknown"; // default role
        this.assignedTasks = new ArrayList<>();
    }

    // Getters
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getPassword() { return password; }

    public ArrayList<Task> getAssignedTasks() {
        return assignedTasks;
    }

    // Password check
    public boolean checkPassword(String pw) {
        return password.equals(pw);
    }

    // Assign task to the user
    public void assignTask(Task task) {

        // Prevent null task assignment
        if (task == null) {
            System.out.println("Error: Cannot assign a null task!");
            return;
        }

        // Prevent duplicates
        if (assignedTasks.contains(task)) {
            System.out.println("⚠ Task '" + task.getTitle() +
                    "' is already assigned to user '" + username + "'.");
            return;
        }

        assignedTasks.add(task);
        task.setAssignedUser(this);
    }

    // Each user type will display tasks differently
    public abstract void viewTasks();
}