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
                        if (num <= Task.getCount()) {
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
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid mark!");
                    }
                }
            } else {
                Task t = null;
                if (input.startsWith("todo")) {
                    t = new Todo(input.substring(5));
                } else if (input.startsWith("deadline")) {
                    String temp = input.substring(9);
                    String[] parts = temp.split("/by");
                    t = new Deadline(parts[0].trim(), parts[1].trim());
                } else if (input.startsWith("event")) {
                    String temp = input.substring(6);
                    int fromInd = temp.indexOf("/from");
                    int toInd = temp.indexOf("/to");
                    t = new Event(temp.substring(0, fromInd), temp.substring(fromInd + 5, toInd), temp.substring(toInd + 3));
                }
                if (t != null) { // more error handling in Level-5
                    tasks[Task.getCount() - 1] = t; // number task once added
                    System.out.println("____________________________________________________________\n" +
                            "Got it. I've added this task:\n" +
                            t + "\n" +
                            "Now you have " + Task.getCount() + " tasks in the list.\n" +
                            "____________________________________________________________\n");
                }
            }
            input = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
