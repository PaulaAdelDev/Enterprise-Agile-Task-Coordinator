package mile1.service;

import mile1.database.Database;
import mile1.entity.User;

public class UserService {

    // Create user with validation
    public void addUser(User user) {

        // Safety check
        if (user == null) {
            System.out.println("Error: User cannot be null!");
            return;
        }

        String username = user.getUsername();
        String role = user.getRole();

        // Validate: username cannot be empty
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error: Username cannot be empty!");
            return;
        }
        username = username.trim();

        // Validate: password cannot be empty
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Error: Password cannot be empty!");
            return;
        }

        // Password constraint:
        // - at least 4 characters
        // - letters or digits only
        String password = user.getPassword().trim();
        if (password.length() < 4 || !password.matches("[a-zA-Z0-9]+")) {
            System.out.println("Error: Password must be at least 4 characters and contain only letters or digits!");
            return;
        }

        // Validate: role cannot be empty
        if (role == null || role.trim().isEmpty()) {
            System.out.println("Error: Role cannot be empty!");
            return;
        }

        // Validate: duplicate username
        if (Database.getUserByUsername(username) != null) {
            System.out.println("Error: Username already exists!");
            return;
        }

        // If all checks passed → Add user
        Database.users.add(user);
        System.out.println("User registered successfully: " +
                user.getUsername() + " (" + user.getRole() + ")");
    }

    // List all users
    public void listUsers() {
        if (Database.users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("All users:");
        for (User u : Database.users) {
            System.out.println("- " + u.getUsername() + " (" + u.getRole() + ")");
        }
    }

    // Get user by username
    public User getUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return Database.getUserByUsername(username.trim());
    }
}
