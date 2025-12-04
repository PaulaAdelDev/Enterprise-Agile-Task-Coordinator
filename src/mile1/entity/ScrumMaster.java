package mile1.entity;

import mile1.database.Database;

public class ScrumMaster extends User {

    public ScrumMaster(String username, String password) {
        super(username, password);
        this.role = "ScrumMaster";
    }

    @Override
    public void viewTasks() {
        System.out.println("Scrum Master view (all tasks):");
        Database.tasks.forEach(System.out::println);
    }

    public void assignTask(Task task, User user) {

        if (task.getAssignedUser() != null && task.getAssignedUser().equals(user)) {
            System.out.println("⚠ ERROR: User '" + user.getUsername() +
                    "' is already assigned to task '" + task.getTitle() + "'.");
            return;
        }

        // Assign to Developer (initial development)
        if((task.getStatus().equals("Created") || task.getStatus().equals("Planned"))
                && user instanceof Developer && task.isImplementationTask()) {

            user.assignTask(task);
            task.setStatus("Planned");
            System.out.println("Task assigned to Developer: " + user.getUsername());
            return;
        }

        // Assign to QA (after COMPLETED by Developer)
        if(task.getStatus().equals("Completed") && user instanceof QAEngineer) {
            user.assignTask(task);
            System.out.println("Task '" + task.getTitle() + "' moved to QA for testing.");
            return;
        }

        // Assign High-Level Epic/Story to SCRUM MASTER ONLY
        if(!task.isImplementationTask() && user instanceof ScrumMaster) {
            user.assignTask(task);
            task.setStatus("Planned");
            System.out.println("Epic/Story assigned to Scrum Master.");
            return;
        }

        System.out.println("Cannot assign task '" + task.getTitle() +
                "' to " + user.getUsername() + " in status " + task.getStatus());
    }


    private boolean validAssignment(Task task, User user) {

        // EPIC CAN BE ASSIGNED ONLY TO PRODUCT OWNER OR SCRUM MASTER
        if(!task.isImplementationTask() && (user instanceof Developer || user instanceof ScrumMaster))
            return true;

        // IMPLEMENTATION TASKS GO TO DEVELOPERS
        if(task.isImplementationTask() && user instanceof Developer)
            return true;

        // QA CAN ONLY TAKE TASKS AFTER COMPLETION FOR TESTING
        if(user instanceof QAEngineer && task.getStatus().equals("Completed"))
            return true;
        else{
        return false;
    }
}

    public void finalizeTask(Task task) {
        if(task.getStatus().equals("Approved") || task.getStatus().equals("Tested")) {
            task.setStatus("Done");
            System.out.println("Task '" + task.getTitle() + "' finalized as Done.");
        } else System.out.println("Cannot finalize task. Must be QA approved or tested first.");
    }

    public void breakTask(Story story, SubTask subTask) {  // Change parameter type
        if (!(story instanceof Story)) {
            System.out.println("ERROR: Can only break down Stories!");
            return;
        }

        subTask.setCreator(this);
        Database.tasks.add(subTask);

        // Link the SubTask to the Story
        story.addSubTask(subTask);
        System.out.println("Subtask '" + subTask.getTitle() +
                "' created and linked to Story '" + story.getTitle() + "'");
    }
}
