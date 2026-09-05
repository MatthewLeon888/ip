package drpijon;

/**
 * Represents a task that can be marked as done or not done.
 */
public class Todo extends Task {
    /**
     * Creates a not-done task with the specified description.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        String status = this.isDone() ? "Yes" : "No";
        return super.toString() + System.lineSeparator() + "is done? " + status;
    }

    @Override
    public char getTaskType() {
        return 'T';
    }
}
