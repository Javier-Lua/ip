package command;

import model.Task;
import model.TaskList;
import model.Todo;
import storage.Storage;
import ui.Ui;

/**
 * Represents a command to add a Todo task to the task list.
 */
public class TodoCommand extends Command {
    private final String misc;

    /**
     * Constructs a {@code TodoCommand} with the specified task description
     * @param misc the description of the todo task.
     */
    public TodoCommand(String misc) {
        this.misc = misc;
    }

    /**
     * Executes the TODO command by creating a new TODO task,
     * adding it to the task list, saving the task list, and
     * displaying a confirmation message to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Todo(misc);
        tasks.add(t);
        storage.saveTasks();
        ui.showAddTask(t, tasks.getCount());
    }
}
