package mile1.entity;

import java.util.ArrayList;
import java.util.List;

public class Story extends Task {
    private List<SubTask> subTasks;
    private Epic epic; // Optional: reference to parent Epic

    public Story(String title) {
        super(title);
        this.subTasks = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        subTask.setStory(this); // Optional: add back-reference
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public boolean isImplementationTask() {
        return true;
    }
}