import java.util.Scanner;

public class Milo {
    public static void main(String[] args) {
        // Assuming no more than 100 tasks
        String[] tasks = new String[100];
        Scanner sc = new Scanner(System.in);
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm Milo!\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n");
        String task = sc.nextLine();
        int itemInd = 0;
        while(!task.equals("bye")) {
            if (task.equals("list")) {
                String[] list = new String[itemInd]; // copy over tasks array into smaller array
                for (int i = 0; i < itemInd; i++) {
                    list[i] = tasks[i];
                }
                System.out.println("____________________________________________________________\n" +
                        String.join("\n", list) + "\n" + // faster than += string concat
                        "____________________________________________________________\n");
            } else {
                int num = itemInd + 1;
                tasks[itemInd] = num + ". " + task; // number task once added
                itemInd += 1;
                System.out.println("____________________________________________________________\n" +
                        "added: " + task + "\n" +
                        "____________________________________________________________\n");
            }
            task = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
