package mile1.entity;

public class Developer extends User {

    public Developer(String username, String password) {
        super(username, password, "Developer");
    }

    public void startTask(Task task) {
        if (task == null) {
            System.out.println("❌ Task does not exist.");
            return;
        }

        if (!assignedTasks.contains(task)) {
            System.out.println("❌ You are not assigned to this task: " + task.getTitle());
            return;
        }

        if (!task.getStatus().equals("Planned")) {
            System.out.println("❌ Task is not in 'Planned' state: " + task.getStatus());
            return;
        }

        task.setStatus("In Progress");
        System.out.println("✅ Task '" + task.getTitle() + "' started.");
    }

    public void completeTask(Task task) {
        if (task == null) {
            System.out.println("❌ Task does not exist.");
            return;
        }

        if (!assignedTasks.contains(task)) {
            System.out.println("❌ You are not assigned to this task: " + task.getTitle());
            return;
        }

        if (!task.getStatus().equals("In Progress")) {
            System.out.println("❌ Cannot complete task. Current state: " + task.getStatus());
            return;
        }

        task.setStatus("Completed");
        System.out.println("✅ Task '" + task.getTitle() + "' completed.");
    }

    @Override
    public void viewTasks() {
        System.out.println("\n📌 Developer Assigned Tasks:");

        if (assignedTasks.isEmpty()) {
            System.out.println("No tasks assigned.");
            return;
        }

        for (Task task : assignedTasks) {
            System.out.println("• " + task.getTitle() + " [" + task.getStatus() + "]");
        }
    }
}
