package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to reset the task list.
 * Clears all tasks from the task list and storage.
 */
public class ResetCommand extends Command {

    /**
     * Executes the reset command by clearing all tasks
     * from the task list and storage. Notifies the user
     * that the task list has been cleared.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        storage.clearFile();
        tasks.clear();
        ui.showTaskCleared();
    }
}
