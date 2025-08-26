package model;

/**
 * Represents a Todo task with only a description and no dates.
 * Extends the {@code Task} class.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task with the specified description.
     * @param description Description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string array representation of the todo task for file storage.
     * @return File input representation of the todo task.
     */
    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "T";
        return s;
    }

    /**
     * Returns a string representation of the todo task, including its status and description.
     * @return Formatted string representing the todo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
