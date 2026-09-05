/**
 * Represents a task with a textual description.
 */
public class Task {
    private final String description;

    /**
     * Creates a task with the specified description.
     *
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return "description: " + description;
    }
}
