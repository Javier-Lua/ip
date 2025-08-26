package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

public class MarkCommand extends Command {
    private final int num;

    public MarkCommand(int num) {
        this.num = num;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (num <= tasks.getCount()) {
            tasks.mark(num - 1);
            ui.showTaskMarked(tasks.getTask(num - 1));
            storage.saveTasks();
        }
    }
}
