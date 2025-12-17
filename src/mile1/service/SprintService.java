package mile1.service;

import mile1.database.Database;
import mile1.entity.*;
import java.util.List;

import java.util.Date;

public class SprintService {

    // ----------------------------
    // Create a new Sprint
    // ----------------------------
    public void createSprint(String objective, Date start, Date end) {
        if (objective == null || objective.isEmpty()) {
            System.out.println("❌ Sprint objective cannot be empty!");
            return;
        }

        if (Database.getSprintByObjective(objective) != null) {
            System.out.println("⚠ Sprint with this objective already exists!");
            return;
        }

        Sprint sprint = new Sprint(objective, start, end);
        Database.sprints.add(sprint);
        System.out.println("✔ Sprint '" + objective + "' created successfully.");
    }

    // ----------------------------
    // Get a Sprint by objective
    // ----------------------------
    public Sprint getSprint(String objective) {
        return Database.getSprintByObjective(objective);
    }

    // ----------------------------
    // Add a task to sprint (single task)
    // ----------------------------
    public void addTaskToSprint(Sprint sprint, Task task) {
        if (sprint == null || task == null) {
            System.out.println("❌ Sprint or Task cannot be null.");
            return;
        }

        if (sprint.getTasks().contains(task)) {
            System.out.println("⚠ Task '" + task.getTitle() + "' is already in Sprint '" + sprint.getObjective() + "'");
            return;
        }

        sprint.addTask(task);
        System.out.println("✔ Task '" + task.getTitle() + "' added to Sprint '" + sprint.getObjective() + "'");
    }

    // ----------------------------
    // Add Epic or Story with hierarchy
    // ----------------------------
    public void addTaskToSprintWithHierarchy(Sprint sprint, Task task) {
        if (sprint == null || task == null) return;

        if (task instanceof Epic epic) {
            if (!sprint.getTasks().contains(epic)) sprint.addTask(epic);
            for (Story story : epic.getStories()) {
                if (!sprint.getTasks().contains(story)) sprint.addTask(story);
                for (SubTask subTask : story.getSubTasks()) {
                    if (!sprint.getTasks().contains(subTask)) sprint.addTask(subTask);
                }
            }
            System.out.println("✔ Epic '" + epic.getTitle() + "' and all its Stories/SubTasks added to Sprint");

        } else if (task instanceof Story story) {
            if (!sprint.getTasks().contains(story)) sprint.addTask(story);
            for (SubTask subTask : story.getSubTasks()) {
                if (!sprint.getTasks().contains(subTask)) sprint.addTask(subTask);
            }
            System.out.println("✔ Story '" + story.getTitle() + "' and all its SubTasks added to Sprint");

        } else {
            addTaskToSprint(sprint, task);
        }
    }

    // ----------------------------
    // Convenience: Add Epic by title
    // ----------------------------
    public void addEpicToSprint(Sprint sprint, String epicTitle) {
        if (sprint == null) {
            System.out.println("❌ Sprint not found.");
            return;
        }
        if (epicTitle == null || epicTitle.trim().isEmpty()) {
            System.out.println("❌ Epic title cannot be empty.");
            return;
        }

        Epic epic = Database.getTaskByTitleAndType(epicTitle.trim(), Epic.class);
        if (epic == null) {
            System.out.println("❌ ERROR: '" + epicTitle + "' is not an Epic or not found!");
            return;
        }

        addTaskToSprintWithHierarchy(sprint, epic);
    }


    // ----------------------------
    // Convenience: Add Story by title
    // ----------------------------
    public void addStoryToSprint(Sprint sprint, String storyTitle) {
        if (sprint == null) {
            System.out.println("❌ Sprint not found.");
            return;
        }
        if (storyTitle == null || storyTitle.trim().isEmpty()) {
            System.out.println("❌ Story title cannot be empty.");
            return;
        }

        Story story = Database.getTaskByTitleAndType(storyTitle.trim(), Story.class);
        if (story == null) {
            System.out.println("❌ ERROR: '" + storyTitle + "' is not a Story or not found!");
            return;
        }

        addTaskToSprintWithHierarchy(sprint, story);
    }


    // ----------------------------
    // View Sprint with hierarchy
    // ----------------------------
    public void viewSprintWithHierarchy(Sprint sprint) {
        if (sprint == null) {
            System.out.println("❌ Sprint not found.");
            return;
        }

        System.out.println("=== Sprint: " + sprint.getObjective() + " ===");
        for (Task task : sprint.getTasks()) {
            if (task instanceof Epic epic) {
                System.out.println("[Epic] " + epic.getTitle());
                for (Story story : epic.getStories()) {
                    System.out.println("  [Story] " + story.getTitle());
                    for (SubTask subTask : story.getSubTasks()) {
                        System.out.println("    [SubTask] " + subTask.getTitle());
                    }
                }
            } else if (task instanceof Story story) {
                if (story.getEpic() == null || !sprint.getTasks().contains(story.getEpic())) {
                    System.out.println("[Standalone Story] " + story.getTitle());
                    for (SubTask subTask : story.getSubTasks()) {
                        System.out.println("  [SubTask] " + subTask.getTitle());
                    }
                }
            } else if (task instanceof SubTask subTask) {
                if (subTask.getStory() == null || !sprint.getTasks().contains(subTask.getStory())) {
                    System.out.println("[Standalone SubTask] " + subTask.getTitle());
                }
            } else {
                System.out.println("[Task] " + task.getTitle() + " (" + task.getClass().getSimpleName() + ")");
            }
        }

        System.out.println("Total tasks: " + sprint.getTasks().size());
        System.out.println("Completion: " + sprint.getCompletionPercentage() + "%");
    }

    // ----------------------------
    // View all Sprints
    // ----------------------------
    public void viewAllSprints() {
        if (Database.sprints.isEmpty()) {
            System.out.println("No sprints found.");
            return;
        }

        System.out.println("=== All Sprints ===");
        for (Sprint sprint : Database.sprints) {
            System.out.println(sprint);
        }
    }

    public List<Sprint> getAllSprints() {
        return Database.sprints;
    }
    public void addUserToSprintTeam(Sprint sprint, User user) {
        if (sprint == null || user == null) {
            System.out.println("❌ Sprint or user cannot be null.");
            return;
        }
        sprint.addTeamMember(user);
    }

    public void viewSprintTeam(Sprint sprint) {
        if (sprint == null) {
            System.out.println("❌ Sprint not found.");
            return;
        }
        System.out.println("=== Sprint Team: " + sprint.getObjective() + " ===");
        if (sprint.getTeam().isEmpty()) {
            System.out.println("No team members assigned.");
            return;
        }
        for (User u : sprint.getTeam()) {
            System.out.println("- " + u.getUsername() + " (" + u.getRole() + ")");
        }
    }



}
