package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import exception.RotomException;
import model.Task;
import model.TaskList;
import ui.Ui;

public class StorageTest {
    @TempDir
    Path tempDir;

    @Test
    public void testReadFile() throws Exception {
        File tempFile = new File(tempDir.toFile(), "testRotom.txt");

        // -----------------------
        // Test invalid lines (should be skipped with error messages)
        // -----------------------
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("X | 0 | Invalid task\n"); // unknown task type
            writer.write("T | 1\n"); // missing description
            writer.write("D | 0 | Report | 2025-12-12T12:00\n"); // valid format
            writer.write("E | 0 | Meeting | 2025-12-12T15:00 | 2025-12-12T22:00\n"); // valid format
        }

        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempFile.getPath(), tasks, new Ui());

        // Should not throw exception for invalid lines, just skip them
        storage.readFile();

        // Only valid tasks should be loaded
        assertEquals(2, tasks.getCount());

        // -----------------------
        // Test valid lines
        // -----------------------
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("T | 1 | Buy groceries\n");
            writer.write("D | 0 | Submit report | 2025-12-12T12:00\n");
            writer.write("E | 0 | Project meeting | 2025-12-12T15:00 | 2025-12-12T22:00\n");
        }

        tasks = new TaskList();
        storage = new Storage(tempFile.getPath(), tasks, new Ui());
        storage.readFile();

        // -----------------------
        // Check task count
        // -----------------------
        assertEquals(3, tasks.getCount());

        // -----------------------
        // Check individual tasks
        // -----------------------
        Task t1 = tasks.getTask(0);
        assertEquals("Buy groceries", t1.getDescription());
        assertTrue(t1.isDone());

        Task t2 = tasks.getTask(1);
        assertEquals("Submit report", t2.getDescription());
        assertFalse(t2.isDone());

        Task t3 = tasks.getTask(2);
        assertEquals("Project meeting", t3.getDescription());
        assertFalse(t3.isDone());
    }

    @Test
    public void testReadFileWithNoPermission() throws Exception {
        // Skip this test on Windows as it doesn't support POSIX permissions well
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return;
        }

        // Create a file with no read permissions
        File tempFile = new File(tempDir.toFile(), "noReadPermission.txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("T | 0 | Test task\n");
        }

        // Remove read permission
        Files.setPosixFilePermissions(tempFile.toPath(),
                PosixFilePermissions.fromString("--x--x--x")); // Execute only, no read

        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempFile.getPath(), tasks, new Ui());

        // Should throw RotomException with permission error
        RotomException thrown = assertThrows(RotomException.class, storage::readFile);
        assertTrue(thrown.getMessage().contains("Permission denied"));
    }

    @Test
    public void testSaveTasksWithNoPermission() throws Exception {
        // Skip this test on Windows as it doesn't support POSIX permissions well
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return;
        }

        // Create a file with no write permissions
        File tempFile = new File(tempDir.toFile(), "noWritePermission.txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("T | 0 | Test task\n");
        }

        // Remove write permission
        Files.setPosixFilePermissions(tempFile.toPath(),
                PosixFilePermissions.fromString("r--r--r--")); // Read only, no write

        TaskList tasks = new TaskList();
        tasks.add(Task.makeTask(enums.TaskType.TODO, "Test task"));

        Storage storage = new Storage(tempFile.getPath(), tasks, new Ui());

        // Should handle permission error gracefully through UI
        storage.saveTasks();

        // No exception should be thrown, error should be handled via UI
    }

    @Test
    public void testCreateNewFile() throws Exception {
        // Test creating a new file in a non-existent directory
        File newFile = new File(tempDir.toFile(), "newdir/testRotom.txt");

        TaskList tasks = new TaskList();
        Storage storage = new Storage(newFile.getPath(), tasks, new Ui());

        // Should create the file and directory structure
        storage.readFile(); // This will create the file if it doesn't exist

        assertTrue(newFile.exists());
        assertTrue(newFile.getParentFile().exists());
    }

    @Test
    public void testEmptyFile() throws Exception {
        File tempFile = new File(tempDir.toFile(), "empty.txt");
        tempFile.createNewFile(); // Create empty file

        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempFile.getPath(), tasks, new Ui());

        // Should not throw any exceptions for empty file
        storage.readFile();

        assertEquals(0, tasks.getCount());
    }
}
