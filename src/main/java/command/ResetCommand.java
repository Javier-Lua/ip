package command;

import logic.Storage;
import logic.TaskList;
import logic.Ui;

public class ResetCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        storage.clearFile();
        tasks.clear();
        ui.showTaskCleared();
    }
}
