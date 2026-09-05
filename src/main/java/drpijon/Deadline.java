package drpijon;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private String by;

    /**
     * Creates a task with a description and deadline.
     *
     * @param description task description
     * @param by deadline description
     */
    public Deadline(String description, String by) {
        super(description);
        setBy(by);
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getBy() {
        return by;
    }
    
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "do by: " + by;
    }

    @Override
    public char getTaskType() {
        return 'D';
    }
}
