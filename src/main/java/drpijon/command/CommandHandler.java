package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.parser.Parser;
import drpijon.storage.Storage;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Executes parsed commands and coordinates task updates, persistence, and responses.
 */
public class CommandHandler {
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates a command handler for the application's task list.
     *
     * @param tasks task list to update
     * @param storage storage used to persist updates
     * @param ui user interface used to display responses
     */
    public CommandHandler(TaskList tasks, Storage storage, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Executes a parsed command and displays any command error.
     *
     * @param parsedCommand parsed command and arguments
     * @return false when the user requested exit
     */
    public boolean execute(Parser.ParsedCommand parsedCommand) {
        try {
            return processCommand(parsedCommand);
        } catch (DrPijonException e) {
            ui.showError(e.getMessage());
            ui.showLineSeparator();
            return true;
        }
    }

    private boolean processCommand(Parser.ParsedCommand parsedCommand) throws DrPijonException {
        String command = parsedCommand.getCommand();
        String taskDescription = parsedCommand.getArguments();

        switch (command) {
        case "bye":
            Command exitCommand = new ExitCommand();
            executeCommand(exitCommand);
            return exitCommand.isExit();
        case "list":
            executeCommand(new ListCommand());
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
            executeCommand(new TodoCommand(taskDescription));
            break;
        case "deadline":
            executeCommand(new DeadlineCommand(taskDescription));
            break;
        case "event":
            executeCommand(new EventCommand(taskDescription));
            break;
        default:
            throw new DrPijonException("I DONT KNOW THAT COMMAND. Try: list, todo, deadline, event, mark, unmark, "
                    + "delete, or bye ^w^");
        }
        ui.showLineSeparator();
        return true;
    }

    private void executeCommand(Command command) throws DrPijonException {
        command.execute(tasks, ui, storage);
    }

    /**
     * Deletes the task at the specified one-based position.
     *
     * @param parsedCommand command and task number entered by the user
     * @throws DrPijonException when the task number is missing, invalid, or out of range
     */
    private void deleteTask(Parser.ParsedCommand parsedCommand) throws DrPijonException {
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

    /**
     * Updates a task's done status.
     *
     * @param parsedCommand command and task number entered by the user
     * @param newDoneStatus done status to apply
     * @throws DrPijonException when the task number is missing, invalid, or out of range
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
