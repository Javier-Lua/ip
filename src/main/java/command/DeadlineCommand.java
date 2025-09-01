package command;

import java.time.LocalDateTime;

import model.Deadline;
import model.Task;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command that adds a {@link Deadline} task
 * to the task list.
 */
public class DeadlineCommand extends Command {

    private final String desc;
    private final LocalDateTime date;

    /**
     * Constructs a {@code DeadlineCommand} with the given description
     * and due date.
     * @param desc Description of the deadline task.
     * @param date Due date and time of the deadline task.
     */
    public DeadlineCommand(String desc, LocalDateTime date) {
        this.desc = desc;
        this.date = date;
    }

    /**
     * Executes the command by creating a {@link Deadline} task,
     * adding it to the given task list, saving the task list to
     * storage, and showing the added task via the UI.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Deadline(this.desc, this.date);
        tasks.add(t);
        storage.saveTasks();
        return ui.showAddTask(t, tasks.getCount());
    }

}
