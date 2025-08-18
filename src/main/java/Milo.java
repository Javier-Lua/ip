import java.util.Scanner;

public class Milo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm Milo!\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n");
        String command = sc.next();
        while(!command.equals("bye")) {
            System.out.println("____________________________________________________________\n" +
                    command + "\n" +
                    "____________________________________________________________\n");
            command = sc.next();
        }
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n");
    }
}
