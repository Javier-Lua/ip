package command;

import exception.MiloException;
import model.Task;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to delete a task from the task list.
 * The task is removed based on its index in the list.
 */
public class DeleteCommand extends Command {

    private final int num;
    private Task deletedTask;

    /**
     * Constructs a {@code DeleteCommand} with the specified task index.
     * @param num The index of the task to be deleted.
     */
    public DeleteCommand(int num) {
        this.num = num;
    }

    /**
     * Executes the command by deleting the task at the specified index,
     * updating the storage, and notifying the user through the UI.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            deletedTask = tasks.getTask(num - 1);
            tasks.delete(num - 1);
            storage.saveTasks();
            return ui.showTaskRemoved(deletedTask, tasks.getCount());
        } else {
            return ui.showError(new MiloException("Task number out of range!"));
        }
    }

    /**
     * Adds back the deleted task
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return The String output message of task added.
     */
    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            int index = num - 1;
            tasks.addAtIndex(deletedTask, index);
            storage.saveTasks();
            return ui.showAddTask(deletedTask, tasks.getCount());
        } else {
            return ui.showError(new MiloException("Task number out of range to undo!"));
        }
    }
}
