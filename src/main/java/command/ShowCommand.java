package command;

import logic.Storage;
import logic.TaskList;
import logic.Ui;

import java.time.LocalDate;

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
