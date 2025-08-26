package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Deadline extends Task {
    private final static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return this.by;
    }

    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "D";
        s[3] = this.by.toString();
        return s;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(formatter) + ")";
    }
}

