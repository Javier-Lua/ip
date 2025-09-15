package command;

import exception.MiloException;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to undo the last executed command.
 */
public class UndoCommand extends Command {
    private final CommandHistory history;

    public UndoCommand(CommandHistory history) {
        this.history = history;
    }
    /**
     * Undoes the most recent command.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return The string message opposite of the previous command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Command lastCommand = history.pop();
        if (lastCommand != null) {
            return lastCommand.undo(tasks, ui, storage);
        } else {
            return ui.showError(new MiloException("Nothing to undo."));
        }
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
        return ui.showError(new MiloException("Cannot undo 'undo' command."));
    }
}
