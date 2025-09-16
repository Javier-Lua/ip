package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import enums.Status;
import enums.TaskType;
import exception.RotomException;

/**
 * Represents a general task with a description and completion status.
 * Can be extended for specific task types such as {@code Deadline}, {@code Event} or {@code Todo}.
 */
public abstract class Task {
    private final String description;
    private Status status;
    private final String[] fileInput;

    /**
     * Constructs a {@code Task} with the specified description.
     * Initially, the task is marked as not done.
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.status = Status.NOT_DONE;
        this.fileInput = new String[] {"", "0", this.description, "", ""};
    }

    /**
     * Creates a {@code Task} of the specified type with a description and optional dates.
     * @param type Type of the task ({@code DEADLINE}, {@code EVENT}, {@code TODO}.
     * @param desc Description of the task.
     * @param dates Optional date(s) depending on the task type.
     * @return Constructed {@code Task} object.
     * @throws RotomException If task type is unknown, description is empty, or dates are invalid.
     */
    public static Task makeTask(TaskType type, String desc, String... dates) throws RotomException {
        if (desc == null || desc.isBlank()) {
            throw new RotomException("Task description cannot be empty");
        }
        switch(type) {
        case TODO:
            return new Todo(desc);
        case DEADLINE:
            if (dates.length != 1) {
                throw new RotomException("Deadline needs one date!");
            }
            try {
                return new Deadline(desc, LocalDateTime.parse(dates[0]));
            } catch (DateTimeParseException e) {
                throw new RotomException("Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd " + "HH:mm>");
            }
        case EVENT:
            if (dates.length != 2) {
                throw new RotomException("Event needs two dates!");
            }
            try {
                return new Event(desc, LocalDateTime.parse(dates[0]), LocalDateTime.parse(dates[1]));
            } catch (DateTimeParseException e) {
                throw new RotomException("Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>"
                        + " /to <yyyy-MM-dd HH:mm>");
            }
        default:
            throw new RotomException("Unknown Task Type: " + type);
        }
    }

    /**
     * Returns a string representing the task's completion status icon.
     * @return "X" if done, otherwise a blank space.
     */
    public String getStatusIcon() {
        if (this.status == Status.DONE) {
            return "X"; // mark done task with X
        } else {
            return " ";
        }
    }

    /**
     * Marks the task as done and updates its file input representation.
     */
    public void markAsDone() {
        this.status = Status.DONE;
        this.fileInput[1] = "1";
    }

    /**
     * Marks the task as not done and updates its file input representation.
     */
    public void markAsUndone() {
        this.status = Status.NOT_DONE;
        this.fileInput[1] = "0";
    }

    /**
     * Checks if the task is done.
     * @return true if the task is marked as done, false otherwise.
     */
    public boolean isDone() {
        return this.status == Status.DONE;
    }

    /**
     * Returns the description of the task.
     * @return Task description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the string array representation of the task for file storage.
     * @return File input representation of the task.
     */
    public String[] getFileInput() {
        return this.fileInput;
    }

    /**
     * Returns the main date and time associated with the task.
     * Can be overridden by subclasses like {@code Deadline} or {@code Event}.
     * @return LocalDateTime of the task, or null if not applicable.
     */
    public LocalDateTime getDateTime() {
        return null;
    }

    /**
     * Returns a string representation of the task, including status and description.
     * @return Formatted string representing the task.
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }
}
