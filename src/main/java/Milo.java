import java.util.Scanner;

public class Milo {
    public static void main(String[] args) {
        // Assuming no more than 100 tasks
        Task[] tasks = new Task[100];
        Scanner sc = new Scanner(System.in);
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm Milo!\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n");
        String input = sc.nextLine();
        input = input.trim();
        while(!input.equals("bye")) {
            try { // Wrap everything in try-catch block in order to catch any Milo Exceptions thrown up
                if (input.equals("list")) {
                    String[] list = new String[Task.getCount()]; // copy over tasks array into smaller array
                    for (int i = 0; i < Task.getCount(); i++) {
                        list[i] = tasks[i].getTag() + ". " + tasks[i].toString();
                    }
                    System.out.println("____________________________________________________________\n" +
                            "Here are the tasks in your list:\n" +
                            String.join("\n", list) + "\n" + // faster than += string concat
                            "____________________________________________________________\n");
                } else if (input.startsWith("mark") || input.startsWith("unmark")) {
                    String[] parts = input.split(" "); // split when encounter spacing
                    if (parts.length == 2) {
                        try {
                            int num = Integer.parseInt(parts[1]);
                            if (num <= Task.getCount() && num > 0) {
                                if (input.startsWith("mark")) {
                                    tasks[num - 1].markAsDone();
                                    System.out.println("____________________________________________________________\n" +
                                            "Nice! I've marked this task as done:\n" +
                                            tasks[num - 1].toString() +
                                            "\n" + "____________________________________________________________");
                                } else {
                                    tasks[num - 1].markAsUndone();
                                    System.out.println("____________________________________________________________\n" +
                                            "OK, I've marked this task as not done yet:\n" +
                                            tasks[num - 1].toString() +
                                            "\n" + "____________________________________________________________");
                                }
                            } else {
                                throw new MiloException("Number out of range! Task number does not exist.");
                            }
                        } catch (NumberFormatException e) {
                            throw new MiloException("Invalid mark command. Use: mark <number> or unmark <number>.");
                        }
                    }
                } else {
                    Task t = null;
                    if (input.startsWith("todo")) {
                        if (input.length() <= 5) {
                            throw new MiloException("The description of a todo cannot be empty!");
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
                    tasks[Task.getCount() - 1] = t; // number task once added
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
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
