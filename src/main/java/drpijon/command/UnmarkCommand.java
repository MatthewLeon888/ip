package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Marks a task as incomplete.
 */
public class UnmarkCommand extends TaskIndexCommand {
    /**
     * Creates an unmark command.
     *
     * @param arguments task number entered by the user
     */
    public UnmarkCommand(String arguments) {
        super(arguments);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        updateTaskStatus(tasks, ui, storage, false);
    }
}
