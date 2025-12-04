package mile1.entity;

public abstract class Task {
    protected String title;
    protected String status;
    protected User creator;
    protected User assignedUser;
    protected Sprint sprint;

    public Task(String title) { this.title = title; this.status = "Created"; }

    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setCreator(User creator) { this.creator = creator; }
    public User getCreator() { return creator; }
    public void setAssignedUser(User user) { this.assignedUser = user; }
    public User getAssignedUser() { return assignedUser; }
    public void setSprint(Sprint sprint) { this.sprint = sprint; }
    public Sprint getSprint() { return sprint; }

    public abstract boolean isImplementationTask();

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "] " + title + " | Status: " + status +
                " | Creator: " + (creator != null ? creator.getUsername() : "None") +
                " | Assigned: " + (assignedUser != null ? assignedUser.getUsername() : "None") +
                " | Sprint: " + (sprint != null ? sprint.getObjective() : "None");
    }
}
