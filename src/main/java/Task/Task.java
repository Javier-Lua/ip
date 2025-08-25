package Task;
import enums.*;

public abstract class Task {
    private final String description;
    private Status status;
    private final int tag;
    private static int count = 0;
    private final TaskType type;
    private final String[] fileInput;

    public Task(String description, TaskType type) {
        this.description = description;
        this.status = Status.NOT_DONE;
        Task.count += 1;
        this.tag = Task.count;
        this.type = type;
        this.fileInput = new String[] {"", "0", this.description, "", ""};
    }

    public static Task makeTask(TaskType type, String desc, String... dates) {
        switch(type) {
        case TODO:
            return new Todo(desc);
        case DEADLINE:
            if (dates.length != 1) throw new IllegalArgumentException("Deadline needs one date!");
            return new Deadline(desc, dates[0]);
        case EVENT:
            if (dates.length != 2) throw new IllegalArgumentException("Event needs two dates!");
            return new Event(desc, dates[0], dates[1]);
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

    public int getTag() {
        return this.tag;
    }

    public String[] getFileInput() {
        return this.fileInput;
    }

    public void delete() {
        Task.count -= 1;
    }

    public TaskType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }
}
