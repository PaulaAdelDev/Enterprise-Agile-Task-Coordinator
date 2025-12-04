// CREATE THIS FILE if you want "Task (standalone)"
package mile1.entity;

public class StandardTask extends Task {
    public StandardTask(String title) {
        super(title);
    }

    @Override
    public boolean isImplementationTask() {
        return true;
    }
}