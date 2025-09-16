import java.io.FileNotFoundException;
import java.util.Set;

import command.Command;
import command.CommandHistory;
import exception.RotomException;
import logic.Parser;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Main class for the Rotom chatbot.
 * Initializes the task list, storage, and user interface,
 * and runs the main command loop to process user input.
 */
public class Rotom {

    // Set of commands that should NOT be saved to history
    private static final Set<Class<? extends Command>> EXCLUDED_COMMANDS = Set.of(
            command.UndoCommand.class,
            command.SortCommand.class,
            command.ShowCommand.class,
            command.ListCommand.class,
            command.HelpCommand.class,
            command.FindCommand.class,
            command.ExitCommand.class
    );
    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;
    private final CommandHistory commandHistory;

    /**
     * Constructs a new instance of the Rotom chatbot.
     * Initializes the task list, user interface, and storage handler,
     * and loads any previously saved tasks from the specified file path.
     * @param filePath the path of the file used for saving and loading tasks
     * @throws FileNotFoundException if the storage file cannot be found
     * @throws RotomException if the task list cannot be initialized.
     */
    public Rotom(String filePath) throws FileNotFoundException, RotomException {
        this.tasks = new TaskList();
        this.ui = new Ui();
        this.storage = new Storage(filePath, tasks, ui);
        this.commandHistory = new CommandHistory();
        assert tasks != null : "TaskList should be initialized";
        assert ui != null : "Ui should be initialized";
        assert storage != null : "Storage should be initialized";
        assert commandHistory != null : "CommandHistory should be initialized";
        storage.readFile();
    }

    /**
     * Processes a single user input string and returns Rotom's response.
     * This method parses the input, executes the command,
     * and returns the result as a string. It does not print to the
     * console, making it suitable for unit testing or GUI
     * integration.
     * @param input the user input string to process
     * @return Rotom's response to the input
     */
    public String getResponse(String input) {
        assert input != null : "Input should not be null";
        try {
            Command c = Parser.parse(input, commandHistory);
            String response = c.execute(tasks, ui, storage);
            if (shouldSaveToHistory(c)) {
                commandHistory.push(c);
            }
            return response;
        } catch (RotomException e) {
            return ui.showError(e);
        }
    }

    /**
     * Checks the given command if it should be added to the command history.
     * @param command Command to check.
     * @return True if command executed can be undone.
     */
    private boolean shouldSaveToHistory(Command command) {
        return !EXCLUDED_COMMANDS.contains(command.getClass());
    }
    /**
     * Entry point for the Rotom chatbot application.
     * Initializes storage, reads tasks from the file, and runs the chatbot.
     * @param args Command-line arguments, optionally containing the file path for task storage.
     */
    public static void main(String[] args) {
        assert args != null : "Command line arguments should not be null";
        String filePath = args.length > 0 ? args[0] : "./src/main/java/rotom.txt";
        // Assuming no more than 100 tasks
        try {
            Rotom m = new Rotom(filePath);
            m.run();
        } catch (FileNotFoundException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (RotomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Runs the main command loop for Rotom.
     * Continuously reads user commands, parses them, executes the corresponding Command,
     * and terminates when the exit command is issued.
     */
    public void run() {
        System.out.println(ui.showWelcome());
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            assert fullCommand != null : "Command should not be null";
            String response = getResponse(fullCommand);
            System.out.println(response);
            try {
                Command c = Parser.parse(fullCommand, commandHistory);
                isExit = c.isExit();
            } catch (RotomException e) {
                // ignore, since getResponse already handled it.
            }
        }
    }
}
