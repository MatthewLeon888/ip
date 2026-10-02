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
    private static final String INVALID_INPUT_MESSAGE = "Bruhhhhh... Invalid input >:(";
    private static final String TASK_FILE_PATH = "data/drpijon.txt";

    /**
     * Starts the Dr. Pijon application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(TASK_FILE_PATH);
        TaskList tasks;

        try {
            tasks = storage.load();
        } catch (DrPijonException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }

        ui.showWelcome();

        runCommandLoop(tasks, storage, new Parser(), ui);
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     *
     * @param tasks stored tasks
     * @param storage task file storage
     * @param parser command parser
     * @param ui console user interface
     */
    private static void runCommandLoop(TaskList tasks, Storage storage, Parser parser, Ui ui) {
        while (ui.hasNextLine()) {
            Parser.ParsedCommand parsedCommand = parser.parse(ui.readLine());
            try {
                if (!processCommand(parsedCommand, tasks, storage, ui)) {
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
     * @param tasks stored tasks
     * @param storage task file storage
     * @param ui console user interface
     * @return false when the user requested exit
     * @throws DrPijonException when the command or its arguments are invalid
     */
    private static boolean processCommand(Parser.ParsedCommand parsedCommand, TaskList tasks,
                                          Storage storage, Ui ui) throws DrPijonException {
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
            updateTaskStatus(parsedCommand, tasks, storage, true, ui);
            break;
        case "unmark":
            updateTaskStatus(parsedCommand, tasks, storage, false, ui);
            break;
        case "delete":
            deleteTask(parsedCommand, tasks, storage, ui);
            break;
        case "todo":
            createTodoTask(taskDescription, tasks, storage, ui);
            break;
        case "deadline":
            createDeadlineTask(taskDescription, tasks, storage, ui);
            break;
        case "event":
            createEventTask(taskDescription, tasks, storage, ui);
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
     * @param tasks stored tasks
     * @param storage task file storage
     * @param ui console user interface
     * @throws DrPijonException when the task number is missing, invalid, or out of range
     */
    private static void deleteTask(Parser.ParsedCommand parsedCommand, TaskList tasks, Storage storage, Ui ui)
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

    private static void createEventTask(String taskDescription, TaskList tasks, Storage storage, Ui ui)
            throws DrPijonException {
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

    private static void createDeadlineTask(String taskDescription, TaskList tasks, Storage storage, Ui ui)
            throws DrPijonException {
        String[] deadlineParts = taskDescription.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new DrPijonException("OI DEADLINE MUST INCLUDE /by >:( Try: deadline return book /by Sunday");
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        tasks.add(deadline);
        storage.save(tasks);
        ui.showTaskAdded(deadline, tasks.size());
    }

    private static void createTodoTask(String taskDescription, TaskList tasks, Storage storage, Ui ui)
            throws DrPijonException {
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
     * @param tasks stored tasks
     * @param storage task file storage
     * @param newDoneStatus done status to apply
     * @param ui console user interface
     */
    private static void updateTaskStatus(Parser.ParsedCommand parsedCommand, TaskList tasks, Storage storage,
                                         boolean newDoneStatus, Ui ui)
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
