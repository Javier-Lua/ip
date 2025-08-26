package command;

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
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showHelp();
    }
}
