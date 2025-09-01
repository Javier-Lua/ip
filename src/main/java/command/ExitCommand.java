package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to exit the application.
 * Displays a goodbye message to the user when executed.
 */
public class ExitCommand extends Command {

    /**
     * Executes the exit command by displaying a goodbye message
     * to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return ui.showGoodbye();
    }
}
