package command;

import exception.MiloException;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to mark a task as done in the task list.
 * The task is identified by its index in the list.
 */
public class MarkCommand extends Command {
    private final int num;

    /**
     * Constructs a {@code MarkCommand} with the specified task index.
     * @param num Index of the task to mark as done.
     */
    public MarkCommand(int num) {
        this.num = num;
    }

    /**
     * Executes the mark command by marking the specified task
     * as done and updating the storage. The task's updated status
     * is displayed to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            tasks.mark(num - 1);
            storage.saveTasks();
            return ui.showTaskMarked(tasks.getTask(num - 1));
        } else {
            return ui.showError(new MiloException("Task number out of range!"));
        }
    }

    /**
     * Unmarks the task.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String for unmarking task.
     */
    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            tasks.unmark(num - 1);
            storage.saveTasks();
            return ui.showTaskUnmarked(tasks.getTask(num - 1));
        } else {
            return ui.showError(new MiloException("Task number out of range to undo!"));
        }
    }
}
