/**
 * Represents a task that can be marked as done or not done.
 */
public class Todo extends Task {
    private boolean isDone = false;

    /**
     * Creates a not-done task with the specified description.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Sets whether this task is done.
     *
     * @param isDone whether the task is done
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns whether this task is done.
     *
     * @return true when the task is done
     */
    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        String status = isDone ? "Yes" : "No";
        return super.toString() + System.lineSeparator() + "is done? " + status;
    }
}
