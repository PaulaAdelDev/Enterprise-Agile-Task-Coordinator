package mile1.service;

import mile1.database.Database;
import mile1.entity.Story;
import mile1.entity.Task;
import mile1.entity.*;

public class TaskService {
    public void addTask(Task task) {
        if (taskExists(task.getTitle())) {
            System.out.println("ERROR: Task with title '" + task.getTitle() + "' already exists!");
            return;
        }

        Database.tasks.add(task);
        System.out.println("✔ Task '" + task.getTitle() + "' created successfully.");
    }

    private boolean taskExists(String title) {
        return Database.tasks.stream()
                .anyMatch(t -> t.getTitle().equalsIgnoreCase(title));
    }


    public void listAllTasks() {
        System.out.println("All tasks:");
        Database.tasks.forEach(System.out::println);
    }

    public Task getTask(String title) {
        return Database.getTaskByTitle(title);
    }
    // NEW: Link Story to Epic
    public void addStoryToEpic(String epicTitle, String storyTitle) {
        Task epicTask = getTask(epicTitle);
        Task storyTask = getTask(storyTitle);

        if (!(epicTask instanceof Epic)) {
            System.out.println("ERROR: " + epicTitle + " is not an Epic!");
            return;
        }

        if (!(storyTask instanceof Story)) {
            System.out.println("ERROR: " + storyTitle + " is not a Story!");
            return;
        }

        Epic epic = (Epic) epicTask;
        Story story = (Story) storyTask;
        epic.addStory(story);
        System.out.println("✓ Story '" + storyTitle + "' added to Epic '" + epicTitle + "'");
    }

    // NEW: Link SubTask to Story
    public void addSubTaskToStory(String storyTitle, String subTaskTitle) {
        Task storyTask = getTask(storyTitle);
        Task subTaskTask = getTask(subTaskTitle);

        if (!(storyTask instanceof Story)) {
            System.out.println("ERROR: " + storyTitle + " is not a Story!");
            return;
        }

        if (!(subTaskTask instanceof SubTask)) {
            System.out.println("ERROR: " + subTaskTitle + " is not a SubTask!");
            return;
        }

        Story story = (Story) storyTask;
        SubTask subTask = (SubTask) subTaskTask;
        story.addSubTask(subTask);
        System.out.println("✓ SubTask '" + subTaskTitle + "' added to Story '" + storyTitle + "'");
    }

    // NEW: View hierarchy
    public void viewHierarchy() {
        System.out.println("TASK HIERARCHY:");
        for (Task task : Database.tasks) {
            if (task instanceof Epic) {
                printEpicHierarchy((Epic) task, 0);
            } else if (!(task instanceof Story || task instanceof SubTask)) {
                // Show standalone tasks
                System.out.println("  [Standalone] " + task.getTitle());
            }
        }
    }

    private void printEpicHierarchy(Epic epic, int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "[Epic] " + epic.getTitle());

        for (Story story : epic.getStories()) {
            System.out.println(indent + "  [Story] " + story.getTitle());
            for (SubTask subTask : story.getSubTasks()) {
                System.out.println(indent + "    [SubTask] " + subTask.getTitle());
            }
        }
    }

}
