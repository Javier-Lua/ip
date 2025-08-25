import Task.Task;
import Task.Todo;
import Task.Deadline;
import Task.Event;

import enums.TaskType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.ArrayList;

public class Milo {

    private final static ArrayList<Task> tasks = new ArrayList<>();
    private final static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private static void readFile(String filePath) throws FileNotFoundException, MiloException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String t = s.nextLine();
            String[] parts = t.split("\\|");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }
            Task temp;
            switch (parts[0]) {
            case "T":
                temp = Task.makeTask(TaskType.TODO, parts[2]);
                if (parts[1].equals("1")) {
                    temp.markAsDone();
                }
                tasks.add(temp);
                break;
            case "D":
                temp = Task.makeTask(TaskType.DEADLINE, parts[2], parts[3]);
                if (parts[1].equals("1")) {
                    temp.markAsDone();
                }
                tasks.add(temp);
                break;
            case "E":
                temp = Task.makeTask(TaskType.EVENT, parts[2], parts[3], parts[4]);
                if (parts[1].equals("1")) {
                    temp.markAsDone();
                }
                tasks.add(temp);
                break;
            default:
                throw new MiloException("Tasks cannot be resolved.");
            }
        }
        s.close();
    }

    private static void saveTasks(String filePath) {
        try { // Write back to file
            // But first, clear contents in file first.
            FileWriter fw = new FileWriter(filePath);
            for (int i = 0; i < tasks.size(); i++) {
                String[] in = tasks.get(i).getFileInput();
                String res = String.join(" | ", in);
                fw.write(res + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("____________________________________________________________\n" +
                    "Error saving tasks: " + e.getMessage() + "\n" +
                    "____________________________________________________________\n");
        }
    }

    private static void clearFile(String filePath) {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.close();
        } catch (IOException e) {
            System.out.println("____________________________________________________________\n" +
                    "Error saving tasks: " + e.getMessage() + "\n" +
                    "____________________________________________________________\n");
        }
    }

    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "./data/milo.txt";
        // Assuming no more than 100 tasks
        try {
            Milo.readFile(filePath);
        } catch (FileNotFoundException | MiloException e) {
            System.out.println("____________________________________________________________\n" +
                    "File error: " + e.getMessage() + "\n" +
                    "____________________________________________________________\n");
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm Milo!\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n");
        String input = sc.nextLine();
        input = input.trim();
        while (!input.equals("bye")) {
            try { // Wrap everything in try-catch block in order to catch any Milo Exceptions thrown up
                if (input.equals("reset")) {
                    clearFile(filePath);
                    tasks.clear();
                    Task.resetCount();
                    System.out.println("____________________________________________________________\n" +
                            "Okay! Task list has been cleared.\n" +
                            "____________________________________________________________\n");
                } else if (input.equals("list")) {
                    String[] list = new String[Task.getCount()]; // copy over tasks descriptions
                    for (int i = 0; i < Task.getCount(); i++) {
                        list[i] = i + 1 + ". " + tasks.get(i).toString();
                    }
                    System.out.println("____________________________________________________________\n" +
                            "Here are the tasks in your list:\n" +
                            String.join("\n", list) + "\n" + // faster than += string concat
                            "____________________________________________________________\n");
                } else if (input.startsWith("mark") || input.startsWith("unmark") || input.startsWith("delete")) {
                    String[] parts = input.split(" "); // split when encounter spacing
                    if (!input.contains(" ")) {
                        throw new MiloException("Invalid mark/delete command. Use: mark <number> or unmark <number> " +
                                "or delete <number>.");
                    }
                    if (parts.length == 2) {
                        try {
                            int num = Integer.parseInt(parts[1]);
                            if (num <= Task.getCount() && num > 0) {
                                if (input.startsWith("mark")) {
                                    tasks.get(num - 1).markAsDone();
                                    System.out.println("____________________________________________________________" +
                                            "\n" +
                                            "Nice! I've marked this task as done:\n" +
                                            tasks.get(num - 1).toString() +
                                            "\n" + "____________________________________________________________");
                                } else if (input.startsWith("unmark")) {
                                    tasks.get(num - 1).markAsUndone();
                                    System.out.println("____________________________________________________________" +
                                            "\n" +
                                            "OK, I've marked this task as not done yet:\n" +
                                            tasks.get(num - 1).toString() +
                                            "\n" + "____________________________________________________________");
                                } else {
                                    Task temp = tasks.get(num - 1);
                                    temp.delete();
                                    tasks.remove(num - 1);
                                    System.out.println("____________________________________________________________" +
                                            "\n" +
                                            "Noted. I've removed this task:\n" + temp + "\n" +
                                            "Now you have " + Task.getCount() + " tasks in the list.\n" +
                                            "____________________________________________________________\n");
                                }
                                saveTasks(filePath);
                            } else {
                                throw new MiloException("Number out of range! Task number does not exist.");
                            }
                        } catch (NumberFormatException e) {
                            throw new MiloException("Invalid mark/delete command. Use: mark <number> or unmark " +
                                    "<number> or delete <number>.");
                        }
                    }
                } else {
                    Task t;
                    if (input.startsWith("todo")) {
                        if (input.length() <= 5) {
                            throw new MiloException("The description of a todo cannot be empty!");
                        }
                        if (input.charAt(4) != ' ') {
                            throw new MiloException("Invalid todo format! Use: todo <desc>.");
                        }
                        t = new Todo(input.substring(5).trim());
                    } else if (input.startsWith("deadline")) {
                        if (input.length() == 8) {
                            throw new MiloException("The description of a deadline cannot be empty!");
                        }
                        String temp = input.substring(9);
                        String[] parts = temp.split("/by");
                        if (parts.length < 2) {
                            throw new MiloException("Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd " +
                                    "HH:mm>");
                        }
                        String dateTime = parts[1].trim();
                        try {
                            LocalDateTime deadline = LocalDateTime.parse(dateTime, formatter);
                            t = new Deadline(parts[0].trim(), deadline);
                        } catch (DateTimeParseException e) {
                            throw new MiloException("Invalid deadline format! Use: deadline <desc> /by <yyyy-MM-dd " +
                                    "HH:mm>");
                        }
                    } else if (input.startsWith("event")) {
                        if (input.length() == 5) {
                            throw new MiloException("The description of an event cannot be empty!");
                        }
                        String temp = input.substring(6);
                        int fromInd = temp.indexOf("/from");
                        int toInd = temp.indexOf("/to");
                        if (fromInd == -1 || toInd == -1) {
                            throw new MiloException("Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>" +
                                    " /to <yyyy-MM-dd HH:mm>");
                        }
                        String dateTimeFrom = temp.substring(fromInd + 5, toInd).trim();
                        String dateTimeTo = temp.substring(toInd + 3).trim();
                        try {
                            LocalDateTime eventFrom = LocalDateTime.parse(dateTimeFrom, formatter);
                            LocalDateTime eventTo = LocalDateTime.parse(dateTimeTo, formatter);
                            t = new Event(temp.substring(0, fromInd).trim(), eventFrom, eventTo);
                        } catch (DateTimeParseException e) {
                            throw new MiloException("Invalid event format! Use: event <desc> /from <yyyy-MM-dd HH:mm>" +
                                    " /to <yyyy-MM-dd HH:mm>");
                        }
                    } else {
                        throw new MiloException("I'm sorry, I don't know what that means...");
                    }
                    tasks.add(t);
                    saveTasks(filePath);
                    System.out.println("____________________________________________________________\n" +
                            "Got it. I've added this task:\n" +
                            t + "\n" +
                            "Now you have " + Task.getCount() + " tasks in the list.\n" +
                            "____________________________________________________________\n");
                }
            } catch (MiloException e) {
                System.out.println("____________________________________________________________\n" +
                        e.getMessage() + "\n" +
                        "____________________________________________________________\n");
            }
            input = sc.nextLine();
        }
        sc.close();
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
