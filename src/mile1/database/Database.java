package mile1.database;

import mile1.entity.*;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public static final List<User> users = new ArrayList<>();
    public static final List<Task> tasks = new ArrayList<>();
    public static final List<Sprint> sprints = new ArrayList<>();

    // ----------------------------
    // User lookup
    // ----------------------------
    public static User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username.trim()))
                .findFirst()
                .orElse(null);
    }

    // ----------------------------
    // Task lookup (by title only) - keep for backward compatibility
    // ----------------------------
    public static Task getTaskByTitle(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        return tasks.stream()
                .filter(t -> t.getTitle().equalsIgnoreCase(title.trim()))
                .findFirst()
                .orElse(null);
    }

    // ✅ NEW: Task lookup by (title + type) to avoid ambiguity
    public static <T extends Task> T getTaskByTitleAndType(String title, Class<T> type) {
        if (title == null || title.trim().isEmpty() || type == null) return null;

        return tasks.stream()
                .filter(t -> t.getTitle().equalsIgnoreCase(title.trim()) && type.isInstance(t))
                .map(type::cast)
                .findFirst()
                .orElse(null);
    }

    // ✅ NEW: Check existence by (title + type)
    public static boolean taskExists(String title, Class<? extends Task> type) {
        return getTaskByTitleAndType(title, type) != null;
    }

    // ----------------------------
    // Sprint lookup
    // ----------------------------
    public static Sprint getSprintByObjective(String objective) {
        if (objective == null || objective.trim().isEmpty()) return null;
        return sprints.stream()
                .filter(s -> s.getObjective().equalsIgnoreCase(objective.trim()))
                .findFirst()
                .orElse(null);
    }

    // ----------------------------
    // Optional: old checks kept
    // ----------------------------
    public static boolean userExists(String username) {
        return getUserByUsername(username) != null;
    }

    // Old: title-only (can be ambiguous if you allow same titles)
    public static boolean taskExists(String title) {
        return getTaskByTitle(title) != null;
    }

    public static boolean sprintExists(String objective) {
        return getSprintByObjective(objective) != null;
    }
}
