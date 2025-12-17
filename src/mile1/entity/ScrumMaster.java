package mile1.entity;

import mile1.database.Database;
import mile1.service.TaskService;

public class ScrumMaster extends User {

    public ScrumMaster(String username, String password) {
        super(username, password, "ScrumMaster");
    }

    @Override
    public void viewTasks() {
        System.out.println("\n📌 Scrum Master View (All Tasks):");
        if (Database.tasks.isEmpty()) {
            System.out.println("No tasks in the system.");
            return;
        }

        for (Task task : Database.tasks) {
            System.out.println("• " + task.getTitle() +
                    " [" + task.getClass().getSimpleName() +
                    "] Status: " + task.getStatus());
        }
    }

    public void assignTask(Task task, User user) {
        if (task == null || user == null) {
            System.out.println("❌ Task or user cannot be null.");
            return;
        }

        if (task.getAssignedUser() != null && task.getAssignedUser().equals(user)) {
            System.out.println("⚠ User '" + user.getUsername() +
                    "' is already assigned to task '" + task.getTitle() + "'.");
            return;
        }

        if (!validAssignment(task, user)) {
            System.out.println("❌ Cannot assign task '" + task.getTitle() +
                    "' to " + user.getUsername() + " in status " + task.getStatus());
            return;
        }

        user.assignTask(task);

        // Update status based on type of user
        if (user instanceof Developer && task.isImplementationTask() &&
                (task.getStatus().equals("Created") || task.getStatus().equals("Planned"))) {
            task.setStatus("Planned");
            System.out.println("✅ Task assigned to Developer: " + user.getUsername());
        } else if (user instanceof QAEngineer && task.getStatus().equals("Completed")) {
            System.out.println("✅ Task '" + task.getTitle() + "' moved to QA for testing.");
        } else if (!task.isImplementationTask() && user instanceof ScrumMaster) {
            task.setStatus("Planned");
            System.out.println("✅ Epic/Story assigned to Scrum Master.");
        }
    }

    private boolean validAssignment(Task task, User user) {
        if (task == null || user == null) return false;

        // EPIC/Story: only ScrumMaster can take
        if (!task.isImplementationTask() && user instanceof ScrumMaster) return true;

        // Implementation tasks: only Developers
        if (task.isImplementationTask() && user instanceof Developer) return true;

        // QA can only take Completed tasks
        if (user instanceof QAEngineer && task.getStatus().equals("Completed")) return true;

        return false;
    }

    public void finalizeTask(Task task) {
        if (task == null) {
            System.out.println("❌ Task does not exist.");
            return;
        }

        if (task.getStatus().equals("Approved") || task.getStatus().equals("Tested")) {
            task.setStatus("Done");
            System.out.println("✅ Task '" + task.getTitle() + "' finalized as Done.");
        } else {
            System.out.println("❌ Cannot finalize task. Must be QA approved or tested first.");
        }
    }

    public void breakTask(Story story, SubTask subTask) {
        if (story == null || subTask == null) {
            System.out.println("❌ Story or SubTask cannot be null.");
            return;
        }

        subTask.setCreator(this);

        TaskService taskService = new TaskService();

        // Use service to enforce uniqueness + consistency
        if (Database.taskExists(subTask.getTitle(), SubTask.class)) {
            System.out.println("⚠ SubTask '" + subTask.getTitle() + "' already exists.");
            return;
        }

        taskService.addTask(subTask);
        story.addSubTask(subTask);

        System.out.println("✅ Subtask '" + subTask.getTitle() +
                "' created and linked to Story '" + story.getTitle() + "'");
    }

}
