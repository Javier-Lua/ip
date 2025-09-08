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
    private static final String TASK_DELIMITER = "\\|";
    private static final String DONE_INDICATOR = "1";
    private static final String TODO_INDICATOR = "T";
    private static final String DEADLINE_INDICATOR = "D";
    private static final String EVENT_INDICATOR = "E";
    private static final int MINIMUM_PARTS_LENGTH = 3;
    private static final int DEADLINE_PARTS_LENGTH = 4;
    private static final int EVENT_PARTS_LENGTH = 5;
    private final String filePath;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a Storage object with the given file path and task list.
     * Initializes file and scanner for reading.
     * @param filePath Path to the storage file.
     * @param tasks TaskList to store read tasks into.
     * @param ui User interface for error reporting.
     */
    public Storage(String filePath, TaskList tasks, Ui ui) {
        this.filePath = filePath;
        this.tasks = tasks;
        this.ui = ui;
    }

    /**
     * Reads tasks from the storage file and adds them to the task list.
     * @throws MiloException If a task cannot be resolved from the file content.
     */
    public void readFile() throws MiloException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                processLine(line);
            }
        } catch (FileNotFoundException e) {
            // Create a new file if it doesn't exist
            createNewFile();
        }
    }

    /**
     * Creates a new storage file if it doesn't exist.
     * @throws MiloException If file creation fails.
     */
    private void createNewFile() throws MiloException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        // Create parent directories if they don't exist
        if (parentDir != null && !parentDir.exists()) {
            boolean directoriesCreated = parentDir.mkdirs();
            if (!directoriesCreated) {
                throw new MiloException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
        // Create the file
        try {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new MiloException("Failed to create file: " + filePath);
            }
        } catch (IOException e) {
            throw new MiloException("Failed to create storage file: " + e.getMessage());
        }
    }

    /**
     * Processes a single line from the storage file.
     * @param line The line to process.
     * @throws MiloException If the task cannot be resolved from the line.
     */
    private void processLine(String line) throws MiloException {
        String[] parts = line.split(TASK_DELIMITER);
        trimAllParts(parts);
        validatePartsLength(parts, MINIMUM_PARTS_LENGTH);
        Task task = createTaskFromParts(parts);
        markTaskIfDone(parts, task);
        tasks.add(task);
    }

    /**
     * Trims all elements in the given string array.
     * @param parts The array to trim.
     */
    private void trimAllParts(String[] parts) {
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
    }

    /**
     * Validates that the parts array meets the minimum required length.
     * @param parts The parts array to validate.
     * @param minLength The minimum required length.
     * @throws MiloException If validation fails.
     */
    private void validatePartsLength(String[] parts, int minLength) throws MiloException {
        if (parts.length < minLength) {
            throw new MiloException("Invalid task format in storage file.");
        }
    }

    /**
     * Creates a task from the parsed parts of a file line.
     * @param parts The parsed components of the task.
     * @return The created task
     * @throws MiloException If the task type is unknown.
     */
    private Task createTaskFromParts(String[] parts) throws MiloException {
        String taskType = parts[0];
        return switch (taskType) {
        case TODO_INDICATOR -> createTodoTask(parts);
        case DEADLINE_INDICATOR -> createDeadlineTask(parts);
        case EVENT_INDICATOR -> createEventTask(parts);
        default -> throw new MiloException("Tasks cannot be resolved.");
        };
    }

    /**
     * Creates a todo task from parts.
     * @param parts The parsed components of the task.
     * @return The created task.
     * @throws MiloException If the task type is unknown.
     */
    private Task createTodoTask(String[] parts) throws MiloException {
        validatePartsLength(parts, MINIMUM_PARTS_LENGTH);
        return Task.makeTask(TaskType.TODO, parts[2]);
    }

    /**
     * Creates a Deadline task from parts.
     * @param parts The parsed components of the task.
     * @return The created task.
     * @throws MiloException If the task type is unknown.
     */
    private Task createDeadlineTask(String[] parts) throws MiloException {
        validatePartsLength(parts, DEADLINE_PARTS_LENGTH);
        return Task.makeTask(TaskType.DEADLINE, parts[2], parts[3]);
    }

    /**
     * Creates an Event task from parts.
     * @param parts The parsed components of the task.
     * @return The created task.
     * @throws MiloException If the task type is unknown.
     */
    private Task createEventTask(String[] parts) throws MiloException {
        validatePartsLength(parts, EVENT_PARTS_LENGTH);
        return Task.makeTask(TaskType.EVENT, parts[2], parts[3], parts[4]);
    }

    /**
     * Marks a task as done if indicated in the file.
     * @param parts The parsed components of the task.
     * @param task The task to mark.
     */
    private void markTaskIfDone(String[] parts, Task task) {
        if (parts.length > 1 && DONE_INDICATOR.equals(parts[1])) {
            task.markAsDone();
        }
    }

    /**
     * Saves the current tasks in the task list to the storage file.
     * Clears the file first before writing.
     * Errors during file operations are displayed via the UI.
     */
    public void saveTasks() {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            writeAllTasksToFile(fileWriter);
        } catch (IOException e) {
            handleFileError(e);
        }
    }

    /**
     * Writes all tasks to the file
     * @param fileWriter The fileWriter.
     * @throws IOException If Tasks cannot be written to file.
     */
    private void writeAllTasksToFile(FileWriter fileWriter) throws IOException {
        for (int i = 0; i < tasks.getCount(); i++) {
            Task task = tasks.getTask(i);
            writeTaskToFile(fileWriter, task);
        }
    }

    /**
     * Writes a single task to the file
     * @param fileWriter The fileWriter.
     * @throws IOException If Task cannot be written to file.
     */
    private void writeTaskToFile(FileWriter fileWriter, Task task) throws IOException {
        String[] fileInput = task.getFileInput();
        String line = String.join(" | ", fileInput);
        fileWriter.write(line + System.lineSeparator());
    }

    /**
     * Clears the contents of the storage file.
     * Errors during file operations are displayed via the UI.
     */
    public void clearFile() {
        try {
            // Create a FileWriter and immediately close it to clear file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.close();
        } catch (IOException e) {
            handleFileError(e);
        }
    }

    /**
     * Handles file errors by displaying them through the UI.
     * @param e The exception.
     */
    private void handleFileError(IOException e) {
        ui.showError(e);
    }
}
