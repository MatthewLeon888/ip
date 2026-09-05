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
            String inputLine = scanner.nextLine().trim();
            String[] inputParts = inputLine.split("\\s+", 2);
            String command = inputParts[0];
            String taskDescription = (inputParts.length > 1) ? inputParts[1] : "";

            if (command.equals("bye")) {
                System.out.println(drPijon.getGoodbye());
                break;
            } else if (command.equals("list")) {
                printList(tasks);
            } else if (command.equals("mark")) {
                updateTaskStatus(inputParts, tasks, true, "COO COO! Task marked as COMPLETE:");
            } else if (command.equals("unmark")) {
                updateTaskStatus(inputParts, tasks, false, "COO COO! Task unmarked:");
            } else if (command.equals("todo") && !taskDescription.isEmpty()) {
                tasks[taskCount] = new Todo(taskDescription);
                taskCount++;
                System.out.println("HMMMMMMMMM ok, Todo added: " + taskDescription);
                System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
            } else if (command.equals("deadline") && !taskDescription.isEmpty()) {
                tasks[taskCount] = new Deadline(taskDescription, "placeholder1");
                taskCount++;
                System.out.println("HMMMMMMMMM ok, Deadline added: " + taskDescription);
                System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
            } else if (command.equals("event") && !taskDescription.isEmpty()) {
                tasks[taskCount] = new Event(taskDescription, "placeholder1", "placeholder2");
                taskCount++;
                System.out.println("HMMMMMMMMM ok, Event added: " + taskDescription);
                System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
            } else {
                tasks[taskCount] = new Task(inputLine);
                taskCount++;
                System.out.println("added: " + inputLine);
                System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
            }
            System.out.println(LINE_SEPARATOR);
        }
    }

    /**
     * Updates a task's done status and prints the updated task.
     *
     * @param inputParts command and task number entered by the user
     * @param tasks stored tasks
     * @param newDoneStatus done status to apply
     * @param confirmationMessage message printed after a successful update
     */
    private static void updateTaskStatus(String[] inputParts, Task[] tasks,
                                         boolean newDoneStatus, String confirmationMessage) {
        if (inputParts.length < 2) {
            System.out.println("BOOOOOOOO! please specify a task number.");
            return;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(inputParts[1]);
        } catch (NumberFormatException e) {
            System.out.println("BOOOOOOOO! Please specify a valid task number.");
            return;
        }

        if (taskNumber < 1 || taskNumber > taskCount || tasks[taskNumber - 1] == null) {
            System.out.println("BOOOOOOOO! That task number does not exist.");
            return;
        }

        Task selectedTask = tasks[taskNumber - 1];
        selectedTask.setDone(newDoneStatus);
        System.out.println(confirmationMessage);
        char typeMarker = selectedTask.getTaskType();
        char statusMarker = selectedTask.isDone() ? 'X' : ' ';
        System.out.println(String.format("  [%c][%c] %s", typeMarker, statusMarker, selectedTask.getDescription()));
    }

    /**
     * Prints all tasks and their current done status.
     *
     * @param tasks stored tasks
     */
    private static void printList(Task[] tasks) {
        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < taskCount; i++) {
            char typeMarker = tasks[i].getTaskType();
            char statusMarker = tasks[i].isDone() ? 'X' : ' ';
            String taskDescription = tasks[i].getDescription();
            System.out.println(String.format("%d. [%c][%c] %s", i + 1, typeMarker, statusMarker, taskDescription));
        }
    }
}
