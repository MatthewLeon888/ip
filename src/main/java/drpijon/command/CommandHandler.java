package drpijon.command;

import drpijon.exception.DrPijonException;
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
     * Executes a command and displays any execution error.
     *
     * @param command command to execute
     * @return false when the command requests application exit
     */
    public boolean execute(Command command) {
        try {
            command.execute(tasks, ui, storage);
        } catch (DrPijonException e) {
            ui.showError(e.getMessage());
        }
        if (!command.isExit()) {
            ui.showLineSeparator();
        }
        return !command.isExit();
    }

}
