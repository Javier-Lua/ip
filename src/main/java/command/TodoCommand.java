package command;

import logic.Storage;
import logic.TaskList;
import logic.Ui;
import task.Task;
import task.Todo;

public class TodoCommand extends Command {
    private final String misc;

    public TodoCommand(String misc) {
        this.misc = misc;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Todo(misc);
        tasks.add(t);
        storage.saveTasks();
        ui.showAddTask(t, tasks.getCount());
    }
}
