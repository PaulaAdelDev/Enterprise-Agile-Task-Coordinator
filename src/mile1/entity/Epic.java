package mile1.entity;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Story> stories;

    public Epic(String title) {
        super(title);
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Epic title cannot be empty.");
        }
        this.stories = new ArrayList<>();
        this.setStatus("Planned"); // default status for new Epics
    }

    /**
     * Add a Story to this Epic
     * @param story Story to add
     */
    public void addStory(Story story) {
        if (story == null) {
            System.out.println("❌ Cannot add null Story to Epic.");
            return;
        }
        if (!stories.contains(story)) {
            stories.add(story);
            story.setEpic(this); // set back-reference
            System.out.println("✅ Story '" + story.getTitle() + "' added to Epic '" + this.getTitle() + "'");
        } else {
            System.out.println("⚠ Story '" + story.getTitle() + "' is already in Epic '" + this.getTitle() + "'");
        }
    }

    /**
     * Return a copy of all stories
     */
    public List<Story> getStories() {
        return new ArrayList<>(stories);
    }

    /**
     * Get all tasks under this Epic, including itself, stories, and subtasks
     */
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
        return false; // Epics are high-level, not implementation tasks
    }
}
