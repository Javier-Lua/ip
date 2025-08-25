package Task;
import enums.*;

public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "D";
        s[3] = this.by;
        return s;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }
}

