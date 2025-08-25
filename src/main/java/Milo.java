import Task.*;
import enums.TaskType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Milo {

    private final static ArrayList<Task> tasks = new ArrayList<>();

    public static void readFile(String filePath) throws FileNotFoundException, MiloException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String t = s.nextLine();
            String[] parts = t.split("\\|");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }
            Task temp;
            switch(parts[0]) {
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

    public static void writeFile(String filePath, String t) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(t + System.lineSeparator());
        fw.close();
    }

    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "./data/milo.txt";
        // Assuming no more than 100 tasks
        try {
            Milo.readFile(filePath);
        } catch (FileNotFoundException | MiloException e) {
            System.out.println("____________________________________________________________\n" +
                    e.getMessage() + "\n" +
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
                if (input.equals("list")) {
                    String[] list = new String[Task.getCount()]; // copy over tasks descriptions
                    for (int i = 0; i < Task.getCount(); i++) {
                        list[i] = tasks.get(i).getTag() + ". " + tasks.get(i).toString();
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
                            throw new MiloException("Invalid deadline format! Use: deadline <desc> /by <date>");
                        }
                        t = new Deadline(parts[0].trim(), parts[1].trim());
                    } else if (input.startsWith("event")) {
                        if (input.length() == 5) {
                            throw new MiloException("The description of an event cannot be empty!");
                        }
                        String temp = input.substring(6);
                        int fromInd = temp.indexOf("/from");
                        int toInd = temp.indexOf("/to");
                        if (fromInd == -1 || toInd == -1) {
                            throw new MiloException("Invalid event format! Use: event <desc> /from <time> /to <time>");
                        }
                        t = new Event(temp.substring(0, fromInd).trim(), temp.substring(fromInd + 5, toInd).trim(),
                                temp.substring(toInd + 3).trim());
                    } else {
                        throw new MiloException("I'm sorry, I don't know what that means...");
                    }
                    tasks.add(t);
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
        // Write back to file
        try {
            // But first, clear contents in file first.
            FileWriter clear = new FileWriter(filePath);
            clear.close();
            for (int i = 0; i < tasks.size(); i++) {
                String[] in = tasks.get(i).getFileInput();
                String res = String.join(" | ", in);
                Milo.writeFile(filePath, res);
            }
        } catch (IOException e) {
            System.out.println("____________________________________________________________\n" +
                    e.getMessage() + "\n" +
                    "____________________________________________________________\n");
        }
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
