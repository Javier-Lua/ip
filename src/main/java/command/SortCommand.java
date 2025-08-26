package command;

import logic.Storage;
import logic.TaskList;
import logic.Ui;

public class SortCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.sort();
        ui.showTaskSorted();
    }
}
