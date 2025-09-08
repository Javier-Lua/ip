package command;

import exception.MiloException;
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
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.sort();
        return ui.showTaskSorted();
    }

    /**
     * Not applicable for this command.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return Not able to undo message.
     */
    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) {
        return ui.showError(new MiloException("Cannot undo 'sort' command."));
    }
}
