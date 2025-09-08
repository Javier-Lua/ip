package command;

import exception.MiloException;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to display help information.
 * Shows instructions to the user when executed.
 */
public class HelpCommand extends Command {

    /**
     * Executes the help command by displaying instructions
     * and commands to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return ui.showHelp();
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
        return ui.showError(new MiloException("Cannot undo 'help' command."));
    }
}
