package drpijon.task;

import java.time.LocalDate;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private final LocalDate by;
    private final String legacyBy;

    /**
     * Creates a task with a description and deadline.
     *
     * @param description task description
     * @param by deadline date
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
        this.legacyBy = null;
    }

    /**
     * Creates a deadline from the application's older free-text format.
     *
     * @param description task description
     * @param legacyBy original deadline text
     * @return deadline that preserves the original text
     */
    public static Deadline fromLegacy(String description, String legacyBy) {
        return new Deadline(description, null, legacyBy);
    }

    private Deadline(String description, LocalDate by, String legacyBy) {
        super(description);
        this.by = by;
        this.legacyBy = legacyBy;
    }

    /**
     * Returns the parsed deadline date.
     *
     * @return parsed deadline date, or null for a legacy free-text deadline
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns whether this deadline has a parsed date.
     *
     * @return true when the deadline uses the typed date format
     */
    public boolean hasDate() {
        return by != null;
    }

    /**
     * Returns the value used when this deadline is saved.
     *
     * @return ISO date text for parsed dates, or the original legacy text
     */
    public String getByText() {
        return hasDate() ? by.toString() : legacyBy;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "do by: " + getByText();
    }

    @Override
    public char getTaskType() {
        return 'D';
    }
}
