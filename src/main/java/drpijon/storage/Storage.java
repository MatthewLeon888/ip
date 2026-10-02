package drpijon.storage;

import drpijon.exception.DrPijonException;
import drpijon.task.Deadline;
import drpijon.task.Event;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from and saves tasks to the application's task file.
 */
public class Storage {
    private final Path taskFile;

    /**
     * Creates storage that uses the specified file.
     *
     * @param filePath path to the task file
     */
    public Storage(String filePath) {
        taskFile = Path.of(filePath);
    }

    /**
     * Loads all tasks from the task file.
     *
     * @return tasks reconstructed from the file, or an empty list when the file does not exist
     * @throws DrPijonException when the task file cannot be read or contains invalid data
     */
    public TaskList load() throws DrPijonException {
        TaskList tasks = new TaskList();
        if (!Files.exists(taskFile)) {
            return tasks;
        }

        try {
            for (String taskLine : Files.readAllLines(taskFile, StandardCharsets.UTF_8)) {
                if (!taskLine.isBlank()) {
                    tasks.add(parseTaskLine(taskLine));
                }
            }
        } catch (IOException e) {
            throw createLoadException();
        }
        return tasks;
    }

    /**
     * Saves all tasks in a simple, line-based format.
     *
     * @param tasks tasks to save
     * @throws DrPijonException when the task file cannot be written
     */
    public void save(TaskList tasks) throws DrPijonException {
        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(serializeTask(task));
        }

        try {
            if (taskFile.getParent() != null) {
                Files.createDirectories(taskFile.getParent());
            }
            Files.write(taskFile, taskLines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new DrPijonException("Could not save tasks to " + taskFile + ".");
        }
    }

    /**
     * Converts a task into its line-based storage representation.
     *
     * @param task task to serialize
     * @return serialized task data
     */
    private String serializeTask(Task task) {
        String taskLine = String.format("%c | %d | %s", task.getTaskType(), task.isDone() ? 1 : 0,
                task.getDescription());
        if (task instanceof Deadline deadline) {
            return taskLine + String.format(" | %s", deadline.getBy());
        }
        if (task instanceof Event event) {
            return taskLine + String.format(" | %s | %s", event.getFrom(), event.getTo());
        }
        return taskLine;
    }

    /**
     * Reconstructs a task from one serialized task line.
     *
     * @param taskLine serialized task data
     * @return reconstructed task
     * @throws DrPijonException when the serialized data is invalid
     */
    private Task parseTaskLine(String taskLine) throws DrPijonException {
        String[] taskParts = taskLine.split("\\s*\\|\\s*");
        if (taskParts.length < 3) {
            throw createLoadException();
        }

        try {
            boolean isDone = Integer.parseInt(taskParts[1]) == 1;
            Task task;
            switch (taskParts[0]) {
            case "T":
                task = new Todo(taskParts[2]);
                break;
            case "D":
                task = new Deadline(taskParts[2], taskParts[3]);
                break;
            case "E":
                task = new Event(taskParts[2], taskParts[3], taskParts[4]);
                break;
            default:
                throw createLoadException();
            }
            task.setDone(isDone);
            return task;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw createLoadException();
        }
    }

    /**
     * Creates the standard user-facing error for a failed load operation.
     *
     * @return load failure exception
     */
    private DrPijonException createLoadException() {
        return new DrPijonException("Could not load tasks from " + taskFile + ".");
    }
}
