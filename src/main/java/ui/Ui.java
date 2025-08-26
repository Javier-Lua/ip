package ui;

import model.Task;
import model.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public class Ui {

    private final Scanner sc;
    private final static DateTimeFormatter dFormatter =
            DateTimeFormatter.ofPattern("MMM dd yyyy").withResolverStyle(ResolverStyle.STRICT);

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm Milo!\n" +
                " What can I do for you?\n" +
                " Type 'help' for the list of commands I can understand!\n" +
                "____________________________________________________________\n");
    }

    public void showGoodbye() {
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
        this.sc.close();
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void showList(TaskList res, LocalDate date) {
        if (date == null) {
            System.out.println("____________________________________________________________\n" +
                    "Here are the tasks in your list:");
        } else {
            System.out.println("____________________________________________________________\n" +
                    "Here are the tasks in your list for " + date.format(dFormatter) + ":");
        }
        for (int i = 0; i < res.getCount(); i++) {
            System.out.println(i + 1 + ". " + res.getTask(i).toString());
        }
        System.out.println("____________________________________________________________\n");
    }

    public void showHelp() {
        System.out.println("____________________________________________________________\n" +
                " show <yyyy-MM-dd> : Shows the list of tasks on the specified day\n" +
                " list : Displays list of all tasks\n" +
                " sort : Sorts the tasks in chronological order\n" +
                " reset : Resets list of tasks\n" +
                " mark/unmark <number> : Marks given task as done/undone\n" +
                " delete <number> : Deletes given task\n" +
                " todo <desc> : Creates a task with no specified date.\n" +
                " deadline <desc> /by <yyyy-MM-dd> : Creates a task with a deadline\n" +
                " event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>: Creates an event\n" +
                " bye : Closes the chatbot\n" +
                "____________________________________________________________\n");
    }

    public void showError(Exception e) {
        System.out.println("____________________________________________________________\n" +
                e.getMessage() + "\n" +
                "____________________________________________________________\n");
    }

    public void showLine() {
        System.out.println("____________________________________________________________\n");
    }

    public void showTaskError(Exception e) {
        System.out.println("____________________________________________________________\n" +
                "Error saving tasks: " + e.getMessage() + "\n" +
                "____________________________________________________________\n");
    }

    public void showFileError(Exception e) {
        System.out.println("____________________________________________________________\n" +
                "File error: " + e.getMessage() + "\n" +
                "____________________________________________________________\n");
    }

    public void showAddTask(Task task, int count) {
        System.out.println("____________________________________________________________\n" +
                "Got it. I've added this task:\n" +
                task + "\n" +
                "Now you have " + count + " tasks in the list.\n" +
                "____________________________________________________________\n");
    }

    public void showTaskSorted() {
        System.out.println("____________________________________________________________\n" +
                "Okay! Task list has been sorted chronologically!\n" +
                "____________________________________________________________\n");
    }

    public void showTaskCleared() {
        System.out.println("____________________________________________________________\n" +
                "Okay! Task list has been cleared.\n" +
                "____________________________________________________________\n");
    }

    public void showTaskMarked(Task task) {
        System.out.println("____________________________________________________________" +
                "\n" +
                "Nice! I've marked this task as done:\n" +
                task +
                "\n" + "____________________________________________________________");
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("____________________________________________________________" +
                "\n" +
                "OK, I've marked this task as not done yet:\n" +
                task +
                "\n" + "____________________________________________________________");
    }

    public void showTaskRemoved(Task task, int count) {
        System.out.println("____________________________________________________________" +
                "\n" +
                "Noted. I've removed this task:\n" + task + "\n" +
                "Now you have " + count + " tasks in the list.\n" +
                "____________________________________________________________\n");
    }
}
