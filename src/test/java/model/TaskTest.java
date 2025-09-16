package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import enums.TaskType;
import exception.RotomException;

public class TaskTest {
    @Test
    public void testMakeTask_edgeCases() {
        // TODO with empty description
        assertThrows(RotomException.class, () -> Task.makeTask(TaskType.TODO, ""));

        // DEADLINE missing date
        assertThrows(RotomException.class, () -> Task.makeTask(TaskType.DEADLINE, "Report"));

        // DEADLINE malformed date
        assertThrows(RotomException.class, () ->
                Task.makeTask(TaskType.DEADLINE, "Report", "12-12-2025 12:00"));

        // EVENT missing dates
        assertThrows(RotomException.class, () -> Task.makeTask(TaskType.EVENT, "Meeting"));
        assertThrows(RotomException.class, () ->
                Task.makeTask(TaskType.EVENT, "Meeting", "2025-12-12T15:00"));

        // EVENT malformed dates
        assertThrows(RotomException.class, () ->
                Task.makeTask(TaskType.EVENT, "Meeting", "15:00", "22:00"));
    }

    @Test
    public void testMakeTask_validCases() throws RotomException {
        // TODO
        Task todo = Task.makeTask(TaskType.TODO, "Buy groceries");
        assertEquals("Buy groceries", todo.getDescription());
        assertFalse(todo.isDone());

        // DEADLINE
        String deadlineStr = "2025-12-12T12:00";
        Task deadline = Task.makeTask(TaskType.DEADLINE, "Submit report", deadlineStr);
        assertEquals("Submit report", deadline.getDescription());
        assertFalse(deadline.isDone());

        // EVENT
        String start = "2025-12-12T15:00";
        String end = "2025-12-12T22:00";
        Task event = Task.makeTask(TaskType.EVENT, "Project meeting", start, end);
        assertEquals("Project meeting", event.getDescription());
        assertFalse(event.isDone());
        assertEquals(LocalDateTime.parse(start), (event).getDateTime());
        assertEquals(LocalDateTime.parse(end), ((model.Event) event).getDateTimeTo());
    }
}
