package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private final LocalDateTime by;

    /**
     * Constructs a {@code Deadline} task with the specified description and due date.
     * @param description Description of the deadline task.
     * @param by Due date and time of the deadline task,
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    /**
     * Returns the due date and time of the deadline task.
     * @return Due date and time.
     */
    public LocalDateTime getBy() {
        return this.by;
    }

    /**
     * Returns an array of strings representing the task for storage in a file.
     * @return String array suitable for file input.
     */
    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "D";
        s[3] = this.by.toString();
        return s;
    }

    /**
     * Returns the main date and time associated with the task.
     * @return Date and time of the task.
     */
    @Override
    public LocalDateTime getDateTime() {
        return this.by;
    }

    /**
     * Returns a string representation of the deadline task, including type, description, and due date.
     * @return Formatted string representing the task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(formatter) + ")";
    }
}

