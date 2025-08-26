package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;
import model.Task;

/**
 * Represents a command to delete a task from the task list.
 * The task is removed based on its index in the list.
 */
public class DeleteCommand extends Command {

    private final int num;

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
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            Task temp = tasks.getTask(num - 1);
            tasks.delete(num - 1);
            ui.showTaskRemoved(temp, tasks.getCount());
            storage.saveTasks();
        }
    }
}
