package mile1.entity;

public class Developer extends User {

    public Developer(String username, String password) {
        super(username, password);
        this.role = "Developer";
    }

    public void startTask(Task task) {
        if(assignedTasks.contains(task) && task.getStatus().equals("Planned")) {
            task.setStatus("In Progress");
            System.out.println("Task '" + task.getTitle() + "' started.");
        } else System.out.println("Cannot start task: " + task.getTitle());
    }

    public void completeTask(Task task) {
        if(assignedTasks.contains(task) && task.getStatus().equals("In Progress")) {
            task.setStatus("Completed");
            System.out.println("Task '" + task.getTitle() + "' completed.");
        } else System.out.println("Cannot complete task: " + task.getTitle());
    }

    @Override
    public void viewTasks() {
        System.out.println("Developer tasks:");
        assignedTasks.forEach(System.out::println);
    }
}
