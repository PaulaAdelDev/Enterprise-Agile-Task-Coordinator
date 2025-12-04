package mile1.entity;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Story> stories;

    public Epic(String title) {
        super(title);
        this.stories = new ArrayList<>();
    }

    public void addStory(Story story) {
        stories.add(story);
        story.setEpic(this); // Optional: add back-reference
    }

    public List<Story> getStories() {
        return new ArrayList<>(stories); // Return copy
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(this);
        for (Story story : stories) {
            allTasks.add(story);
            allTasks.addAll(story.getSubTasks());
        }
        return allTasks;
    }

    @Override
    public boolean isImplementationTask() {
        return false;
    }
}