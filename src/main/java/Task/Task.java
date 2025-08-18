package Task;
import enums.*;

public abstract class Task {
    private final String description;
    private Status status;
    private final int tag;
    private static int count = 0;
    private final TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.status = Status.NOT_DONE;
        Task.count += 1;
        this.tag = Task.count;
        this.type = type;
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
    }

    public void markAsUndone() {
        this.status = Status.NOT_DONE;
    }

    public String getDescription() {
        return this.description;
    }

    public int getTag() {
        return this.tag;
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
