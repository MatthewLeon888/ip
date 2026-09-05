package drpijon;

/**
 * Represents a task scheduled between a start time and an end time.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Creates an event with a description and time range.
     *
     * @param description event description
     * @param from event start time
     * @param to event end time
     */
    public Event(String description, String from, String to) {
        super(description);
        setFrom(from);
        setTo(to);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    @Override
    public char getTaskType() {
        return 'E';
    }
}
