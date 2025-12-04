package mile1.database;

import mile1.entity.*;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Task> tasks = new ArrayList<>();
    public static ArrayList<Sprint> sprints = new ArrayList<>();

    public static User getUserByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public static Task getTaskByTitle(String title) {
        return tasks.stream().filter(t -> t.getTitle().equals(title)).findFirst().orElse(null);
    }

    public static Sprint getSprintByObjective(String objective) {
        return sprints.stream().filter(s -> s.getObjective().equals(objective)).findFirst().orElse(null);
    }
}
