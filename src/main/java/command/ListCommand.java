package command;

import exception.RotomException;
import model.TaskList;
import storage.Storage;
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
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return ui.showList(tasks, null);
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
        return ui.showError(new RotomException("Cannot undo 'list' command."));
    }
}
