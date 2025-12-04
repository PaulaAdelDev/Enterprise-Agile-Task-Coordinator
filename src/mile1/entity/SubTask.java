package mile1.entity;

public class SubTask extends Task {
    private Story story; // Reference to parent Story

    public SubTask(String title) {
        super(title);
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    @Override
    public boolean isImplementationTask() {
        return true;
    }
}