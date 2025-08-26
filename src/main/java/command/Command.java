package command;

import java.time.LocalDate;
import java.time.LocalDateTime;

import exception.MiloException;
import model.TaskList;
import storage.Storage;
import ui.Ui;

public abstract class Command {
    public static Command of(String input) throws MiloException {
        switch (input) {
        case "bye": return new ExitCommand();
        case "help": return new HelpCommand();
        case "sort": return new SortCommand();
        case "reset": return new ResetCommand();
        case "list": return new ListCommand();
        default: throw new MiloException("Command not found!");
        }
    }

    public static Command of(String input, int num) throws MiloException {
        switch (input) {
        case "mark": return new MarkCommand(num);
        case "unmark": return new UnmarkCommand(num);
        case "delete": return new DeleteCommand(num);
        default: throw new MiloException("Command not found!");
        }
    }

    public static Command of(String input, LocalDate date) throws MiloException {
        if (input.equals("show")) {
            return new ShowCommand(date);
        }
        throw new MiloException("Command not found!");
    }

    public static Command of(String input, String misc) throws MiloException {
        if (input.equals("todo")) {
            return new TodoCommand(misc);
        }
        throw new MiloException("Command not found!");
    }

    public static Command of(String input, String misc, LocalDateTime... dates) throws MiloException {
        switch (input) {
        case "deadline": return new DeadlineCommand(misc, dates[0]);
        case "event": return new EventCommand(misc, dates[0], dates[1]);
        default: throw new MiloException("Command not found!");
        }
    }

    public boolean isExit() {
        return this instanceof ExitCommand;
    }

    public abstract void execute(TaskList tasks, Ui ui, Storage storage);
}
