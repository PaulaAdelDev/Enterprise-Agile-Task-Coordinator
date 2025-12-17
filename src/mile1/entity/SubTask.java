package mile1.entity;

public class SubTask extends Task {

    private Story story; // Reference to parent Story

    public SubTask(String title) {
        super(title);
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("SubTask title cannot be empty.");
        }
        this.setStatus("Planned"); // default status for new SubTasks
    }

    /**
     * Set parent Story
     */
    public void setStory(Story story) {
        this.story = story;
    }

    /**
     * Get parent Story
     */
    public Story getStory() {
        return story;
    }

    @Override
    public boolean isImplementationTask() {
        return true; // SubTasks are implementation-level
    }
}
