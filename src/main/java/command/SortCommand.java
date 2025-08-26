package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to sort the tasks in the task list.
 * Tasks are sorted according to their chronological order.
 */
public class SortCommand extends Command {

    /**
     * Executes the sort command by sorting the task list
     * and displaying a confirmation message to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.sort();
        ui.showTaskSorted();
    }
}
