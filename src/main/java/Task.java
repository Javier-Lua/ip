public class Task {
    protected String description;
    protected boolean isDone;
    protected int tag;
    protected static int count = 0;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        Task.count += 1;
        this.tag = Task.count;
    }

    public static int getCount() {
        return Task.count;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public int getTag() {
        return this.tag;
    }
}

