package mile1.entity;

import java.util.ArrayList;

public abstract class User {
    protected String username;
    protected String password;
    protected String role;
    protected ArrayList<Task> assignedTasks = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public ArrayList<Task> getAssignedTasks() { return assignedTasks; }

    public boolean checkPassword(String pw) { return password.equals(pw); }

    public void assignTask(Task task) {

        // ❗ Prevent duplicate assignment into list
        if (assignedTasks.contains(task)) {
            System.out.println("⚠ Task '" + task.getTitle() +
                    "' is already assigned to user '" + username + "'.");
            return;
        }

        assignedTasks.add(task);
        task.setAssignedUser(this);
    }

    public abstract void viewTasks();
}
