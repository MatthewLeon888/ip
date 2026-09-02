import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DrPijon drPijon = new DrPijon();
        String LINE = "____________________________________________________________";

        System.out.println(drPijon.banner);
        System.out.println(drPijon.greet);

        Todo[] todoList = new Todo[100];
        String inputLine;
        Scanner in = new Scanner(System.in);

        while (true) {
            inputLine = in.nextLine();

            String[] words = inputLine.split("\\s+");

            if (words[0].equals("bye")) {
                System.out.println(drPijon.goodbye);
                break;
            } else if (words[0].equals("list")) {
                printList(todoList);
            } else if (words[0].equals("mark")) {
                int markingIndex = Integer.parseInt(words[1]) - 1;
                todoList[markingIndex].setDone(true);
                System.out.println("COO COO! Task marked as COMPLETE:");
                printList(todoList);
            } else if (words[0].equals("unmark")) {
                int unmarkingIndex = Integer.parseInt(words[1]) - 1;
                todoList[unmarkingIndex].setDone(false);
                System.out.println("COO COO! Task unmarked:");
                printList(todoList);
            } else {
                todoList[listSize] = new Todo(inputLine);
                listSize++;
                System.out.println("added: " + inputLine);
            }
            System.out.println(LINE);
        }
    }

    public static int listSize = 0;

    public static void printList(Todo[] list) {
        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < listSize; i++) {
            char marker = ' ';
            if (list[i].isDone()) {
                marker = 'X';
            }
            System.out.println(String.format("%d. [%c] %s", i+1, marker, list[i].getDescription()));
        }
    }
}