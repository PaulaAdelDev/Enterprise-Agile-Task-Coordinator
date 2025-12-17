package mile1.entity;

public class QAEngineer extends User {

    public QAEngineer(String username, String password) {
        super(username, password, "QAEngineer");
    }

    public void testTask(Task task, String result) {
        if (task == null) {
            System.out.println("❌ Task does not exist.");
            return;
        }

        if (!assignedTasks.contains(task)) {
            System.out.println("❌ You are not assigned to this task: " + task.getTitle());
            return;
        }

        if (!task.getStatus().equals("Completed")) {
            System.out.println("❌ Task must be 'Completed' before QA testing. Current status: " + task.getStatus());
            return;
        }

        switch (result) {
            case "Approved", "Rejected", "Tested" -> {
                task.setStatus(result);
                System.out.println("✅ QA result '" + result + "' set for task '" + task.getTitle() + "'");
            }
            default -> System.out.println("❌ Invalid QA result. Valid options: Approved, Rejected, Tested");
        }
    }

    @Override
    public void viewTasks() {
        System.out.println("\n📌 QA Engineer Assigned Tasks:");

        if (assignedTasks.isEmpty()) {
            System.out.println("No tasks assigned.");
            return;
        }

        for (Task task : assignedTasks) {
            System.out.println("• " + task.getTitle() + " [" + task.getStatus() + "]");
        }
    }
}
