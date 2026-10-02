package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.parser.Parser;
import drpijon.storage.Storage;
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
            executeCommand(new MarkCommand(taskDescription));
            break;
        case "unmark":
            executeCommand(new UnmarkCommand(taskDescription));
            break;
        case "delete":
            executeCommand(new DeleteCommand(taskDescription));
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

}
