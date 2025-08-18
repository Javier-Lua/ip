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
                    list[i] = tasks[i].getTag() + ". [" + tasks[i].getStatusIcon() + "] " + tasks[i].getDescription();
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
                                        "[" + tasks[num - 1].getStatusIcon() + "] " + tasks[num - 1].getDescription() +
                                         "\n" + "____________________________________________________________");
                            } else {
                                tasks[num - 1].markAsUndone();
                                System.out.println("____________________________________________________________\n" +
                                        "OK, I've marked this task as not done yet:\n" +
                                        "[" + tasks[num - 1].getStatusIcon() + "] " + tasks[num - 1].getDescription() +
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
                Task t = new Task(input);
                tasks[Task.getCount() - 1] = t; // number task once added
                System.out.println("____________________________________________________________\n" +
                        "added: " + t.getDescription() + "\n" +
                        "____________________________________________________________\n");
            }
            input = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
