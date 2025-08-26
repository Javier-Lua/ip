package command;

import model.Task;
import model.TaskList;
import model.Todo;
import storage.Storage;
import ui.Ui;

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
