package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;

import java.time.LocalDate;

/**
 * Represents a command to show tasks for a specific date.
 * Filters tasks based on the provided date.
 */
public class ShowCommand extends Command {

    private final LocalDate date;

    /**
     * Constructs a {@code ShowCommand} with the specified date.
     * @param date the date to filter tasks by.
     */
    public ShowCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Executes the show command by filtering tasks for the specified date
     * and displaying them to the user.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        TaskList res = tasks.filter(date);
        ui.showList(res, date);
    }
}
