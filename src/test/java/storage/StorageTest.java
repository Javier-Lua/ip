package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.Test;

import exception.MiloException;
import model.Task;
import model.TaskList;
import ui.Ui;

public class StorageTest {
    @Test
    public void testReadFile() throws Exception {
        File tempFile = File.createTempFile("testMilo", ".txt");
        tempFile.deleteOnExit();

        // -----------------------
        // Test invalid lines
        // -----------------------
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("X | 0 | Invalid task\n"); // unknown task type
            writer.write("T | 1\n"); // missing description
            writer.write("D | 0 | Report | 2025-12-12 12:00\n"); // wrong date format
            writer.write("E | 0 | Meeting | 15:00 | 22:00\n"); // wrong date format
        }

        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempFile.getPath(), tasks, new Ui());

        // Expect MiloException for first invalid line
        MiloException thrown = assertThrows(MiloException.class, storage::readFile);
        assertEquals("Tasks cannot be resolved.", thrown.getMessage());

        // -----------------------
        // Test valid lines
        // -----------------------
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("T | 1 | Buy groceries |  | \n");
            writer.write("D | 0 | Submit report | 2025-12-12T12:00 | \n");
            writer.write("D | 0 | Submit report | 2025-12-12T11:00 | \n");
            writer.write("E | 0 | Project meeting | 2025-12-12T15:00 | 2025-12-12T22:00\n");
            writer.write("E | 0 | Project meeting | 2025-12-12T19:00 | 2025-12-12T22:00\n");
        }

        tasks = new TaskList();
        storage = new Storage(tempFile.getPath(), tasks, new Ui());
        storage.readFile();

        // -----------------------
        // Check task count
        // -----------------------
        assertEquals(5, tasks.getCount());

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
        assertEquals("Submit report", t3.getDescription());
        assertFalse(t3.isDone());

        Task t4 = tasks.getTask(3);
        assertEquals("Project meeting", t4.getDescription());
        assertFalse(t4.isDone());

        Task t5 = tasks.getTask(4);
        assertEquals("Project meeting", t5.getDescription());
        assertFalse(t5.isDone());
    }
}