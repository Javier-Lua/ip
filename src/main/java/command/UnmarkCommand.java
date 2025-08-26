package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to unmark a task as incomplete in teh task list.
 */
public class UnmarkCommand extends Command {
    private final int num;

    /**
     * Constructs an {@code UnmarkCommand} for the specified task index.
     * @param num the index of the task to unmark (1-based).
     */
    public UnmarkCommand(int num) {
        this.num = num;
    }

    /**
     * Executes the unmark command by marking the specified task as incomplete,
     * saving the updated task list, and displaying a confirmation message to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            tasks.unmark(num - 1);
            ui.showTaskUnmarked(tasks.getTask(num - 1));
            storage.saveTasks();
        }
    }
}
