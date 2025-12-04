package mile1.entity;

public class QAEngineer extends User {

    public QAEngineer(String username, String password) {
        super(username, password);
        this.role = "QAEngineer";
    }

    public void testTask(Task task, String result) {
        if(assignedTasks.contains(task) && task.getStatus().equals("Completed")) {

            switch(result) {
                case "Approved" -> task.setStatus("Approved");
                case "Rejected" -> task.setStatus("Rejected");
                case "Tested" -> task.setStatus("Tested");
                default -> {
                    System.out.println("Invalid QA result. Valid options: Approved, Rejected, Tested");
                    return; // <-- STOP so it doesn't print success
                }
            }

            System.out.println("QA result '" + result + "' set for task '" + task.getTitle() + "'");
        } else {
            System.out.println("Cannot test task: " + task.getTitle());
        }
    }


    @Override
    public void viewTasks() {
        System.out.println("QA Engineer tasks:");
        assignedTasks.forEach(System.out::println);
    }
}
