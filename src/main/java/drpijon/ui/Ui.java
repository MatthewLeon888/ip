package drpijon.ui;

import java.util.Scanner;

/**
 * Handles console input and common user-facing messages.
 */
public class Ui {
    private static final String LINE_SEPARATOR = "____________________________________________________________";

    private final DrPijon messages;
    private final Scanner scanner;

    /**
     * Creates a console user interface connected to standard input.
     */
    public Ui() {
        messages = new DrPijon();
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another console line is available.
     *
     * @return true when another input line is available
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next raw console line.
     *
     * @return next console line
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Displays the application banner and greeting.
     */
    public void showWelcome() {
        System.out.println(messages.getBanner());
        System.out.println(messages.getGreet());
    }

    /**
     * Displays the application goodbye message.
     */
    public void showGoodbye() {
        System.out.println(messages.getGoodbye());
    }

    /**
     * Displays a user-facing error message.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the separator used between command responses.
     */
    public void showLineSeparator() {
        System.out.println(LINE_SEPARATOR);
    }
}
