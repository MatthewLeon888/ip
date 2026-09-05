/**
 * Runs the Dr. Pijon command-line task manager.
 */
import java.util.Scanner;

public class Main {
    private static final int MAX_TASKS = 100;
    private static final String LINE_SEPARATOR = "____________________________________________________________";

    private static int taskCount = 0;

    /**
     * Starts the Dr. Pijon application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        DrPijon drPijon = new DrPijon();
        Task[] tasks = new Task[MAX_TASKS];
        Scanner scanner = new Scanner(System.in);

        System.out.println(drPijon.getBanner());
        System.out.println(drPijon.getGreet());

        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();
            String[] commandParts = inputLine.split("\\s+");

            if (commandParts[0].equals("bye")) {
                System.out.println(drPijon.getGoodbye());
                break;
            } else if (commandParts[0].equals("list")) {
                printList(tasks);
            } else if (commandParts[0].equals("mark")) {
                updateTaskStatus(commandParts, tasks, true,
                        "COO COO! Task marked as COMPLETE:");
            } else if (commandParts[0].equals("unmark")) {
                updateTaskStatus(commandParts, tasks, false, "COO COO! Task unmarked:");
            } else {
                tasks[taskCount] = new Todo(inputLine);
                taskCount++;
                System.out.println("added: " + inputLine);
            }
            System.out.println(LINE_SEPARATOR);
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
    private static void updateTaskStatus(String[] commandParts, Task[] tasks,
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

        if (taskNumber < 1 || taskNumber > taskCount || tasks[taskNumber - 1] == null) {
            System.out.println("That task number does not exist.");
            return;
        }

        Task selectedTask = tasks[taskNumber - 1];
        selectedTask.setDone(newDoneStatus);
        System.out.println(confirmationMessage);
        char marker = selectedTask.isDone() ? 'X' : ' ';
        System.out.println(String.format("  [%c] %s", marker, selectedTask.getDescription()));
    }

    /**
     * Prints all tasks and their current done status.
     *
     * @param tasks stored tasks
     */
    private static void printList(Task[] tasks) {
        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < taskCount; i++) {
            char marker = tasks[i].isDone() ? 'X' : ' ';
            System.out.println(String.format("%d. [%c] %s", i + 1, marker,
                    tasks[i].getDescription()));
        }
    }
}
