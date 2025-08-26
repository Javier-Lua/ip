package command;

import model.TaskList;
import storage.Storage;
import ui.Ui;

public class FindCommand extends Command {
    private final String misc;

    public FindCommand(String misc) {
        this.misc = misc;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskSearched(tasks.filter(misc));
    }
}
