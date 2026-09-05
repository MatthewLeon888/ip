/**
 * Represents a task with a textual description.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the specified description.
     *
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
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
        return "description: " + description;
    }

    public char getTaskType() {
        return ' ';
    }
}
