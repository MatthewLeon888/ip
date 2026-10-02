package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.task.Todo;
import drpijon.ui.Ui;

/**
 * Adds a todo task.
 */
public class TodoCommand extends AddCommand {
    /**
     * Creates a todo command.
     *
     * @param description todo description
     */
    public TodoCommand(String description) {
        super(description);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        if (getDescription().isBlank()) {
            throw new DrPijonException("OI TODO DESCRIPTION CANT BE EMPTY >:( Try: todo read book");
        }

        addTask(new Todo(getDescription()), tasks, ui, storage);
    }
}
