package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import command.Command;
import exception.MiloException;

public class Parser {

    private static final DateTimeFormatter dtFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public static Command parse(String input) throws MiloException {
        if (input.equals("bye") || input.equals("help") || input.equals("sort") ||
                input.equals("reset") || input.equals("list")) {
            return Command.of(input);
        } else if (input.startsWith("show")) {
            String[] temp = input.split(" ");
            if (temp.length != 2) {
                throw new MiloException("Invalid show command. Use: show <yyyy-MM-dd>");
            } else {
                try {
                    LocalDate reqDate = LocalDate.parse(temp[1]);
                    return Command.of("show", reqDate);
                } catch (DateTimeParseException e) {
                    throw new MiloException("Invalid show command. Use: show <yyyy-MM-dd>");
                }
            }
        } else if (input.startsWith("mark") || input.startsWith("unmark") || input.startsWith("delete")) {
            String[] parts = input.split(" "); // split when encounter spacing
            if (!input.contains(" ")) {
                throw new MiloException("Invalid mark/delete command. Use: mark <number> or unmark <number> "
                        + "or delete <number>.");
            }
            if (parts.length == 2) {
                try {
                    int num = Integer.parseInt(parts[1]);
                    if (/*num <= tasks.getCount()*/num > 0) {
                        if (input.startsWith("mark")) {
                            return Command.of("mark", num);
                        } else if (input.startsWith("unmark")) {
                            return Command.of("unmark", num);
                        } else {
                            return Command.of("delete", num);
                        }
                    } else {
                        throw new MiloException("Number out of range! Task number does not exist.");
                    }
                } catch (NumberFormatException e) {
                    throw new MiloException("Invalid mark/delete command. Use: mark <number> or unmark "
                            + "<number> or delete <number>.");
                }
            }
        } else {
            if (input.startsWith("todo")) {
                if (input.length() <= 5) {
                    throw new MiloException("The description of a todo cannot be empty!");
                }
                if (input.charAt(4) != ' ') {
                    throw new MiloException("Invalid todo format! Use: todo <desc>.");
                }
                return Command.of("todo", input.substring(5).trim());
            } else if (input.startsWith("deadline")) {
                if (input.length() == 8) {
                    throw new MiloException("The description of a deadline cannot be empty!");
                }
                String temp = input.substring(9);
                String[] parts = temp.split("/by");
                if (parts.length < 2) {
                    throw new MiloException("Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd "
                            + "HH:mm>");
                }
                String dateTime = parts[1].trim();
                try {
                    LocalDateTime deadline = LocalDateTime.parse(dateTime, dtFormatter);
                    return Command.of("deadline", parts[0].trim(), deadline);
                } catch (DateTimeParseException e) {
                    throw new MiloException("Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd "
                            + "HH:mm>");
                }
            } else if (input.startsWith("event")) {
                if (input.length() == 5) {
                    throw new MiloException("The description of an event cannot be empty!");
                }
                String temp = input.substring(6);
                int fromInd = temp.indexOf("/from");
                int toInd = temp.indexOf("/to");
                if (fromInd == -1 || toInd == -1) {
                    throw new MiloException("Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>"
                            + " /to <yyyy-MM-dd HH:mm>");
                }
                String dateTimeFrom = temp.substring(fromInd + 5, toInd).trim();
                String dateTimeTo = temp.substring(toInd + 3).trim();
                try {
                    LocalDateTime eventFrom = LocalDateTime.parse(dateTimeFrom, dtFormatter);
                    LocalDateTime eventTo = LocalDateTime.parse(dateTimeTo, dtFormatter);
                    return Command.of("event", temp.substring(0, fromInd).trim(), eventFrom, eventTo);
                } catch (DateTimeParseException e) {
                    throw new MiloException("Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>"
                            + " /to <yyyy-MM-dd HH:mm>");
                }
            }
        }
        throw new MiloException("I'm sorry, I don't know what that means...");
    }
}
