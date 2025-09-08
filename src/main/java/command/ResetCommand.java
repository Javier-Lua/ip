package command;

import model.Task;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to reset the task list.
 * Clears all tasks from the task list and storage.
 */
public class ResetCommand extends Command {

    private TaskList originalTasks;

    /**
     * Executes the reset command by clearing all tasks
     * from the task list and storage. Notifies the user
     * that the task list has been cleared.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        this.originalTasks = new TaskList();
        for (int i = 0; i < tasks.getCount(); i++) {
            Task originalTask = tasks.getTask(i);
            this.originalTasks.add(originalTask);
        }
        storage.clearFile();
        tasks.clear();
        return ui.showTaskCleared();
    }

    /**
     * Undoes the reset.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     * @return String for adding back tasks.
     */
    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) {
        tasks.clear();
        for (int i = 0; i < originalTasks.getCount(); i++) {
            tasks.add(originalTasks.getTask(i));
        }
        storage.saveTasks();
        return ui.showTaskAllAdded();
    }
}
