import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DrPijon drPijon = new DrPijon();
        String LINE = "____________________________________________________________";

        System.out.println(drPijon.banner);
        System.out.println(drPijon.greet);

        // Level 1: Echo
        String[] list = new String[100];
        String line;
        Scanner in = new Scanner(System.in);

        while (true) {
            line = in.nextLine();
            if (line.equals("bye")) {
                System.out.println(drPijon.goodbye);
                break;
            }
            if (line.equals("list")) {
                printList(list);
            }

            System.out.println(line);
        }
        // Level 1: Echo

    }

    public static void printList(String[] list) {
        for (String item : list) {
            System.out.println(item);
        }
    }
}