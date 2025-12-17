package mile1.entity;

public class StandardTask extends Task {

    public StandardTask(String title) {
        super(title);
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("StandardTask title cannot be empty.");
        }
        this.setStatus("Planned"); // default status for new tasks
    }

    @Override
    public boolean isImplementationTask() {
        return true; // StandardTasks are implementation tasks
    }
}
