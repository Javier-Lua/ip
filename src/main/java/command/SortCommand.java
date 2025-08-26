package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;

public class SortCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.sort();
        ui.showTaskSorted();
    }
}
