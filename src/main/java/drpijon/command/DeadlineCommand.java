package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.Deadline;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Adds a deadline task.
 */
public class DeadlineCommand extends AddCommand {
    /**
     * Creates a deadline command.
     *
     * @param description deadline description and due date
     */
    public DeadlineCommand(String description) {
        super(description);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        String[] deadlineParts = getDescription().split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new DrPijonException("OI DEADLINE MUST INCLUDE /by >:( Try: deadline return book /by Sunday");
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        addTask(deadline, tasks, ui, storage);
    }
}
