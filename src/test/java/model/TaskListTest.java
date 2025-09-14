package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.TaskType;
import exception.MiloException;

/**
 * Test class for TaskList functionality.
 * Tests all public methods of the TaskList class.
 */
public class TaskListTest {
    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;
    private Task eventTask;

    @BeforeEach
    public void setUp() throws MiloException {
        taskList = new TaskList();

        // Create test tasks
        todoTask = Task.makeTask(TaskType.TODO, "Test todo task");
        deadlineTask = Task.makeTask(TaskType.DEADLINE, "Test deadline",
                "2023-12-12T12:00");
        eventTask = Task.makeTask(TaskType.EVENT, "Test event",
                "2023-12-12T15:00", "2023-12-12T17:00");
    }

    @Test
    public void testConstructor() {
        assertNotNull(taskList);
        assertEquals(0, taskList.getCount());
    }

    @Test
    public void testAddTask() {
        taskList.add(todoTask);
        assertEquals(1, taskList.getCount());
        assertEquals(todoTask, taskList.getTask(0));
    }

    @Test
    public void testAddTaskAtIndex() {
        taskList.add(todoTask);
        taskList.addAtIndex(deadlineTask, 0);

        assertEquals(2, taskList.getCount());
        assertEquals(deadlineTask, taskList.getTask(0));
        assertEquals(todoTask, taskList.getTask(1));
    }

    @Test
    public void testAddTaskAtIndexOutOfBounds() {
        taskList.add(todoTask);

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.addAtIndex(deadlineTask, 5));
    }

    @Test
    public void testDeleteTaskByIndex() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        assertEquals(2, taskList.getCount());
        taskList.delete(0);
        assertEquals(1, taskList.getCount());
        assertEquals(deadlineTask, taskList.getTask(0));
    }

    @Test
    public void testDeleteTaskByIndexOutOfBounds() {
        taskList.add(todoTask);

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.delete(5));
    }

    @Test
    public void testRemoveTaskByObject() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        assertEquals(2, taskList.getCount());
        taskList.remove(todoTask);
        assertEquals(1, taskList.getCount());
        assertEquals(deadlineTask, taskList.getTask(0));
    }

    @Test
    public void testRemoveNonExistentTask() {
        taskList.add(todoTask);

        // Removing a task that's not in the list should not throw an exception
        taskList.remove(deadlineTask);
        assertEquals(1, taskList.getCount());
    }

    @Test
    public void testGetCount() {
        assertEquals(0, taskList.getCount());

        taskList.add(todoTask);
        assertEquals(1, taskList.getCount());

        taskList.add(deadlineTask);
        assertEquals(2, taskList.getCount());

        taskList.delete(0);
        assertEquals(1, taskList.getCount());
    }

    @Test
    public void testGetTask() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
    }

    @Test
    public void testGetTaskOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.getTask(0));

        taskList.add(todoTask);

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.getTask(1));
    }

    @Test
    public void testMarkTask() {
        taskList.add(todoTask);
        assertFalse(todoTask.isDone());

        taskList.mark(0);
        assertTrue(todoTask.isDone());
    }

    @Test
    public void testMarkTaskOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.mark(0));
    }

    @Test
    public void testUnmarkTask() {
        taskList.add(todoTask);
        todoTask.markAsDone();
        assertTrue(todoTask.isDone());

        taskList.unmark(0);
        assertFalse(todoTask.isDone());
    }

    @Test
    public void testUnmarkTaskOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.unmark(0));
    }

    @Test
    public void testClear() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);

        assertEquals(3, taskList.getCount());
        taskList.clear();
        assertEquals(0, taskList.getCount());
    }

    @Test
    public void testSortTasks() throws MiloException {
        // Create tasks with different dates
        Task earlyDeadline = Task.makeTask(TaskType.DEADLINE, "Early deadline",
                "2023-12-10T12:00");
        Task lateDeadline = Task.makeTask(TaskType.DEADLINE, "Late deadline",
                "2023-12-14T12:00");
        Task middleEvent = Task.makeTask(TaskType.EVENT, "Middle event",
                "2023-12-12T10:00", "2023-12-12T12:00");
        Task todo = Task.makeTask(TaskType.TODO, "Todo task");

        // Add in random order
        taskList.add(lateDeadline);
        taskList.add(todo);
        taskList.add(earlyDeadline);
        taskList.add(middleEvent);

        // Sort the tasks
        taskList.sort();

        // Verify order: tasks with dates first (in chronological order), then todos
        assertEquals(earlyDeadline, taskList.getTask(0));
        assertEquals(middleEvent, taskList.getTask(1));
        assertEquals(lateDeadline, taskList.getTask(2));
        assertEquals(todo, taskList.getTask(3));
    }

    @Test
    public void testFilterByDate() throws MiloException {
        LocalDate today = LocalDate.of(2023, 12, 12);

        Task todayDeadline = Task.makeTask(TaskType.DEADLINE, "Today deadline",
                "2023-12-12T12:00");
        Task tomorrowEvent = Task.makeTask(TaskType.EVENT, "Tomorrow event",
                "2023-12-13T10:00", "2023-12-13T12:00");
        Task todo = Task.makeTask(TaskType.TODO, "Todo task");

        taskList.add(todayDeadline);
        taskList.add(tomorrowEvent);
        taskList.add(todo);

        // Filter for today's tasks
        TaskList todayTasks = taskList.filter(today);
        assertEquals(1, todayTasks.getCount());
        assertEquals(todayDeadline, todayTasks.getTask(0));

        // Filter for tomorrow's tasks
        LocalDate tomorrow = LocalDate.of(2023, 12, 13);
        TaskList tomorrowTasks = taskList.filter(tomorrow);
        assertEquals(1, tomorrowTasks.getCount());
        assertEquals(tomorrowEvent, tomorrowTasks.getTask(0));

        // Filter for a date with no tasks
        LocalDate futureDate = LocalDate.of(2023, 12, 20);
        TaskList futureTasks = taskList.filter(futureDate);
        assertEquals(0, futureTasks.getCount());
    }

    @Test
    public void testFilterByDescription() throws MiloException {
        Task buyGroceries = Task.makeTask(TaskType.TODO, "Buy groceries");
        Task buyBooks = Task.makeTask(TaskType.TODO, "Buy books");
        Task readBooks = Task.makeTask(TaskType.TODO, "Read books");

        taskList.add(buyGroceries);
        taskList.add(buyBooks);
        taskList.add(readBooks);

        // Filter for "buy" tasks
        TaskList buyTasks = taskList.filter("Buy");
        assertEquals(2, buyTasks.getCount());
        assertTrue(buyTasks.getTask(0).getDescription().contains("Buy"));
        assertTrue(buyTasks.getTask(1).getDescription().contains("Buy"));

        // Filter for "books" tasks
        TaskList bookTasks = taskList.filter("books");
        assertEquals(2, bookTasks.getCount());
        assertTrue(bookTasks.getTask(0).getDescription().contains("books"));
        assertTrue(bookTasks.getTask(1).getDescription().contains("books"));

        // Filter for non-existent description
        TaskList carTasks = taskList.filter("car");
        assertEquals(0, carTasks.getCount());
    }

    @Test
    public void testFilterByDescriptionCaseSensitivity() throws MiloException {
        Task task = Task.makeTask(TaskType.TODO, "Case Sensitive Task");

        taskList.add(task);

        // Case sensitive filtering
        TaskList upperCaseTasks = taskList.filter("TASK");
        assertEquals(0, upperCaseTasks.getCount());

        TaskList lowerCaseTasks = taskList.filter("task");
        assertEquals(0, lowerCaseTasks.getCount());

        TaskList mixedCaseTasks = taskList.filter("Task");
        assertEquals(1, mixedCaseTasks.getCount());
    }

    @Test
    public void testFilterByEmptyDescription() throws MiloException {
        Task task = Task.makeTask(TaskType.TODO, "Test task");

        taskList.add(task);

        // Filter with empty string
        TaskList emptyFilterTasks = taskList.filter("");
        assertEquals(1, emptyFilterTasks.getCount());

        // Filter with space
        TaskList spaceFilterTasks = taskList.filter(" ");
        assertEquals(0, spaceFilterTasks.getCount());
    }
}
