package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;

public class UnmarkCommand extends Command {
    private final int num;

    public UnmarkCommand(int num) {
        this.num = num;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            tasks.unmark(num - 1);
            ui.showTaskUnmarked(tasks.getTask(num - 1));
            storage.saveTasks();
        }
    }
}
