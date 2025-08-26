package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a list of tasks with functionality to add, delete, sort, filter,
 * and update task completion status.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Sorts tasks in ascending order based on their date and time.
     * Tasks without a date are placed at the end.
     */
    public void sort() {
        tasks.sort(Comparator.comparing(
                t -> t.getDateTime() != null ? t.getDateTime() : LocalDateTime.MAX
        ));
    }

    /**
     * Filters tasks to return only those occurring on the specified date.
     * @param date Date to filer tasks by.
     * @return A new TaskList containing tasks that occur on the specified date.
     */
    public TaskList filter(LocalDate date) {
        TaskList res = new TaskList();
        for (Task task : tasks) {
            LocalDateTime taskDateTime = task.getDateTime();
            if (taskDateTime != null) {
                LocalDate taskDate = taskDateTime.toLocalDate();
                if (taskDate.equals(date)) {
                    res.add(task);
                }
            }
        }
        return res;
    }

    /**
     * Returns a new TaskList containing all tasks whose
     * descriptions contain the specified keyword or
     * phrase.
     * @param desc Keyword or phrase to search for in task description.
     * @return TaskList of tasks that contain the specified keyword.
     */
    public TaskList filter(String desc) {
        TaskList res = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().contains(desc)) {
                res.add(task);
            }
        }
        return res;
    }
    /**
     * Clears all tasks from the task list.
     */
    public void clear() {
        tasks.clear();
    }

    /**
     * Adds a task to the task list.
     * @param task Task to be added.
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Deletes the task at the specified index.
     * @param index Index of the task to be deleted.
     */
    public void delete(int index) {
        this.tasks.remove(index);
    }

    /**
     * Returns the total number of tasks in the list.
     * @return Number of tasks.
     */
    public int getCount() {
        return this.tasks.size();
    }

    /**
     * Retrieves the task at the specified index.
     * @param index Index of the task to retrieve.
     * @return Task at the specified index.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Marks the task at the specified index as done.
     * @param index Index of the task to mark.
     */
    public void mark(int index) {
        this.tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the specified index as not done.
     * @param index Index of the task to unmark.
     */
    public void unmark(int index) {
        this.tasks.get(index).markAsUndone();
    }
}
