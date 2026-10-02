package drpijon;

import drpijon.exception.DrPijonException;
import drpijon.parser.Parser;
import drpijon.storage.Storage;
import drpijon.task.Deadline;
import drpijon.task.Event;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.task.Todo;
import drpijon.ui.Ui;

/**
 * Runs the Dr. Pijon command-line task manager.
 */
public class Main {
    private static final String TASK_FILE_PATH = "data/drpijon.txt";
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private final TaskList tasks;

    /**
     * Creates the application and loads its saved tasks.
     *
     * @param filePath path to the task data file
     */
    public Main(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();

        TaskList loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (DrPijonException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Starts the Dr. Pijon application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Main(TASK_FILE_PATH).run();
    }

    /**
     * Runs the application until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        runCommandLoop();
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private void runCommandLoop() {
        while (ui.hasNextLine()) {
            Parser.ParsedCommand parsedCommand = parser.parse(ui.readLine());
            try {
                if (!processCommand(parsedCommand)) {
                    return;
                }
            } catch (DrPijonException e) {
                ui.showError(e.getMessage());
                ui.showLineSeparator();
            }
        }
    }

    /**
     * Processes one command and returns whether command processing should continue.
     *
     * @param parsedCommand parsed command and arguments
     * @return false when the user requested exit
     * @throws DrPijonException when the command or its arguments are invalid
     */
    private boolean processCommand(Parser.ParsedCommand parsedCommand) throws DrPijonException {
        String command = parsedCommand.getCommand();
        String taskDescription = parsedCommand.getArguments();

        switch (command) {
        case "bye":
            ui.showGoodbye();
            return false;
        case "list":
            ui.showTaskList(tasks);
            break;
        case "mark":
            updateTaskStatus(parsedCommand, true);
            break;
        case "unmark":
            updateTaskStatus(parsedCommand, false);
            break;
        case "delete":
            deleteTask(parsedCommand);
            break;
        case "todo":
            createTodoTask(taskDescription);
            break;
        case "deadline":
            createDeadlineTask(taskDescription);
            break;
        case "event":
            createEventTask(taskDescription);
            break;
        default:
            throw new DrPijonException("I DONT KNOW THAT COMMAND. Try: list, todo, deadline, event, mark, unmark, "
                    + "delete, or bye ^w^");
        }
        ui.showLineSeparator();
        return true;
    }

    /**
     * Deletes the task at the specified one-based position and prints it.
     *
     * @param parsedCommand command and task number entered by the user
     * @throws DrPijonException when the task number is missing, invalid, or out of range
     */
    private void deleteTask(Parser.ParsedCommand parsedCommand)
            throws DrPijonException {
        if (parsedCommand.getArguments().isEmpty()) {
            throw new DrPijonException("BOOOOOOOO! Please specify a task number!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parsedCommand.getArguments());
        } catch (NumberFormatException e) {
            throw new DrPijonException("BOOOOOOOO! Please specify a valid task number!");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DrPijonException("BOOOOOOOO! That task number does not exist!");
        }

        Task deletedTask = tasks.remove(taskNumber - 1);
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    private void createEventTask(String taskDescription) throws DrPijonException {
        String[] eventParts = taskDescription.split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[0].isBlank() || eventParts[1].isBlank()
                || eventParts[2].isBlank()) {
            throw new DrPijonException("OI EVENT MUST INCLUDE /from AND /to >:( Try: event career fest /from 14 Sep "
                    + "/to 21 Sep");
        }

        Event event = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        tasks.add(event);
        storage.save(tasks);
        ui.showTaskAdded(event, tasks.size());
    }

    private void createDeadlineTask(String taskDescription) throws DrPijonException {
        String[] deadlineParts = taskDescription.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new DrPijonException("OI DEADLINE MUST INCLUDE /by >:( Try: deadline return book /by Sunday");
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        tasks.add(deadline);
        storage.save(tasks);
        ui.showTaskAdded(deadline, tasks.size());
    }

    private void createTodoTask(String taskDescription) throws DrPijonException {
        if (taskDescription.isBlank()) {
            throw new DrPijonException("OI TODO DESCRIPTION CANT BE EMPTY >:( Try: todo read book");
        }

        Todo todo = new Todo(taskDescription);
        tasks.add(todo);
        storage.save(tasks);
        ui.showTaskAdded(todo, tasks.size());
    }

    /**
     * Updates a task's done status and prints the updated task.
     *
     * @param parsedCommand command and task number entered by the user
     * @param newDoneStatus done status to apply
     */
    private void updateTaskStatus(Parser.ParsedCommand parsedCommand, boolean newDoneStatus)
            throws DrPijonException {
        if (parsedCommand.getArguments().isEmpty()) {
            throw new DrPijonException("BOOOOOOOO! Please specify a task number!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parsedCommand.getArguments());
        } catch (NumberFormatException e) {
            throw new DrPijonException("BOOOOOOOO! Please specify a valid task number!");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DrPijonException("BOOOOOOOO! That task number does not exist!");
        }

        Task selectedTask = tasks.get(taskNumber - 1);
        selectedTask.setDone(newDoneStatus);
        storage.save(tasks);
        if (newDoneStatus) {
            ui.showTaskMarked(selectedTask);
        } else {
            ui.showTaskUnmarked(selectedTask);
        }
    }

}
