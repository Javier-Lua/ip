import java.io.FileNotFoundException;

import command.Command;
import exception.MiloException;
import logic.Parser;
import model.TaskList;
import storage.Storage;
import ui.Ui;

/**
 * Main class for the Milo chatbot.
 * Initializes the task list, storage, and user interface,
 * and runs the main command loop to process user input.
 */
public class Milo {

    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;

    /**
     * Constructs a new instance of the Milo chatbot.
     * Initializes the task list, user interface, and storage handler,
     * and loads any previously saved tasks from the specified file path.
     * @param filePath the path of the file used for saving and loading tasks
     * @throws FileNotFoundException if the storage file cannot be found
     * @throws MiloException if the task list cannot be initialized.
     */
    public Milo(String filePath) throws FileNotFoundException, MiloException {
        this.tasks = new TaskList();
        this.ui = new Ui();
        this.storage = new Storage(filePath, tasks, ui);
        storage.readFile();
    }

    /**
     * Processes a single user input string and returns Milo's response.
     * This method parses the input, executes the command,
     * and returns the result as a string. It does not print to the
     * console, making it suitable for unit testing or GUI
     * integration.
     * @param input the user input string to process
     * @return Milo's response to the input
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            return c.execute(tasks, ui, storage);
        } catch (MiloException e) {
            return ui.showError(e);
        }
    }
    /**
     * Entry point for the Milo chatbot application.
     * Initializes storage, reads tasks from the file, and runs the chatbot.
     * @param args Command-line arguments, optionally containing the file path for task storage.
     */
    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "./src/main/java/milo.txt";
        // Assuming no more than 100 tasks
        try {
            Milo m = new Milo(filePath);
            m.run();
        } catch (FileNotFoundException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (MiloException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Runs the main command loop for Milo.
     * Continuously reads user commands, parses them, executes the corresponding Command,
     * and terminates when the exit command is issued.
     */
    public void run() {
        System.out.println(ui.showWelcome());
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            String response = getResponse(fullCommand);
            System.out.println(response);
            try {
                Command c = Parser.parse(fullCommand);
                isExit = c.isExit();
            } catch (MiloException e) {
                // ignore, since getResponse already handled it.
            }
        }
    }
}
