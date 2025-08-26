package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks, null);
    }
}
