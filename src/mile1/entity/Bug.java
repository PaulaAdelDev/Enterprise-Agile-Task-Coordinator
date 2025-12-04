package mile1.entity;

public class Bug extends Task {

    public Bug(String title) {
        super(title);
    }

    @Override
    public boolean isImplementationTask() {
        return true; // Bugs require implementation/fixing
    }
}
