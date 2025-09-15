package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.PatternSyntaxException;

import command.Command;
import command.CommandHistory;
import exception.MiloException;

/**
 * Provides parsing functionality for user input commands.
 * Converts string input into corresponding Command objects.
 */
public class Parser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final String ERROR_INVALID_COMMAND = "I'm sorry, I don't know what that means...";
    private static final String ERROR_INVALID_FIND = "Invalid find command.";
    private static final String ERROR_INVALID_SHOW = "Invalid show command. Use: show <yyyy-MM-dd>";
    private static final String ERROR_INVALID_MARK_UNMARK_DELETE = "Invalid mark/delete command. "
            + "Use: mark <number> or unmark <number> or delete <number>.";
    private static final String ERROR_NUMBER_OUT_OF_RANGE = "Number out of range! Task number does not exist.";
    private static final String ERROR_TODO_EMPTY_DESCRIPTION = "The description of a todo cannot be empty!";
    private static final String ERROR_TODO_FORMAT = "Invalid todo format! Use: todo <desc>.";
    private static final String ERROR_DEADLINE_EMPTY_DESCRIPTION = "The description of a deadline cannot be empty!";
    private static final String ERROR_DEADLINE_FORMAT = "Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd "
            + "HH:mm>";
    private static final String ERROR_EVENT_EMPTY_DESCRIPTION = "The description of an event cannot be empty!";
    private static final String ERROR_EVENT_FORMAT = "Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>"
            + " /to <yyyy-MM-dd HH:mm>";
    private static final String ERROR_GENERIC_PARSE = "I couldn't understand that command. Please check the format.";

    /**
     * Parses the given user input and returns the corresponding Command object.
     * Throws MiloException if the input is invalid or cannot be converted to a valid command.
     * @param input User input string to parse.
     * @return Corresponding Command object for the input.
     * @throws MiloException If the input is invalid or cannot be parsed.
     */
    public static Command parse(String input, CommandHistory commandHistory) throws MiloException {
        assert input != null : "Input string cannot be null";
        if (input == null || input.trim().isEmpty()) {
            throw new MiloException("Please enter a command. Type 'help' to see available commands.");
        }
        String trimmedInput = input.trim();
        String commandWord = getCommandWord(trimmedInput);
        try {
            return switch (commandWord) {
            case "find" -> parseFindCommand(trimmedInput);
            case "show" -> parseShowCommand(trimmedInput);
            case "mark", "unmark", "delete" -> parseMarkUnmarkDeleteCommand(trimmedInput);
            case "todo" -> parseTodoCommand(trimmedInput);
            case "deadline" -> parseDeadlineCommand(trimmedInput);
            case "event" -> parseEventCommand(trimmedInput);
            case "bye", "help", "sort", "reset", "list", "undo" -> Command.of(commandWord, commandHistory);
            default -> throw new MiloException(ERROR_INVALID_COMMAND);
            };
        } catch (MiloException e) {
            // Re-throw custom exceptions
            throw e;
        } catch (Exception e) {
            // Catch any unexpected exceptions and provide a user-friendly message
            throw new MiloException(ERROR_GENERIC_PARSE);
        }
    }

    /**
     * Extracts the command word from the input string.
     * @param input The user input string.
     * @return The first word of the input, converted to lowercase.
     */
    private static String getCommandWord(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        if (firstSpaceIndex == -1) {
            return input.toLowerCase();
        }
        return input.substring(0, firstSpaceIndex).toLowerCase();
    }

    /**
     * Parses a find command from user input.
     * @param input User input string starting with "find".
     * @return FindCommand with the search query.
     * @throws MiloException If the input is malformed or missing the search query.
     */
    private static Command parseFindCommand(String input) throws MiloException {
        try {
            String[] parts = input.split(" ", 2);
            validateArgumentCount(parts, ERROR_INVALID_FIND);
            return Command.of("find", parts);
        } catch (PatternSyntaxException e) {
            throw new MiloException(ERROR_INVALID_FIND);
        }
    }

    /**
     * Parses a show command from user input.
     * @param input User input string starting with "show"
     * @return ShowCommand with the specified date.
     * @throws MiloException If the input is malformed or contains an invalid date format.
     */
    private static Command parseShowCommand(String input) throws MiloException {
        String[] parts = input.split(" ");
        validateArgumentCount(parts, ERROR_INVALID_SHOW);
        try {
            LocalDate reqDate = LocalDate.parse(parts[1]);
            return Command.of("show", reqDate);
        } catch (DateTimeParseException e) {
            throw new MiloException(ERROR_INVALID_SHOW);
        }
    }

    /**
     * Parses mark, unmark or delete commands from user input.
     * @param input User input string starting with "mark", "unmark", or "delete".
     * @return Corresponding MarkCommand, UnmarkCommand, or DeleteCommand.
     * @throws MiloException If the input is malformed or contains an invalid task number.
     */
    private static Command parseMarkUnmarkDeleteCommand(String input) throws MiloException {
        String[] parts = input.split(" ");
        validateArgumentCount(parts, ERROR_INVALID_MARK_UNMARK_DELETE);
        try {
            int taskNumber = Integer.parseInt(parts[1]);
            validateTaskNumber(taskNumber);
            if (input.startsWith("mark")) {
                return Command.of("mark", taskNumber);
            } else if (input.startsWith("unmark")) {
                return Command.of("unmark", taskNumber);
            } else {
                return Command.of("delete", taskNumber);
            }
        } catch (NumberFormatException e) {
            throw new MiloException(ERROR_INVALID_MARK_UNMARK_DELETE);
        }
    }

    /**
     * Validates that a task number is positive.
     * @param taskNumber The task number to validate.
     * @throws MiloException If the task number is not positive.
     */
    private static void validateTaskNumber(int taskNumber) throws MiloException {
        if (taskNumber <= 0) {
            throw new MiloException(ERROR_NUMBER_OUT_OF_RANGE);
        }
    }

    /**
     * Parses a todo command from user input.
     * @param input User input string starting with "todo".
     * @return TodoCommand with the task description.
     * @throws MiloException If the input is malformed or missing the task description.
     */
    private static Command parseTodoCommand(String input) throws MiloException {
        validateInputLength(input, 5, ERROR_TODO_EMPTY_DESCRIPTION);
        validateFormat(input.charAt(4) == ' ', ERROR_TODO_FORMAT);
        String description = input.substring(5).trim();
        return Command.of("todo", description);
    }

    /**
     * Parses a deadline command from user input.
     * @param input User input string starting with "deadline".
     * @return DeadlineCommand with the task description and deadline.
     * @throws MiloException If the input is malformed, missing required parts, or contains an invalid date format.
     */
    private static Command parseDeadlineCommand(String input) throws MiloException {
        validateInputLength(input, 9, ERROR_DEADLINE_EMPTY_DESCRIPTION);
        String content = input.substring(9);
        String[] parts = content.split("/by");
        validateArgumentCount(parts, ERROR_DEADLINE_FORMAT);
        try {
            LocalDateTime deadline = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
            return Command.of("deadline", parts[0].trim(), deadline);
        } catch (DateTimeParseException e) {
            throw new MiloException(ERROR_DEADLINE_FORMAT);
        }
    }

    /**
     * Parses an event command from user input.
     * @param input User input string starting with "event".
     * @return EventCommand with the task description, start time, and end time.
     * @throws MiloException If the input is malformed, missing required parts, or contains invalid date formats.
     */
    private static Command parseEventCommand(String input) throws MiloException {
        validateInputLength(input, 6, ERROR_EVENT_EMPTY_DESCRIPTION);
        String content = input.substring(6);
        int fromIndex = content.indexOf("/from");
        int toIndex = content.indexOf("/to");
        validateFormat(fromIndex != -1 && toIndex != -1, ERROR_EVENT_FORMAT);
        try {
            String dateTimeFrom = content.substring(fromIndex + 5, toIndex).trim();
            String dateTimeTo = content.substring(toIndex + 3).trim();
            LocalDateTime eventFrom = LocalDateTime.parse(dateTimeFrom, DATE_TIME_FORMATTER);
            LocalDateTime eventTo = LocalDateTime.parse(dateTimeTo, DATE_TIME_FORMATTER);
            return Command.of("event", content.substring(0, fromIndex).trim(), eventFrom, eventTo);
        } catch (DateTimeParseException e) {
            throw new MiloException(ERROR_EVENT_FORMAT);
        }
    }

    /**
     * Validates that an array has at least the expected number of elements.
     *
     * @param parts        The array to validate.
     * @param errorMessage The error message to use if validation fails.
     * @throws MiloException If the array has fewer elements than expected.
     */
    private static void validateArgumentCount(String[] parts, String errorMessage) throws MiloException {
        if (parts.length < 2) {
            throw new MiloException(errorMessage);
        }
    }

    /**
     * Validates that a string has at least the minimum required length.
     * @param input The string to validate.
     * @param minLength The minimum required length.
     * @param errorMessage The error message to use if validation fails.
     * @throws MiloException If the string is shorter than the minimum required length.
     */
    private static void validateInputLength(String input, int minLength, String errorMessage) throws MiloException {
        if (input.length() <= minLength) {
            throw new MiloException(errorMessage);
        }
    }

    /**
     * Validates a condition and throws an exception if it fails.
     * @param condition The condition to validate.
     * @param errorMessage The error message to use if validation fails.
     * @throws MiloException If the condition is false.
     */
    private static void validateFormat(boolean condition, String errorMessage) throws MiloException {
        if (!condition) {
            throw new MiloException(errorMessage);
        }
    }
}
