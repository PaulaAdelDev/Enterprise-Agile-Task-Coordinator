package mile1.entity;

import mile1.database.Database;
import mile1.service.TaskService;

public class Stakeholder extends User {

    private final TaskService taskService = new TaskService();

    public Stakeholder(String username, String password) {
        super(username, password, "Stakeholder");
    }

    public void createEpic(Epic epic) {
        if (epic == null) {
            System.out.println("❌ Epic cannot be null.");
            return;
        }

        epic.setCreator(this);

        if (Database.taskExists(epic.getTitle(), Epic.class)) {
            System.out.println("⚠ Epic '" + epic.getTitle() + "' already exists.");
            return;
        }

        taskService.addTask(epic);
        System.out.println("✅ Epic created: '" + epic.getTitle() + "'");
    }


    public void createStory(Story story) {
        if (story == null) {
            System.out.println("❌ Story cannot be null.");
            return;
        }

        story.setCreator(this);

        if (Database.taskExists(story.getTitle(), Story.class)) {
            System.out.println("⚠ Story '" + story.getTitle() + "' already exists.");
            return;
        }

        taskService.addTask(story);
        System.out.println("✅ Story created: '" + story.getTitle() + "'");
    }


    @Override
    public void viewTasks() {
        System.out.println("\n📌 Stakeholder View (High-Level Requests Only):");

        boolean found = false;

        for (Task task : Database.tasks) {
            if (task instanceof Epic || task instanceof Story) {
                System.out.println("• " + task.getTitle() +
                        " [" + task.getClass().getSimpleName() +
                        "] Status: " + task.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No Epics/Stories found.");
        }
    }


    public void createStoryForEpic(Epic epic, Story story) {
        if (epic == null || story == null) {
            System.out.println("❌ Epic or Story cannot be null.");
            return;
        }

        story.setCreator(this);

        if (!Database.taskExists(story.getTitle(), Story.class)) {
            taskService.addTask(story);
        }

        epic.addStory(story);

        System.out.println("✅ Story '" + story.getTitle() +
                "' created and added to Epic '" + epic.getTitle() + "'");
    }

}

