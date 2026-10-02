package drpijon;

import drpijon.exception.DrPijonException;
import drpijon.task.Deadline;
import drpijon.task.Event;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.task.Todo;
import drpijon.ui.DrPijon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Dr. Pijon command-line task manager.
 */
public class Main {
    private static final String INVALID_INPUT_MESSAGE = "Bruhhhhh... Invalid input >:(";
    private static final String LINE_SEPARATOR = "____________________________________________________________";
    private static final Path TASK_FILE = Path.of("data", "drpijon.txt");

    /**
     * Starts the Dr. Pijon application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        DrPijon drPijon = new DrPijon();
        TaskList tasks = new TaskList();
        Scanner scanner = new Scanner(System.in);

        try {
            loadTasks(tasks);
        } catch (DrPijonException e) {
            System.out.println(e.getMessage());
        }

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
    private static void runCommandLoop(DrPijon drPijon, TaskList tasks, Scanner scanner) {
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine().trim();
            try {
                if (!processCommand(inputLine, drPijon, tasks)) {
                    return;
                }
            } catch (DrPijonException e) {
                System.out.println(e.getMessage());
                System.out.println(LINE_SEPARATOR);
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
     * @throws DrPijonException when the command or its arguments are invalid
     */
    private static boolean processCommand(String inputLine, DrPijon drPijon, TaskList tasks) throws DrPijonException {
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
        case "delete":
            deleteTask(inputParts, tasks);
            break;
        case "todo":
            createTodoTask(taskDescription, tasks);
            break;
        case "deadline":
            createDeadlineTask(taskDescription, tasks);
            break;
        case "event":
            createEventTask(taskDescription, tasks);
            break;
        default:
            throw new DrPijonException("I DONT KNOW THAT COMMAND. Try: list, todo, deadline, event, mark, unmark, "
                    + "delete, or bye ^w^");
        }
        System.out.println(LINE_SEPARATOR);
        return true;
    }

    /**
     * Deletes the task at the specified one-based position and prints it.
     *
     * @param inputParts command and task number entered by the user
     * @param tasks stored tasks
     * @throws DrPijonException when the task number is missing, invalid, or out of range
     */
    private static void deleteTask(String[] inputParts, TaskList tasks) throws DrPijonException {
        if (inputParts.length < 2) {
            throw new DrPijonException("BOOOOOOOO! Please specify a task number!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(inputParts[1]);
        } catch (NumberFormatException e) {
            throw new DrPijonException("BOOOOOOOO! Please specify a valid task number!");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DrPijonException("BOOOOOOOO! That task number does not exist!");
        }

        Task deletedTask = tasks.remove(taskNumber - 1);
        saveTasks(tasks);
        char typeMarker = deletedTask.getTaskType();
        char statusMarker = deletedTask.isDone() ? 'X' : ' ';
        System.out.println("COO COO! Task deleted:");
        System.out.println(String.format("  [%c][%c] %s", typeMarker, statusMarker, deletedTask.getDescription()));
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    private static void createEventTask(String taskDescription, TaskList tasks) throws DrPijonException {
        String[] eventParts = taskDescription.split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[0].isBlank() || eventParts[1].isBlank()
                || eventParts[2].isBlank()) {
            throw new DrPijonException("OI EVENT MUST INCLUDE /from AND /to >:( Try: event career fest /from 14 Sep "
                    + "/to 21 Sep");
        }

        Event event = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        tasks.add(event);
        saveTasks(tasks);
        System.out.println("HMMMMMMMMM ok, Event added:");
        System.out.println(String.format("  [E][ ] %s (from: %s to: %s)", event.getDescription(),
                event.getFrom(), event.getTo()));
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    private static void createDeadlineTask(String taskDescription, TaskList tasks) throws DrPijonException {
        String[] deadlineParts = taskDescription.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new DrPijonException("OI DEADLINE MUST INCLUDE /by >:( Try: deadline return book /by Sunday");
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        tasks.add(deadline);
        saveTasks(tasks);
        System.out.println("HMMMMMMMMM ok, Deadline added:");
        System.out.println(String.format("  [D][ ] %s (by: %s)", deadline.getDescription(), deadline.getBy()));
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    private static void createTodoTask(String taskDescription, TaskList tasks) throws DrPijonException {
        if (taskDescription.isBlank()) {
            throw new DrPijonException("OI TODO DESCRIPTION CANT BE EMPTY >:( Try: todo read book");
        }

        Todo todo = new Todo(taskDescription);
        tasks.add(todo);
        saveTasks(tasks);
        System.out.println("HMMMMMMMMM ok, Todo added:");
        System.out.println(String.format("  [T][ ] %s", todo.getDescription()));
        System.out.println(String.format("Now you have %d tasks in the list. ^w^", tasks.size()));
    }

    /**
     * Updates a task's done status and prints the updated task.
     *
     * @param inputParts command and task number entered by the user
     * @param tasks stored tasks
     * @param newDoneStatus done status to apply
     * @param confirmationMessage message printed after a successful update
     */
    private static void updateTaskStatus(String[] inputParts, TaskList tasks,
                                         boolean newDoneStatus, String confirmationMessage) throws DrPijonException {
        if (inputParts.length < 2) {
            throw new DrPijonException("BOOOOOOOO! Please specify a task number!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(inputParts[1]);
        } catch (NumberFormatException e) {
            throw new DrPijonException("BOOOOOOOO! Please specify a valid task number!");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DrPijonException("BOOOOOOOO! That task number does not exist!");
        }

        Task selectedTask = tasks.get(taskNumber - 1);
        selectedTask.setDone(newDoneStatus);
        saveTasks(tasks);
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
    private static void printList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("BEHOLD! AN EMPTY LIST ^w^");
            return;
        }
        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            char typeMarker = task.getTaskType();
            char statusMarker = task.isDone() ? 'X' : ' ';
            String taskDescription = formatTaskDetails(task);
            System.out.println(String.format("%d. [%c][%c] %s", i + 1, typeMarker, statusMarker, taskDescription));
        }
    }

    /**
     * Formats a task for display, including details specific to its task type.
     *
     * @param task task to format
     * @return task description with deadline or event details when applicable
     */
    private static String formatTaskDetails(Task task) {
        if (task instanceof Deadline deadline) {
            return String.format("%s (by: %s)", deadline.getDescription(), deadline.getBy());
        }
        if (task instanceof Event event) {
            return String.format("%s (from: %s to: %s)", event.getDescription(),
                    event.getFrom(), event.getTo());
        }
        return task.getDescription();
    }

    /**
     * Saves the current tasks in a simple, line-based format.
     *
     * @param tasks stored tasks
     * @throws DrPijonException when the task file cannot be written
     */
    private static void saveTasks(TaskList tasks) throws DrPijonException {
        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            String taskLine = String.format("%c | %d | %s", task.getTaskType(), task.isDone() ? 1 : 0,
                    task.getDescription());
            if (task instanceof Deadline deadline) {
                taskLine += String.format(" | %s", deadline.getBy());
            } else if (task instanceof Event event) {
                taskLine += String.format(" | %s | %s", event.getFrom(), event.getTo());
            }
            taskLines.add(taskLine);
        }

        try {
            Files.createDirectories(TASK_FILE.getParent());
            Files.write(TASK_FILE, taskLines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new DrPijonException("Could not save tasks to " + TASK_FILE + ".");
        }
    }

    /**
     * Loads tasks from the task file when it exists.
     *
     * @param tasks list to populate
     * @throws DrPijonException when the task file cannot be read or contains invalid data
     */
    private static void loadTasks(TaskList tasks) throws DrPijonException {
        if (!Files.exists(TASK_FILE)) {
            return;
        }

        try {
            for (String taskLine : Files.readAllLines(TASK_FILE, StandardCharsets.UTF_8)) {
                if (!taskLine.isBlank()) {
                    tasks.add(parseTaskLine(taskLine));
                }
            }
        } catch (IOException e) {
            throw new DrPijonException("Could not load tasks from " + TASK_FILE + ".");
        }
    }

    /**
     * Creates a task from one serialized task line.
     *
     * @param taskLine serialized task data
     * @return reconstructed task
     * @throws DrPijonException when the serialized data is invalid
     */
    private static Task parseTaskLine(String taskLine) throws DrPijonException {
        String[] taskParts = taskLine.split("\\s*\\|\\s*");
        if (taskParts.length < 3) {
            throw new DrPijonException("Could not load tasks from " + TASK_FILE + ".");
        }

        try {
            boolean isDone = Integer.parseInt(taskParts[1]) == 1;
            Task task;
            switch (taskParts[0]) {
            case "T":
                task = new Todo(taskParts[2]);
                break;
            case "D":
                task = new Deadline(taskParts[2], taskParts[3]);
                break;
            case "E":
                task = new Event(taskParts[2], taskParts[3], taskParts[4]);
                break;
            default:
                throw new DrPijonException("Could not load tasks from " + TASK_FILE + ".");
            }
            task.setDone(isDone);
            return task;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new DrPijonException("Could not load tasks from " + TASK_FILE + ".");
        }
    }
}
