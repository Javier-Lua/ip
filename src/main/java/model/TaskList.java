package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class TaskList {
    private final ArrayList<Task> tasks;
    private int count;

    public TaskList() {
        this.tasks = new ArrayList<Task>();
        this.count = 0;
    }

    public void sort() {
        tasks.sort(Comparator.comparing(
                t -> t.getDateTime() != null ? t.getDateTime() : LocalDateTime.MAX
        ));
    }

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

    public TaskList filter(String desc) {
        TaskList res = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().contains(desc)) {
                res.add(task);
            }
        }
        return res;
    }

    public void clear() {
        tasks.clear();
        count = 0;
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    public void delete(int index) {
        this.tasks.remove(index);
    }

    public int getCount() {
        return this.tasks.size();
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public void mark(int index) {
        this.tasks.get(index).markAsDone();
    }

    public void unmark(int index) {
        this.tasks.get(index).markAsUndone();
    }
}
