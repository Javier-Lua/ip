package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Represents an Event task with a description, start time, and end time.
 * Extends the {@code Task} class.
 */
public class Event extends Task {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an Event task with the specified description, start time, and end time.
     * @param description Description of the event.
     * @param from Start date and time of the event.
     * @param to End date and time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string array representation of the event for file storage.
     * @return File input representation of the event.
     */
    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "E";
        s[3] = this.from.toString();
        s[4] = this.to.toString();
        return s;
    }

    /**
     * Returns the start date and time of the event.
     * @return Start LocalDateTime of the event.
     */
    @Override
    public LocalDateTime getDateTime() {
        return this.from;
    }

    /**
     * Returns the end date and time of the event.
     * @return End LocalDateTime of the event.
     */
    public LocalDateTime getDateTimeTo() {
        return this.to;
    }

    /**
     * Returns a string representation of the event, including status, description,
     * and formatted start and end times.
     * @return Formatted string representing the event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(formatter) + " to: "
                + this.to.format(formatter) + ")";
    }
}
