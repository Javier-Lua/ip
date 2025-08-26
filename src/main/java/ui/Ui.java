package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;

import model.Task;
import model.TaskList;

/**
 * Handles user interaction with the chatbot.
 * Provides methods to display messages, read input, and show task-related updates.
 */
public class Ui {

    private static final DateTimeFormatter dFormatter =
            DateTimeFormatter.ofPattern("MMM dd yyyy").withResolverStyle(ResolverStyle.STRICT);
    private final Scanner sc;

    /**
     * Constructs Ui object with a Scanner for reading user input.
     */
    public Ui() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the chatbot starts.
     */
    public void showWelcome() {
        System.out.println("""
                ____________________________________________________________
                 Hello! I'm Milo!
                 What can I do for you?
                 Type 'help' for the list of commands I can understand!
                ____________________________________________________________
                """);
    }

    /**
     * Displays the goodbye message and closes the scanner.
     */
    public void showGoodbye() {
        System.out.println("""
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________
                """);
        this.sc.close();
    }

    /**
     * Reads the next line of user input.
     * @return User input as a String.
     */
    public String readCommand() {
        return sc.nextLine().trim();
    }

    /**
     * Displays a list of tasks, optionally filtered by a specific date.
     * Note the TaskList cannot be filtered using this method.
     * @param res TaskList containing the tasks to display.
     * @param date Date to display the date to the user.
     */
    public void showList(TaskList res, LocalDate date) {
        if (date == null) {
            System.out.println("____________________________________________________________\n"
                    + "Here are the tasks in your list:");
        } else {
            System.out.println("____________________________________________________________\n"
                    + "Here are the tasks in your list for " + date.format(dFormatter) + ":");
        }
        for (int i = 0; i < res.getCount(); i++) {
            System.out.println(i + 1 + ". " + res.getTask(i).toString());
        }
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Displays a list of supported commands and their usage.
     */
    public void showHelp() {
        System.out.println("""
                ____________________________________________________________
                 show <yyyy-MM-dd> : Shows the list of tasks on the specified day
                 list : Displays list of all tasks
                 sort : Sorts the tasks in chronological order
                 reset : Resets list of tasks
                 mark/unmark <number> : Marks given task as done/undone
                 delete <number> : Deletes given task
                 todo <desc> : Creates a task with no specified date.
                 deadline <desc> /by <yyyy-MM-dd> : Creates a task with a deadline
                 event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>: Creates an event
                 bye : Closes the chatbot
                ____________________________________________________________
                """);
    }

    /**
     * Displays an error message for general exceptions.
     * @param e Exception to display.
     */
    public void showError(Exception e) {
        System.out.println("____________________________________________________________\n"
                + e.getMessage() + "\n"
                + "____________________________________________________________\n");
    }

    /**
     * Displays an error message when saving tasks fails.
     * @param e Exception containing error details.
     */
    public void showTaskError(Exception e) {
        System.out.println("____________________________________________________________\n"
                + "Error saving tasks: " + e.getMessage() + "\n"
                + "____________________________________________________________\n");
    }

    /**
     * Displays an error message when file-related operations fail.
     * @param e Exception containing error details.
     */
    public void showFileError(Exception e) {
        System.out.println("____________________________________________________________\n"
                + "File error: " + e.getMessage() + "\n"
                + "____________________________________________________________\n");
    }

    /**
     * Displays a message when a task is added to the list.
     * @param task Task that was added.
     * @param count Current number of tasks in the list.
     */
    public void showAddTask(Task task, int count) {
        System.out.println("____________________________________________________________\n"
                + "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + count + " tasks in the list.\n"
                + "____________________________________________________________\n");
    }

    /**
     * Displays a message when the task list has been sorted.
     */
    public void showTaskSorted() {
        System.out.println("""
                ____________________________________________________________
                Okay! Task list has been sorted chronologically!
                ____________________________________________________________
                """);
    }

    /**
     * Displays a message when the task list has been cleared.
     */
    public void showTaskCleared() {
        System.out.println("""
                ____________________________________________________________
                Okay! Task list has been cleared.
                ____________________________________________________________
                """);
    }

    /**
     * Displays a message when a task is marked as done.
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("____________________________________________________________"
                + "\n"
                + "Nice! I've marked this task as done:\n"
                + task
                + "\n" + "____________________________________________________________");
    }

    /**
     * Displays a message when a task is marked as not done.
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("____________________________________________________________"
                + "\n"
                + "OK, I've marked this task as not done yet:\n"
                + task
                + "\n" + "____________________________________________________________");
    }

    /**
     * Displays a message when a task is removed from the list
     * @param task Task that was removed.
     * @param count Current number of tasks in the list.
     */
    public void showTaskRemoved(Task task, int count) {
        System.out.println("____________________________________________________________"
                + "\n"
                + "Noted. I've removed this task:\n" + task + "\n"
                + "Now you have " + count + " tasks in the list.\n"
                + "____________________________________________________________\n");
    }

    /**
     * Displays a message when a task is searched.
     * @param tasks Task that was searched
     */
    public void showTaskSearched(TaskList tasks) {
        System.out.println("____________________________________________________________\n"
                + "Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.getCount(); i++) {
            System.out.println(i + 1 + ". " + tasks.getTask(i).toString());
        }
        System.out.println("____________________________________________________________\n");
    }
}
