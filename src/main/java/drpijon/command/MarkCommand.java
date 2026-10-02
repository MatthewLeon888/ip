package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Marks a task as complete.
 */
public class MarkCommand extends TaskIndexCommand {
    /**
     * Creates a mark command.
     *
     * @param arguments task number entered by the user
     */
    public MarkCommand(String arguments) {
        super(arguments);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        updateTaskStatus(tasks, ui, storage, true);
    }
}
