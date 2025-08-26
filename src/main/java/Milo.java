import command.Command;
import exception.MiloException;
import logic.Parser;
import storage.Storage;
import model.TaskList;
import ui.Ui;

import java.io.FileNotFoundException;

/**
 * Main class for the Milo chatbot.
 * Initializes the task list, storage, and user interface,
 * and runs the main command loop to process user input.
 */
public class Milo {

    private final static TaskList tasks = new TaskList();
    private final static Ui ui = new Ui();
    private static Storage storage;

    /**
     * Entry point for the Milo chatbot application.
     * Initializes storage, reads tasks from the file, and runs the chatbot.
     * @param args Command-line arguments, optionally containing the file path for task storage.
     */
    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "./src/main/java/milo.txt";
        // Assuming no more than 100 tasks
        try {
            storage = new Storage(filePath, tasks);
            storage.readFile();
            run();
        } catch (FileNotFoundException e) {
            ui.showFileError(e);
        } catch (MiloException e) {
            ui.showError(e);
        }
    }

    /**
     * Runs the main command loop for Milo.
     * Continuously reads user commands, parses them, executes the corresponding Command,
     * and terminates when the exit command is issued.
     */
    public static void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (MiloException e) {
                ui.showError(e);
            }
        }
    }

}
