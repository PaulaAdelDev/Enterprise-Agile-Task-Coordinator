package mile1.entity;

public abstract class Task {

    protected final String title;
    protected String status;
    protected User creator;
    protected User assignedUser;
    protected Sprint sprint;

    public Task(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }
        this.title = title.trim();
        this.status = "Planned"; // default initial status
    }

    // ----------------------------
    // Getters and Setters
    // ----------------------------
    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            System.out.println("⚠ Invalid status. Status not changed.");
            return;
        }
        this.status = status.trim();
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    // ----------------------------
    // Implementation type
    // ----------------------------
    public abstract boolean isImplementationTask();

    // ----------------------------
    // ToString
    // ----------------------------
    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "] " + title +
                " | Status: " + status +
                " | Creator: " + (creator != null ? creator.getUsername() : "None") +
                " | Assigned: " + (assignedUser != null ? assignedUser.getUsername() : "None") +
                " | Sprint: " + (sprint != null ? sprint.getObjective() : "None");
    }
}
