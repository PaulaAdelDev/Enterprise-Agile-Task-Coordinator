package mile1.service;

import mile1.database.Database;
import mile1.entity.User;

public class UserService {
    public void addUser(User user) {
        Database.users.add(user);
    }

    public void listUsers() {
        System.out.println("All users:");
        Database.users.forEach(u -> System.out.println(u.getUsername() + " (" + u.getRole() + ")"));
    }

    public User getUser(String username) {
        return Database.getUserByUsername(username);
    }
}
