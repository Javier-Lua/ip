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

public class Storage {
    private final String filePath;
    private final File f;
    private final Scanner s;
    private final TaskList tasks;
    private final Ui ui = new Ui();

    public Storage(String filePath, TaskList tasks) throws FileNotFoundException {
        this.f = new File(filePath);
        this.s = new Scanner(f);
        this.filePath = filePath;
        this.tasks = tasks;
    }

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

    public void clearFile() {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.close();
        } catch (IOException e) {
            ui.showTaskError(e);
        }
    }
}
