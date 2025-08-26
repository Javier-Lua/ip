package command;

import logic.Storage;
import logic.TaskList;
import logic.Ui;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks, null);
    }
}
