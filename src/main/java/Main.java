package drpijon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Dr. Pijon command-line task manager.
 */
public class Main {
    private static final String INVALID_INPUT_MESSAGE = "Invalid input >:(";
    private static final String LINE_SEPARATOR = "____________________________________________________________";

    /**
     * Starts the Dr. Pijon application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        DrPijon drPijon = new DrPijon();
        List<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println(drPijon.getBanner());
        System.out.println(drPijon.getGreet());

        runCommandLoop(drPijon, tasks, scanner);
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     *
     * @param drPijon application messages
     * @param tasks stored tasks
     * @param scanner console input
     */
    private static void runCommandLoop(DrPijon drPijon, List<Task> tasks, Scanner scanner) {
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine().trim();
            if (!processCommand(inputLine, drPijon, tasks)) {
                return;
            }
        }
    }

    /**
     * Processes one command and returns whether command processing should continue.
     *
     * @param inputLine trimmed command line
     * @param drPijon application messages
     * @param tasks stored tasks
     * @return false when the user requested exit
     */
    private static boolean processCommand(String inputLine, DrPijon drPijon, List<Task> tasks) {
        String[] inputParts = inputLine.split("\\s+", 2);
        String command = inputParts[0];
        String taskDescription = (inputParts.length > 1) ? inputParts[1] : "";

        switch (command) {
        case "bye":
            System.out.println(drPijon.getGoodbye());
            return false;
        case "list":
            printList(tasks);
            break;
        case "mark":
            updateTaskStatus(inputParts, tasks, true, "COO COO! Task marked as COMPLETE:");
            break;
        case "unmark":
            updateTaskStatus(inputParts, tasks, false, "COO COO! Task unmarked:");
            break;
        case "todo":
            if (taskDescription.isEmpty()) {
                System.out.println(INVALID_INPUT_MESSAGE);
            } else {
                createTodoTask(taskDescription, tasks);
            }
            break;
        case "deadline":
            if (taskDescription.isEmpty()) {
                System.out.println(INVALID_INPUT_MESSAGE);
            } else {
                createDeadlineTask(taskDescription, tasks);
            }
            break;
        case "event":
            if (taskDescription.isEmpty()) {
                System.out.println(INVALID_INPUT_MESSAGE);
            } else {
                createEventTask(taskDescription, tasks);
            }
            break;
        default:
            System.out.println(INVALID_INPUT_MESSAGE);
            break;
        }
        System.out.println(LINE_SEPARATOR);
        return true;
    }

    private static void createEventTask(String taskDescription, List<Task> tasks) {
        String[] eventParts = taskDescription.split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[0].isBlank()
                || eventParts[1].isBlank() || eventParts[2].isBlank()) {
            System.out.println(INVALID_INPUT_MESSAGE);
            return;
        }

        Event event = new Event(
                eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        tasks.add(event);
        System.out.println("HMMMMMMMMM ok, Event added:");
        System.out.println(String.format(
                "  [E][ ] %s (from: %s to: %s)",
                event.getDescription(), event.getFrom(), event.getTo()));
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    private static void createDeadlineTask(String taskDescription, List<Task> tasks) {
        String[] deadlineParts = taskDescription.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            System.out.println(INVALID_INPUT_MESSAGE);
            return;
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        tasks.add(deadline);
        System.out.println("HMMMMMMMMM ok, Deadline added:");
        System.out.println(String.format("  [D][ ] %s (by: %s)", deadline.getDescription(), deadline.getBy()));
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    private static void createTodoTask(String taskDescription, List<Task> tasks) {
        Todo todo = new Todo(taskDescription);
        tasks.add(todo);
        System.out.println("HMMMMMMMMM ok, Todo added:");
        System.out.println(String.format("  [T][ ] %s", taskDescription));
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    /**
     * Updates a task's done status and prints the updated task.
     *
     * @param inputParts command and task number entered by the user
     * @param tasks stored tasks
     * @param newDoneStatus done status to apply
     * @param confirmationMessage message printed after a successful update
     */
    private static void updateTaskStatus(String[] inputParts, List<Task> tasks,
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

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("BOOOOOOOO! That task number does not exist.");
            return;
        }

        Task selectedTask = tasks.get(taskNumber - 1);
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
    private static void printList(List<Task> tasks) {
        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            char typeMarker = task.getTaskType();
            char statusMarker = task.isDone() ? 'X' : ' ';
            String taskDescription = task.getDescription();
            System.out.println(String.format("%d. [%c][%c] %s", i + 1, typeMarker, statusMarker, taskDescription));
        }
    }
}
