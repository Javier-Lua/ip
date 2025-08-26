package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import enums.Status;
import enums.TaskType;
import exception.MiloException;

public abstract class Task {
    private final String description;
    private Status status;
    private final String[] fileInput;

    public Task(String description) {
        this.description = description;
        this.status = Status.NOT_DONE;
        this.fileInput = new String[] {"", "0", this.description, "", ""};
    }

    public static Task makeTask(TaskType type, String desc, String... dates) throws MiloException {
        if (desc == null || desc.isBlank()) {
            throw new MiloException("Task description cannot be empty");
        }
        switch(type) {
        case TODO:
            return new Todo(desc);
        case DEADLINE:
            if (dates.length != 1) {
                throw new MiloException("Deadline needs one date!");
            }
            try {
                return new Deadline(desc, LocalDateTime.parse(dates[0]));
            } catch (DateTimeParseException e) {
                throw new MiloException("Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd " + "HH:mm>");
            }
        case EVENT:
            if (dates.length != 2) {
                throw new MiloException("Event needs two dates!");
            }
            try {
                return new Event(desc, LocalDateTime.parse(dates[0]), LocalDateTime.parse(dates[1]));
            } catch (DateTimeParseException e) {
                throw new MiloException("Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>"
                        + " /to <yyyy-MM-dd HH:mm>");
            }
        default:
            throw new MiloException("Unknown Task Type: " + type);
        }
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

    public boolean isDone() {
        return this.status == Status.DONE;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getFileInput() {
        return this.fileInput;
    }

    public LocalDateTime getDateTime() {
        return null;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }
}
