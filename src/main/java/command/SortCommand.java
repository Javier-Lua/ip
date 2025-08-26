package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

public class SortCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.sort();
        ui.showTaskSorted();
    }
}
