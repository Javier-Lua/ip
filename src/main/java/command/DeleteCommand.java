package command;

import logic.Storage;
import logic.TaskList;
import logic.Ui;
import task.Task;

public class DeleteCommand extends Command {

    private final int num;

    public DeleteCommand(int num) {
        this.num = num;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            Task temp = tasks.getTask(num - 1);
            tasks.delete(num - 1);
            ui.showTaskRemoved(temp, tasks.getCount());
            storage.saveTasks();
        }
    }
}
