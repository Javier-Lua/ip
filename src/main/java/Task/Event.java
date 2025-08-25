package Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Event extends Task {
    private final static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "E";
        s[3] = this.from.toString();
        s[4] = this.to.toString();
        return s;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(formatter) + " to: "
                + this.to.format(formatter) + ")";
    }
}
