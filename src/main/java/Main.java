import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DrPijon drPijon = new DrPijon();
        String LINE = "____________________________________________________________";

        System.out.println(drPijon.banner);
        System.out.println(drPijon.greet);

        String[] list = new String[100];
        String inputLine;
        Scanner in = new Scanner(System.in);

        while (true) {
            inputLine = in.nextLine();

            if (inputLine.equals("bye")) {
                System.out.println(drPijon.goodbye);
                break;
            } else if (inputLine.equals("list")) {
                printList(list);
            } else {
                list[listSize] = inputLine;
                listSize++;
                System.out.println("added: " + inputLine);
            }
            System.out.println(LINE);
        }
    }

    public static int listSize = 0;

    public static void printList(String[] list) {
        for (int i = 0; i < listSize; i++) {
            System.out.println(String.format("%d. %s", i+1, list[i]));
        }
    }
}