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
                updateTaskStatus(words, todoList, true, "COO COO! Task marked as COMPLETE:");
            } else if (words[0].equals("unmark")) {
                updateTaskStatus(words, todoList, false, "COO COO! Task unmarked:");
            } else {
                todoList[listSize] = new Todo(inputLine);
                listSize++;
                System.out.println("added: " + inputLine);
            }
            System.out.println(LINE);
        }
    }

    /**
     * Updates a task's done status and prints the updated task.
     *
     * @param commandParts command and task number entered by the user
     * @param tasks stored tasks
     * @param newDoneStatus done status to apply
     * @param confirmationMessage message printed after a successful update
     */
    private static void updateTaskStatus(String[] commandParts, Todo[] tasks,
            boolean newDoneStatus, String confirmationMessage) {
        if (commandParts.length < 2) {
            System.out.println("Please specify a task number.");
            return;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Please specify a valid task number.");
            return;
        }

        if (taskNumber < 1 || taskNumber > listSize || tasks[taskNumber - 1] == null) {
            System.out.println("That task number does not exist.");
            return;
        }

        Todo selectedTask = tasks[taskNumber - 1];
        selectedTask.setDone(newDoneStatus);
        System.out.println(confirmationMessage);
        char marker = selectedTask.isDone() ? 'X' : ' ';
        System.out.println(String.format("%d. [%c] %s", taskNumber, marker, selectedTask.getDescription()));
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
