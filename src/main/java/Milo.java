import command.Command;
import exception.MiloException;
import logic.Parser;
import storage.Storage;
import model.TaskList;
import ui.Ui;

import java.io.FileNotFoundException;

public class Milo {

    private final static TaskList tasks = new TaskList();
    private final static Ui ui = new Ui();
    private static Storage storage;

    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "./storage/milo.txt";
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
