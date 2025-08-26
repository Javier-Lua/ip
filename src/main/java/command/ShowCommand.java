package command;

import java.time.LocalDate;

import model.TaskList;
import storage.Storage;
import ui.Ui;

public class ShowCommand extends Command {

    private final LocalDate date;

    public ShowCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        TaskList res = tasks.filter(date);
        ui.showList(res, date);
    }
}
