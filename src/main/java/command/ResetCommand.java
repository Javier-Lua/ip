package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;

public class ResetCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        storage.clearFile();
        tasks.clear();
        ui.showTaskCleared();
    }
}
