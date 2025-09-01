package command;

import java.time.LocalDate;

import model.TaskList;
import storage.Storage;
import ui.Ui;

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
     * @return String output message to the user after executing command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        TaskList res = tasks.filter(date);
        return ui.showList(res, date);
    }
}
