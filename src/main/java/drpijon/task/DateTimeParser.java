package drpijon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses and serializes the date and date-time values used by tasks.
 */
public final class DateTimeParser {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    private DateTimeParser() {
    }

    /**
     * Parses a date or date-time value.
     *
     * @param text date or date-time text
     * @return parsed value, using midnight for date-only text
     * @throws DateTimeParseException when the text is not supported
     */
    public static LocalDateTime parse(String text) throws DateTimeParseException {
        String normalizedText = normalize(text);
        try {
            return LocalDateTime.parse(normalizedText, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(normalizedText).atStartOfDay();
        }
    }

    /**
     * Returns whether a date or date-time input includes an explicit time.
     *
     * @param text date or date-time text
     * @return true when the text includes a time component
     */
    public static boolean hasTime(String text) {
        return normalize(text).contains(" ");
    }

    /**
     * Converts a parsed value to the storage format.
     *
     * @param value parsed date or date-time value
     * @param hasTime whether the original input included a time
     * @return ISO date or date-time text
     */
    public static String formatForStorage(LocalDateTime value, boolean hasTime) {
        return hasTime ? value.format(DATE_TIME_FORMAT) : value.toLocalDate().toString();
    }

    private static String normalize(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }
}
