package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
    private static final String TASK_DELIMITER = "\\s*\\|\\s*";
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
        assert filePath != null : "File path cannot be null";
        assert !filePath.isEmpty() : "File path cannot be empty";
        assert tasks != null : "Task list cannot be null";
        this.filePath = filePath;
        this.tasks = tasks;
        this.ui = ui;
    }

    /**
     * Reads tasks from the storage file and adds them to the task list.
     * @throws MiloException If a task cannot be resolved from the file content.
     */
    public void readFile() throws MiloException {
        File file = new File(filePath);
        if (!file.exists()) {
            createNewFile();
            return;
        }
        checkFileReadPermissions(file);
        try (Scanner scanner = new Scanner(file)) {
            readAndProcessTasks(scanner);
        } catch (FileNotFoundException e) {
            throw new MiloException("Storage file not found: " + filePath);
        } catch (SecurityException e) {
            throw new MiloException("Security manager denied access to storage file: " + filePath);
        } catch (Exception e) {
            throw new MiloException("Unexpected error reading from storage file: " + e.getMessage());
        }
    }

    /**
     * Checks if file can be read.
     * @param file The file containing task data.
     * @throws MiloException If storage file cannot be read.
     */
    private void checkFileReadPermissions(File file) throws MiloException {
        if (!file.canRead()) {
            throw new MiloException("Cannot read from storage file: Permission denied. "
                    + "Please check file permissions for: " + filePath);
        }
    }

    /**
     * Reads and processes tasks.
     * @param scanner The scanner for tasks.
     */
    private void readAndProcessTasks(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                processLine(line);
            } catch (MiloException e) {
                ui.showError(e);
            }
        }
    }

    /**
     * Creates a new storage file if it doesn't exist.
     * @throws MiloException If file creation fails.
     */
    private void createNewFile() throws MiloException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        createParentDirectories(parentDir);
        createStorageFiles(file);
    }

    /**
     * Creates parent directories for the storage file if they don't exist.
     * @param parentDir The parent directory to create
     * @throws MiloException If directory creation fails due to IO or permission issues
     */
    private void createParentDirectories(File parentDir) throws MiloException {
        if (parentDir != null && !parentDir.exists()) {
            try {
                Files.createDirectories(parentDir.toPath());
            } catch (IOException e) {
                throw new MiloException("Failed to create directory: " + parentDir.getAbsolutePath()
                        + ". Error: " + e.getMessage());
            } catch (SecurityException e) {
                throw new MiloException("Permission denied when creating directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    /**
     * Creates the storage file if it doesn't exist.
     * @param file The file to create
     * @throws MiloException If file creations fails due to IO or permission issues.
     */
    private void createStorageFiles(File file) throws MiloException {
        try {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated && !file.exists()) {
                throw new MiloException("Failed to create file: " + filePath);
            }
        } catch (IOException e) {
            throw new MiloException("Failed to create storage file: " + e.getMessage());
        } catch (SecurityException e) {
            throw new MiloException("Permission denied when creating file: " + filePath);
        }
    }

    /**
     * Processes a single line from the storage file.
     * @param line The line to process.
     * @throws MiloException If the task cannot be resolved from the line.
     */
    private void processLine(String line) throws MiloException {
        if (line.trim().isEmpty()) {
            return;
        }
        String[] parts = line.split(TASK_DELIMITER);
        trimAllParts(parts);
        if (parts.length < MINIMUM_PARTS_LENGTH) {
            throw new MiloException("Invalid task format: not enough components");
        }
        validateTaskType(parts[0]);
        Task task = createTaskFromParts(parts);
        markTaskIfDone(parts, task);
        tasks.add(task);
    }

    /**
     * Validates that the task type is one of the known types.
     * @param taskType The task type to validate.
     * @throws MiloException If the task type is unknown.
     */
    private void validateTaskType(String taskType) throws MiloException {
        if (!isValidTaskType(taskType)) {
            throw new MiloException("Unknown task type: " + taskType);
        }
    }

    /**
     * Checks if the given task type is a valid known type.
     * @param taskType The task type to check
     * @return true if the task type is valid, else false.
     */
    private boolean isValidTaskType(String taskType) {
        return TODO_INDICATOR.equals(taskType)
                || DEADLINE_INDICATOR.equals(taskType)
                || EVENT_INDICATOR.equals(taskType);
    }

    /**
     * Trims all elements in the given string array.
     * @param parts The array to trim.
     */
    private void trimAllParts(String[] parts) {
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != null) {
                parts[i] = parts[i].trim();
            }
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
        File file = new File(filePath);
        if (file.exists() && !file.canWrite()) {
            handleFileError(new IOException("Cannot write to storage file: Permission denied"));
            return;
        }
        saveToTemporaryFile();
    }

    /**
     * Saves tasks to a temporary file first to prevent data corruption.
     * Replaces the original file only if the temporary file save succeeds.
     */
    private void saveToTemporaryFile() {
        String tempFilePath = filePath + ".tmp";
        File tempFile = new File(tempFilePath);
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            writeAllTasksToFile(fileWriter);
            replaceOriginalFile(tempFile);
        } catch (IOException e) {
            cleanupTemporaryFile(tempFile);
            handleFileError(e);
        } catch (SecurityException e) {
            cleanupTemporaryFile(tempFile);
            handleFileError(new IOException("Permission denied when saving tasks"));
        }
    }

    /**
     * Replaces the original file with the temporary file.
     * @param tempFile The temporary file containing the successfully saved data
     */
    private void replaceOriginalFile(File tempFile) throws IOException {
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            throw new IOException("Failed to delete original file: " + filePath);
        }
        if (!tempFile.renameTo(file)) {
            throw new IOException("Failed to rename temporary file to: " + filePath);
        }
    }

    /**
     * Cleans up the temporary file in case of errors during save operations.
     * @param tempFile The temporary file to clean up.
     */
    private void cleanupTemporaryFile(File tempFile) {
        if (tempFile.exists() && !tempFile.delete()) {
            System.err.println("Warning: Could not delete temporary file: " + tempFile.getAbsolutePath());
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
        File file = new File(filePath);
        if (file.exists() && !file.canWrite()) {
            handleFileError(new IOException("Cannot clear storage file: Permission denied"));
            return;
        }
        clearFileContents();
    }

    /**
     * Clears the contents of the storage file.
     * Handles IO and security exceptions during the clear operation.
     */
    private void clearFileContents() {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // Simply opening and closing the file writer clears the content
        } catch (IOException e) {
            handleFileError(e);
        } catch (SecurityException e) {
            handleFileError(new IOException("Permission denied when clearing file"));
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
