package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import enums.TaskType;
import exception.MiloException;
import model.Task;
import model.TaskList;
import ui.Ui;

/**
 * Handles reading and writing tasks to a persistent storage file.
 * Provides functionality to read tasks from file, save tasks to file,
 * and clear the file contents.
 */
public class Storage {
    private final String filePath;
    private final Scanner s;
    private final TaskList tasks;
    private final Ui ui = new Ui();

    /**
     * Constructs a Storage object with the given file path and task list.
     * Initializes file and scanner for reading.
     * @param filePath Path to the storage file.
     * @param tasks TaskList to store read tasks into,
     * @throws FileNotFoundException If the specified file does not exist.
     */
    public Storage(String filePath, TaskList tasks) throws FileNotFoundException {
        this.s = new Scanner(new File(filePath));
        this.filePath = filePath;
        this.tasks = tasks;
    }

    /**
     * Reads tasks from the storage file and adds them to the task list.
     * @throws MiloException If a task cannot be resolved from the file content.
     */
    public void readFile() throws MiloException {
        while (s.hasNextLine()) {
            String t = s.nextLine();
            String[] parts = t.split("\\|");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }
            Task temp;
            switch (parts[0]) {
            case "T":
                temp = Task.makeTask(TaskType.TODO, parts[2]);
                if (parts[1].equals("1")) {
                    temp.markAsDone();
                }
                tasks.add(temp);
                break;
            case "D":
                temp = Task.makeTask(TaskType.DEADLINE, parts[2], parts[3]);
                if (parts[1].equals("1")) {
                    temp.markAsDone();
                }
                tasks.add(temp);
                break;
            case "E":
                temp = Task.makeTask(TaskType.EVENT, parts[2], parts[3], parts[4]);
                if (parts[1].equals("1")) {
                    temp.markAsDone();
                }
                tasks.add(temp);
                break;
            default:
                throw new MiloException("Tasks cannot be resolved.");
            }
        }
        s.close();
    }

    /**
     * Saves the current tasks in the task list to the storage file.
     * Clears the file first before writing.
     * Errors during file operations are displayed via the UI.
     */
    public void saveTasks() {
        try { // Write back to file
            // But first, clear contents in file first.
            FileWriter fw = new FileWriter(filePath);
            for (int i = 0; i < tasks.getCount(); i++) {
                String[] in = tasks.getTask(i).getFileInput();
                String res = String.join(" | ", in);
                fw.write(res + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            ui.showTaskError(e);
        }
    }

    /**
     * Clears the contents of the storage file.
     * Errors during file operations are displayed via the UI.
     */
    public void clearFile() {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.close();
        } catch (IOException e) {
            ui.showTaskError(e);
        }
    }
}
