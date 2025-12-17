package mile1.entity;

import java.util.ArrayList;
import java.util.List;

public class Story extends Task {

    private final List<SubTask> subTasks;
    private Epic epic; // Optional: reference to parent Epic

    public Story(String title) {
        super(title);
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Story title cannot be empty.");
        }
        this.subTasks = new ArrayList<>();
        this.setStatus("Planned"); // default status
    }

    /**
     * Add a SubTask to this Story
     */
    public void addSubTask(SubTask subTask) {
        if (subTask == null) {
            System.out.println("❌ Cannot add null SubTask to Story.");
            return;
        }
        if (!subTasks.contains(subTask)) {
            subTasks.add(subTask);
            subTask.setStory(this); // set back-reference
            System.out.println("✅ SubTask '" + subTask.getTitle() + "' added to Story '" + this.getTitle() + "'");
        } else {
            System.out.println("⚠ SubTask '" + subTask.getTitle() + "' already exists in Story '" + this.getTitle() + "'");
        }
    }

    /**
     * Get a copy of SubTasks
     */
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    /**
     * Set Epic reference
     */
    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public boolean isImplementationTask() {
        return true; // Stories are implementation-level tasks
    }
}
