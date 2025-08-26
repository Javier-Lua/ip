package command;

import exception.MiloException;
import storage.Storage;
import model.TaskList;
import ui.Ui;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an abstract command in the Milo application.
 * Concrete commands are created using the factory {@code of()} methods
 * based on user input and executed with the {@code execute()} method.
 */
public abstract class Command {

    /**
     * Returns a command instance based on a keyword input
     * with no arguments.
     * @param input The user input keyword (e.g. "bye", "help", "sort" etc.).
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of (String input) throws MiloException {
        switch (input) {
        case "bye": return new ExitCommand();
        case "help": return new HelpCommand();
        case "sort": return new SortCommand();
        case "reset": return new ResetCommand();
        case "list": return new ListCommand();
        default: throw new MiloException("Command not found!");
        }
    }

    /**
     * Returns a command instance based on a keyword input and a task index.
     * @param input The user input keyword.
     * @param num The index of the task to be affected.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of (String input, int num) throws MiloException {
        switch (input) {
        case "mark": return new MarkCommand(num);
        case "unmark": return new UnmarkCommand(num);
        case "delete": return new DeleteCommand(num);
        default: throw new MiloException("Command not found!");
        }
    }

    /**
     * Returns a command instance based on a keyword input and a date.
     * @param input The user input keyword
     * @param date The date used to filter tasks.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of (String input, LocalDate date) throws MiloException {
        if (input.equals("show")) {
            return new ShowCommand(date);
        }
        throw new MiloException("Command not found!");
    }

    /**
     * Returns a command instance based on a keyword input and a string argument.
     * @param input The user input keyword (currently only todo).
     * @param misc The task description or additional information.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of (String input, String misc) throws MiloException {
        if (input.equals("todo")) {
            return new TodoCommand(misc);
        }
        throw new MiloException("Command not found!");
    }

    /**
     * Returns a command instance based on a keyword input, a description, and one or more date-time arguments.
     * @param input The user input keyword (e.g. "deadline", "event").
     * @param misc The task description.
     * @param dates One or more {@link LocalDateTime} values required by the command.
     *              For deadlines, one date is required. For events, a start and end date are required.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command or if required dates are missing.
     */
    public static Command of (String input, String misc, LocalDateTime... dates) throws MiloException {
        switch (input) {
        case "deadline": return new DeadlineCommand(misc, dates[0]);
        case "event": return new EventCommand(misc, dates[0], dates[1]);
        default: throw new MiloException("Command not found!");
        }
    }

    /**
     * Checks if this command is an exit command.
     * @return {@code true} if the command is an instance of {@code ExitCommand}, otherwise {@code false}.
     */
    public boolean isExit() {
        return this instanceof ExitCommand;
    }

    /**
     * Executes the command using the given task list, user interface, and storage.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying results.
     * @param storage The storage handler for saving changes.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);
}
