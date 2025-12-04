package mile1.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sprint {
    private String objective;
    private Date startDate;
    private Date endDate;
    private List<Task> tasks = new ArrayList<>(); // Changed to List interface

    public Sprint(String objective, Date start, Date end) {
        this.objective = objective;
        this.startDate = start;
        this.endDate = end;
    }

    public String getObjective() { return objective; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public List<Task> getTasks() { return tasks; }

    public void addTask(Task task) {
        tasks.add(task);
        task.setSprint(this);
    }

    public int getCompletionPercentage() {
        if(tasks.isEmpty()) return 0;
        long completed = tasks.stream().filter(t -> t.getStatus().equals("Done")).count();
        return (int)((completed * 100.0) / tasks.size());
    }

    @Override
    public String toString() {
        return "Sprint: " + objective + " | Start: " + startDate + " | End: " + endDate +
                " | Tasks: " + tasks.size() + " | Completion: " + getCompletionPercentage() + "%";
    }
}