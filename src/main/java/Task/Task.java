package Task;
import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;

public abstract class Task {
    private final String description;
    private Status status;
    private static int count = 0;
    private final String[] fileInput;

    public Task(String description) {
        this.description = description;
        this.status = Status.NOT_DONE;
        Task.count += 1;
        this.fileInput = new String[] {"", "0", this.description, "", ""};
    }

    public static Task makeTask(TaskType type, String desc, String... dates) {
        switch(type) {
        case TODO:
            return new Todo(desc);
        case DEADLINE:
            if (dates.length != 1) throw new IllegalArgumentException("Deadline needs one date!");
            return new Deadline(desc, LocalDateTime.parse(dates[0]));
        case EVENT:
            if (dates.length != 2) throw new IllegalArgumentException("Event needs two dates!");
            return new Event(desc, LocalDateTime.parse(dates[0]), LocalDateTime.parse(dates[1]));
        default:
            throw new IllegalArgumentException("Unknown Task Type: " + type);
        }
    }

    public static int getCount() {
        return Task.count;
    }

    public String getStatusIcon() {
        if (this.status == Status.DONE) {
            return "X"; // mark done task with X
        } else {
            return " ";
        }
    }

    public void markAsDone() {
        this.status = Status.DONE;
        this.fileInput[1] = "1";
    }

    public void markAsUndone() {
        this.status = Status.NOT_DONE;
        this.fileInput[1] = "0";
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getFileInput() {
        return this.fileInput;
    }

    public void delete() {
        Task.count -= 1;
    }

    public static void resetCount() {
        Task.count = 0;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }
}
