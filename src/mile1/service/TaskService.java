package mile1.service;

import mile1.database.Database;
import mile1.entity.*;

public class TaskService {

    // ----------------------------
    // Add a Task (unique per title+type)
    // ----------------------------
    public void addTask(Task task) {
        if (task == null) {
            System.out.println("❌ Cannot add null task.");
            return;
        }

        if (Database.taskExists(task.getTitle(), task.getClass())) {
            System.out.println("⚠ ERROR: " + task.getClass().getSimpleName()
                    + " with title '" + task.getTitle() + "' already exists!");
            return;
        }

        Database.tasks.add(task);
        System.out.println("✔ Task '" + task.getTitle() + "' created successfully.");
    }

    // ----------------------------
    // List all Tasks
    // ----------------------------
    public void listAllTasks() {
        if (Database.tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("All tasks:");
        Database.tasks.forEach(System.out::println);
    }

    // ----------------------------
    // Get Task by Title (AMBIGUOUS if same titles exist)
    // Keep it, but use carefully.
    // ----------------------------
    public Task getTask(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        return Database.getTaskByTitle(title.trim());
    }

    // ✅ NEW: Get Task by Title AND Type (SAFE)
    public <T extends Task> T getTask(String title, Class<T> type) {
        return Database.getTaskByTitleAndType(title, type);
    }

    // ----------------------------
    // View Task Hierarchy
    // ----------------------------
    public void viewHierarchy() {
        System.out.println("=== TASK HIERARCHY ===");

        boolean hasTasks = false;
        for (Task task : Database.tasks) {
            if (task instanceof Epic epic) {
                printEpicHierarchy(epic);
                hasTasks = true;
            } else if (!(task instanceof Story || task instanceof SubTask)) {
                System.out.println("[Standalone] " + task.getTitle());
                hasTasks = true;
            }
        }

        if (!hasTasks) System.out.println("No tasks found.");
    }

    private void printEpicHierarchy(Epic epic) {
        System.out.println("[Epic] " + epic.getTitle());

        for (Story story : epic.getStories()) {
            System.out.println("  [Story] " + story.getTitle());
            for (SubTask subTask : story.getSubTasks()) {
                System.out.println("    [SubTask] " + subTask.getTitle());
            }
        }
    }

    // ----------------------------
    // Link Story -> Epic (SAFE)
    // ----------------------------
    public void addStoryToEpic(String epicTitle, String storyTitle) {

        if (epicTitle == null || epicTitle.trim().isEmpty()) {
            System.out.println("❌ Epic title cannot be empty.");
            return;
        }
        if (storyTitle == null || storyTitle.trim().isEmpty()) {
            System.out.println("❌ Story title cannot be empty.");
            return;
        }

        Epic epic = getTask(epicTitle.trim(), Epic.class);
        Story story = getTask(storyTitle.trim(), Story.class);

        if (epic == null) {
            System.out.println("❌ ERROR: '" + epicTitle + "' is not an Epic or not found!");
            return;
        }
        if (story == null) {
            System.out.println("❌ ERROR: '" + storyTitle + "' is not a Story or not found!");
            return;
        }

        if (story.getEpic() != null && story.getEpic() != epic) {
            System.out.println("⚠ Story '" + story.getTitle() + "' is already linked to Epic '" +
                    story.getEpic().getTitle() + "'. It will be linked to '" + epic.getTitle() + "' now.");
        }

        epic.addStory(story);
    }

    // ----------------------------
    // Create + Link SubTask -> Story (SAFE)
    // ----------------------------
    public void addSubTaskToStory(String storyTitle, String subTitle) {

        if (storyTitle == null || storyTitle.trim().isEmpty()) {
            System.out.println("❌ Story title cannot be empty.");
            return;
        }
        if (subTitle == null || subTitle.trim().isEmpty()) {
            System.out.println("❌ SubTask title cannot be empty.");
            return;
        }

        Story story = getTask(storyTitle.trim(), Story.class);
        if (story == null) {
            System.out.println("❌ ERROR: '" + storyTitle + "' is not a Story or not found!");
            return;
        }

        // prevent duplicate SubTask titles within SubTask type
        if (Database.taskExists(subTitle.trim(), SubTask.class)) {
            System.out.println("⚠ ERROR: SubTask with title '" + subTitle.trim() + "' already exists!");
            return;
        }

        SubTask subTask = new SubTask(subTitle.trim());
        addTask(subTask);
        story.addSubTask(subTask);
    }
    public void addTaskAsStakeholder(Task task) {
        if (!(task instanceof Epic) && !(task instanceof Story)) {
            System.out.println("❌ Stakeholder can only create Epics and Stories.");
            return;
        }
        addTask(task);
    }

}
