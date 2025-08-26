package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks, null);
    }
}
