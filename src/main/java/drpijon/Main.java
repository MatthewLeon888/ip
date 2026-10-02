package drpijon;

import drpijon.command.Command;
import drpijon.command.CommandHandler;
import drpijon.exception.DrPijonException;
import drpijon.parser.Parser;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Runs the Dr. Pijon command-line task manager.
 */
public class Main {
    private static final String TASK_FILE_PATH = "data/drpijon.txt";
    private final Ui ui;
    private final Storage storage;
    private final CommandHandler commandHandler;

    /**
     * Creates the application and loads its saved tasks.
     *
     * @param filePath path to the task data file
     */
    public Main(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (DrPijonException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }
        commandHandler = new CommandHandler(loadedTasks, storage, ui);
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
            try {
                Command command = Parser.parse(ui.readLine());
                if (!commandHandler.execute(command)) {
                    return;
                }
            } catch (DrPijonException e) {
                ui.showError(e.getMessage());
                ui.showLineSeparator();
            }
        }
    }
}
