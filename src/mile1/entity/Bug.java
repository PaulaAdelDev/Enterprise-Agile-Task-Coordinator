package mile1.entity;

public class Bug extends Task {

    public Bug(String title) {
        super(title); // calls Task constructor
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Bug title cannot be empty.");
        }
        this.setStatus("Planned"); // default status for a new Bug
    }

    @Override
    public boolean isImplementationTask() {
        return true; // Bugs require implementation/fixing
    }
}
