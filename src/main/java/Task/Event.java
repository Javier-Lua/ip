package Task;
import enums.*;

public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "E";
        s[3] = this.from;
        s[4] = this.to;
        return s;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}
