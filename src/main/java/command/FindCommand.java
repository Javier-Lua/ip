package command;

import exception.MiloException;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to find tasks that contain a specified keyword or phrase.
 * The search is performed on the descriptions of all tasks in the task list.
 */
public class FindCommand extends Command {
    private final String misc;

    /**
     * Constructs a {@code FindCommand} with the specified search keyword or phrase.
     * @param misc Keyword or phrase to search for in task description.
     */
    public FindCommand(String misc) {
        this.misc = misc;
    }

    /**
     * Executes the find command by filtering tasks in the task list
     * that contain the specified keyword and displaying the results
     * via the UI.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return ui.showTaskSearched(tasks.filter(misc));
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
        return ui.showError(new MiloException("Cannot undo 'find' command."));
    }
}
