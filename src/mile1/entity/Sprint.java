package mile1.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sprint {

    private final String objective;
    private final Date startDate;
    private final Date endDate;
    private final List<Task> tasks;

    public Sprint(String objective, Date start, Date end) {
        if (objective == null || objective.trim().isEmpty()) {
            throw new IllegalArgumentException("Sprint objective cannot be empty.");
        }
        if (start == null || end == null || end.before(start)) {
            throw new IllegalArgumentException("Invalid start or end date for Sprint.");
        }

        this.objective = objective;
        this.startDate = start;
        this.endDate = end;
        this.tasks = new ArrayList<>();
    }

    // ----------------------------
    // Getters
    // ----------------------------
    public String getObjective() { return objective; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public List<Task> getTasks() { return new ArrayList<>(tasks); } // Return copy to prevent external modification

    // ----------------------------
    // Add Task to Sprint
    // ----------------------------
    public void addTask(Task task) {
        if (task == null) {
            System.out.println("❌ Cannot add null task to Sprint.");
            return;
        }
        if (!tasks.contains(task)) {
            tasks.add(task);
            task.setSprint(this);
            System.out.println("✅ Task '" + task.getTitle() + "' added to Sprint '" + objective + "'");
        } else {
            System.out.println("⚠ Task '" + task.getTitle() + "' is already in Sprint '" + objective + "'");
        }
    }

    // ----------------------------
    // Completion Percentage
    // ----------------------------
    public int getCompletionPercentage() {
        if (tasks.isEmpty()) return 0;
        long completed = tasks.stream().filter(t -> t.getStatus().equals("Done")).count();
        return (int)((completed * 100.0) / tasks.size());
    }

    // ----------------------------
    // ToString
    // ----------------------------
    @Override
    public String toString() {
        return "Sprint: " + objective +
                " | Start: " + startDate +
                " | End: " + endDate +
                " | Tasks: " + tasks.size() +
                " | Completion: " + getCompletionPercentage() + "%";
    }
    // add this field
    private final List<User> team = new ArrayList<>();

    public List<User> getTeam() {
        return team;
    }

    public void addTeamMember(User user) {
        if (user == null) {
            System.out.println("❌ Cannot add null team member.");
            return;
        }
        if (team.contains(user)) {
            System.out.println("⚠ " + user.getUsername() + " is already in the sprint team.");
            return;
        }
        team.add(user);
        System.out.println("✔ Team member added: " + user.getUsername());
    }

    public void removeTeamMember(User user) {
        if (user == null) return;
        team.remove(user);
    }
}




