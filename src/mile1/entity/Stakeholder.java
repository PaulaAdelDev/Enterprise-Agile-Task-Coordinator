package mile1.entity;

import mile1.database.Database;
import mile1.service.TaskService;

public class Stakeholder extends User {

    private final TaskService taskService = new TaskService();

    public Stakeholder(String username, String password) {
        super(username, password);
        this.role = "Stakeholder";
    }

    public void createEpic(Epic epic) {
        epic.setCreator(this);
        taskService.addTask(epic);  // Prevent duplicate
    }

    public void createStory(Story story) {
        story.setCreator(this);
        taskService.addTask(story); // Prevent duplicate
    }

    @Override
    public void viewTasks() {
        System.out.println("Stakeholder view (all tasks):");
        Database.tasks.forEach(System.out::println);
    }
    public void createStoryForEpic(Epic epic, Story story) {
        story.setCreator(this);
        taskService.addTask(story);

        // Link to Epic
        epic.addStory(story);
        System.out.println("Story '" + story.getTitle() +
                "' created and added to Epic '" + epic.getTitle() + "'");
    }
}
