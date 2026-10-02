package drpijon.ui;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import drpijon.task.Deadline;
import drpijon.task.Event;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.task.Todo;

/**
 * Handles console input and common user-facing messages.
 */
public class Ui {
    private static final String LINE_SEPARATOR = "____________________________________________________________";
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy",
            Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
            "MMM dd yyyy, h:mm a", Locale.ENGLISH);

    private final DrPijon messages;
    private final Scanner scanner;

    /**
     * Creates a console user interface connected to standard input.
     */
    public Ui() {
        messages = new DrPijon();
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another console line is available.
     *
     * @return true when another input line is available
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next raw console line.
     *
     * @return next console line
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Displays the application banner and greeting.
     */
    public void showWelcome() {
        System.out.println(messages.getBanner());
        System.out.println(messages.getGreet());
    }

    /**
     * Displays the application goodbye message.
     */
    public void showGoodbye() {
        System.out.println(messages.getGoodbye());
    }

    /**
     * Displays a user-facing error message.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the separator used between command responses.
     */
    public void showLineSeparator() {
        System.out.println(LINE_SEPARATOR);
    }

    /**
     * Displays all tasks and their current done status.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("BEHOLD! AN EMPTY LIST ^w^");
            return;
        }

        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            char typeMarker = task.getTaskType();
            char statusMarker = task.isDone() ? 'X' : ' ';
            System.out.println(String.format("%d. [%c][%c] %s", i + 1, typeMarker, statusMarker,
                    formatTaskDetails(task)));
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task added task
     * @param taskCount number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        String taskType = getTaskTypeName(task);
        System.out.println(String.format("HMMMMMMMMM ok, %s added:", taskType));
        System.out.println(String.format("  [%c][%c] %s", task.getTaskType(), task.isDone() ? 'X' : ' ',
                formatTaskDetails(task)));
        String suffix = task instanceof Todo ? " ^w^" : "";
        System.out.println(String.format("Now you have %d tasks in the list.%s", taskCount, suffix));
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task deleted task
     * @param taskCount number of tasks after the deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("COO COO! Task deleted:");
        showTaskSummary(task);
        System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
    }

    /**
     * Displays confirmation that a task was marked as complete.
     *
     * @param task updated task
     */
    public void showTaskMarked(Task task) {
        System.out.println("COO COO! Task marked as COMPLETE:");
        showTaskSummary(task);
    }

    /**
     * Displays confirmation that a task was unmarked.
     *
     * @param task updated task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("COO COO! Task unmarked:");
        showTaskSummary(task);
    }

    private void showTaskSummary(Task task) {
        char typeMarker = task.getTaskType();
        char statusMarker = task.isDone() ? 'X' : ' ';
        System.out.println(String.format("  [%c][%c] %s", typeMarker, statusMarker, task.getDescription()));
    }

    private String formatTaskDetails(Task task) {
        if (task instanceof Deadline deadline) {
            String by;
            if (!deadline.hasDate()) {
                by = deadline.getByText();
            } else if (deadline.hasTime()) {
                by = deadline.getBy().format(DISPLAY_DATE_TIME_FORMAT);
            } else {
                by = deadline.getBy().format(DISPLAY_DATE_FORMAT);
            }
            return String.format("%s (by: %s)", deadline.getDescription(), by);
        }
        if (task instanceof Event event) {
            return String.format("%s (from: %s to: %s)", event.getDescription(), event.getFrom(), event.getTo());
        }
        return task.getDescription();
    }

    private String getTaskTypeName(Task task) {
        if (task instanceof Todo) {
            return "Todo";
        }
        if (task instanceof Deadline) {
            return "Deadline";
        }
        return "Event";
    }
}
