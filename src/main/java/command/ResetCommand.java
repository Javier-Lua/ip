package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

public class ResetCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        storage.clearFile();
        tasks.clear();
        ui.showTaskCleared();
    }
}
