package mile1.service;

import mile1.database.Database;
import mile1.entity.*;
import java.util.Date;

public class SprintService {
    // EXISTING METHODS (from your original)
    public void createSprint(String objective, Date start, Date end) {
        Database.sprints.add(new Sprint(objective, start, end));
        System.out.println("Sprint created successfully.");
    }

    public Sprint getSprint(String objective) {
        return Database.getSprintByObjective(objective);
    }

    public void addTaskToSprint(Sprint sprint, Task task) {
        sprint.addTask(task);
        System.out.println("Task '" + task.getTitle() + "' added to Sprint '" + sprint.getObjective() + "'");
    }

    public void viewAllSprints() {
        System.out.println("All Sprints:");
        Database.sprints.forEach(System.out::println);
    }

    // NEW HIERARCHY METHODS (from what you provided)
    public void addTaskToSprintWithHierarchy(Sprint sprint, Task task) {
        if (task instanceof Epic) {
            // Add Epic and all its children
            Epic epic = (Epic) task;
            sprint.addTask(epic);

            for (Story story : epic.getStories()) {
                sprint.addTask(story);
                for (SubTask subTask : story.getSubTasks()) {
                    sprint.addTask(subTask);
                }
            }
            System.out.println("✓ Epic '" + task.getTitle() +
                    "' and all its Stories/SubTasks added to Sprint");

        } else if (task instanceof Story) {
            // Add Story and all its SubTasks
            Story story = (Story) task;
            sprint.addTask(story);

            for (SubTask subTask : story.getSubTasks()) {
                sprint.addTask(subTask);
            }
            System.out.println("✓ Story '" + task.getTitle() +
                    "' and all its SubTasks added to Sprint");

        } else {
            // Add standalone tasks (Bug, StandardTask, SubTask without Story)
            sprint.addTask(task);
            System.out.println("✓ Task '" + task.getTitle() + "' added to Sprint");
        }
    }

    // NEW: Add specific task types with validation
    public void addEpicToSprint(Sprint sprint, String epicTitle) {
        Task task = Database.getTaskByTitle(epicTitle);
        if (task instanceof Epic) {
            addTaskToSprintWithHierarchy(sprint, task);
        } else {
            System.out.println("ERROR: '" + epicTitle + "' is not an Epic!");
        }
    }

    public void addStoryToSprint(Sprint sprint, String storyTitle) {
        Task task = Database.getTaskByTitle(storyTitle);
        if (task instanceof Story) {
            addTaskToSprintWithHierarchy(sprint, task);
        } else {
            System.out.println("ERROR: '" + storyTitle + "' is not a Story!");
        }
    }

    // NEW: View sprint with hierarchy
    public void viewSprintWithHierarchy(Sprint sprint) {
        System.out.println("SPRINT: " + sprint.getObjective());
        System.out.println("Tasks in this sprint:");

        // Group by type
        for (Task task : sprint.getTasks()) {
            if (task instanceof Epic) {
                System.out.println("  [Epic] " + task.getTitle());
            } else if (task instanceof Story) {
                // Check if parent Epic is in sprint
                Story story = (Story) task;
                if (story.getEpic() == null ||
                        !sprint.getTasks().contains(story.getEpic())) {
                    System.out.println("  [Standalone Story] " + task.getTitle());
                } else {
                    System.out.println("    [Story] " + task.getTitle());
                }
            } else if (task instanceof SubTask) {
                System.out.println("      [SubTask] " + task.getTitle());
            } else {
                System.out.println("  [Task] " + task.getTitle() +
                        " (" + task.getClass().getSimpleName() + ")");
            }
        }
    }
}