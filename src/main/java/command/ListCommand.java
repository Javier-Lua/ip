package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;

/**
 * Represents a command to list all tasks in the task list.
 * Displays all tasks to the user when executed.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by displaying all tasks
     * currently stored in the task list.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks, null);
    }
}
