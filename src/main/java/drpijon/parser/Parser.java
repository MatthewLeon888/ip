package drpijon.parser;

/**
 * Interprets raw console input as a command and its arguments.
 */
public class Parser {
    /**
     * Parses one line of console input.
     *
     * @param inputLine raw console input
     * @return parsed command and arguments
     */
    public ParsedCommand parse(String inputLine) {
        String[] inputParts = inputLine.trim().split("\\s+", 2);
        String command = inputParts[0];
        String arguments = inputParts.length > 1 ? inputParts[1] : "";
        return new ParsedCommand(command, arguments);
    }

    /**
     * Represents a command keyword and the text entered after it.
     */
    public static final class ParsedCommand {
        private final String command;
        private final String arguments;

        /**
         * Creates a parsed command.
         *
         * @param command command keyword
         * @param arguments text following the command keyword
         */
        public ParsedCommand(String command, String arguments) {
            this.command = command;
            this.arguments = arguments;
        }

        public String getCommand() {
            return command;
        }

        public String getArguments() {
            return arguments;
        }
    }
}
