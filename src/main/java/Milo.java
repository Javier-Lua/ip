import java.io.FileNotFoundException;

import command.Command;
import exception.MiloException;
import logic.Parser;
import model.TaskList;
import storage.Storage;
import ui.Ui;

public class Milo {

    private static final TaskList tasks = new TaskList();
    private static final Ui ui = new Ui();
    private static Storage storage;

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
