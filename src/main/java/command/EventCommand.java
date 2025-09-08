package command;

import java.time.LocalDateTime;

import model.Event;
import model.Task;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to add an Event task to the task list.
 * The event includes a description, a start date and time, and an end date and time.
 */
public class EventCommand extends Command {
    private final Task t;
    /**
     * Constructs an EventCommand with the specified description,
     * start date and time, and end date and time.
     * @param desc the description of the event.
     * @param from the starting date and time of the event.
     * @param to the ending date and time of the event.
     */
    public EventCommand(String desc, LocalDateTime from, LocalDateTime to) {
        this.t = new Event(desc, from, to);
    }

    /**
     * Executes the event command by creating a new {@link Event} task,
     * adding it to the task list, saving the updated task list to storage,
     * and displaying confirmation to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(t);
        storage.saveTasks();
        return ui.showAddTask(t, tasks.getCount());
    }

    /**
     * Removes added event task.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return Task deleted message.
     */
    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) {
        tasks.remove(t);
        storage.saveTasks();
        return ui.showTaskRemoved(t, tasks.getCount());
    }
}
