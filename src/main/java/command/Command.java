package command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import exception.MiloException;
import model.TaskList;
import storage.Storage;
import ui.Ui;

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
    public static Command of(String input) throws MiloException {
        return switch (input) {
        case "bye" -> new ExitCommand();
        case "help" -> new HelpCommand();
        case "sort" -> new SortCommand();
        case "reset" -> new ResetCommand();
        case "list" -> new ListCommand();
        default -> throw new MiloException("Command not found!");
        };
    }
    /**
     * Returns a command instance based on a keyword input and a task index.
     * @param input The user input keyword.
     * @param num The index of the task to be affected.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of(String input, int num) throws MiloException {
        return switch (input) {
        case "mark" -> new MarkCommand(num);
        case "unmark" -> new UnmarkCommand(num);
        case "delete" -> new DeleteCommand(num);
        default -> throw new MiloException("Command not found!");
        };
    }
    /**
     * Returns a command instance based on a keyword input and a date.
     * @param input The user input keyword
     * @param date The date used to filter tasks.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of(String input, LocalDate date) throws MiloException {
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
    public static Command of(String input, String misc) throws MiloException {
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
    public static Command of(String input, String misc, LocalDateTime... dates) throws MiloException {
        return switch (input) {
        case "deadline" -> new DeadlineCommand(misc, dates[0]);
        case "event" -> new EventCommand(misc, dates[0], dates[1]);
        default -> throw new MiloException("Command not found!");
        };
    }

    /**
     * Returns a Command based on the input string and additional keywords.
     * If the input is "find", constructs a {@code FindCommand} using the
     * keywords from the second element onwards.
     * @param input Input command string.
     * @param keywords Array of keywords related to the command.
     * @return Command corresponding to the input and keywords.
     * @throws MiloException If the input command is not recognized.
     */
    public static Command of(String input, String[] keywords) throws MiloException {
        if (input.equals("find")) {
            keywords = Arrays.copyOfRange(keywords, 1, keywords.length);
            return new FindCommand(String.join(" ", keywords));
        }
        throw new MiloException("Command not found!");
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
     * @return String output message to the user after executing command.
     */
    public abstract String execute(TaskList tasks, Ui ui, Storage storage);
}
