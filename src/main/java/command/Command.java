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
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_SORT = "sort";
    private static final String COMMAND_RESET = "reset";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_SHOW = "show";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_FIND = "find";
    private static final String ERROR_COMMAND_NOT_FOUND = "Command not found!";
    private static final String ERROR_DEADLINE_REQUIRES_ONE_DATE = "Deadline command requires exactly one date.";
    private static final String ERROR_EVENT_REQUIRES_TWO_DATES = "Event command requires exactly two dates.";
    /**
     * Returns a command instance based on a keyword input
     * with no arguments.
     * @param input The user input keyword (e.g. "bye", "help", "sort" etc.).
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of(String input) throws MiloException {
        assert input != null : "Input string cannot be null";
        return switch (input) {
        case COMMAND_BYE -> new ExitCommand();
        case COMMAND_HELP -> new HelpCommand();
        case COMMAND_SORT -> new SortCommand();
        case COMMAND_RESET -> new ResetCommand();
        case COMMAND_LIST -> new ListCommand();
        default -> throw new MiloException(ERROR_COMMAND_NOT_FOUND);
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
        assert input != null : "Input string cannot be null";
        assert num > 0 : "Task index must be positive";
        return switch (input) {
        case COMMAND_MARK -> new MarkCommand(num);
        case COMMAND_UNMARK -> new UnmarkCommand(num);
        case COMMAND_DELETE -> new DeleteCommand(num);
        default -> throw new MiloException(ERROR_COMMAND_NOT_FOUND);
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
        assert input != null : "Input string cannot be null";
        assert date != null : "Date cannot be null";
        if (COMMAND_SHOW.equals(input)) {
            return new ShowCommand(date);
        }
        throw new MiloException(ERROR_COMMAND_NOT_FOUND);
    }
    /**
     * Returns a command instance based on a keyword input and a string argument.
     * @param input The user input keyword (currently only todo).
     * @param description The task description or additional information.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command.
     */
    public static Command of(String input, String description) throws MiloException {
        assert input != null : "Input string cannot be null";
        assert description != null : "Description cannot be null";
        assert !description.isEmpty() : "Description cannot be empty";
        if (COMMAND_TODO.equals(input)) {
            return new TodoCommand(description);
        }
        throw new MiloException(ERROR_COMMAND_NOT_FOUND);
    }
    /**
     * Returns a command instance based on a keyword input, a description, and one or more date-time arguments.
     * @param input The user input keyword (e.g. "deadline", "event").
     * @param description The task description.
     * @param dates One or more {@link LocalDateTime} values required by the command.
     *              For deadlines, one date is required. For events, a start and end date are required.
     * @return The corresponding {@code Command} instance.
     * @throws MiloException If the input does not match any valid command or if required dates are missing.
     */
    public static Command of(String input, String description, LocalDateTime... dates) throws MiloException {
        assert input != null : "Input string cannot be null";
        assert description != null : "Description cannot be null";
        assert !description.isEmpty() : "Description cannot be empty";
        return switch (input) {
        case COMMAND_DEADLINE -> createDeadlineCommand(description, dates);
        case COMMAND_EVENT -> createEventCommand(description, dates);
        default -> throw new MiloException(ERROR_COMMAND_NOT_FOUND);
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
        assert input != null : "Input string cannot be null";
        assert keywords != null : "Keywords array cannot be null";
        if (COMMAND_FIND.equals(input)) {
            String searchQuery = extractSearchQuery(keywords);
            return new FindCommand(searchQuery);
        }
        throw new MiloException(ERROR_COMMAND_NOT_FOUND);
    }

    /**
     * Creates a Deadline Command
     * @param description Description of deadline task
     * @param dates Due date for deadline task
     * @return Deadline command.
     * @throws MiloException If there is 0 or more than 1 date.
     */
    private static Command createDeadlineCommand(String description, LocalDateTime... dates) throws MiloException {
        if (dates.length != 1) {
            throw new MiloException(ERROR_DEADLINE_REQUIRES_ONE_DATE);
        }
        return new DeadlineCommand(description, dates[0]);
    }

    /**
     * Creates an Event Command
     * @param description Description of event task
     * @param dates Due dates for event task
     * @return Event command.
     * @throws MiloException If there is 0, 1 or more than 2 dates.
     */
    private static Command createEventCommand(String description, LocalDateTime... dates) throws MiloException {
        if (dates.length != 2) {
            throw new MiloException(ERROR_EVENT_REQUIRES_TWO_DATES);
        }
        return new EventCommand(description, dates[0], dates[1]);
    }

    /**
     * Returns the search query.
     * @param keywords Used to extract the query
     * @return String representing the search query.
     */
    private static String extractSearchQuery(String[] keywords) {
        String[] searchKeywords = Arrays.copyOfRange(keywords, 1, keywords.length);
        return String.join(" ", searchKeywords);
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
