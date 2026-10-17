package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    // Các định dạng ngày được chấp nhận khi nhập.
    private static final List<DateTimeFormatter> INPUT_FORMATTERS =
            Arrays.asList(
                    createFormatter("uuuu-MM-dd"),
                    createFormatter("M/d/uuuu"),
                    createFormatter("d/M/uuuu"),
                    createFormatter("M-d-uuuu"),
                    createFormatter("d-M-uuuu"),
                    createFormatter("d.M.uuuu"),
                    createFormatter("uuuu/M/d"),
                    createFormatter("uuuu.M.d")
            );

    // Định dạng ngày khi in ra, giống mẫu trong đề bài.
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            createFormatter("MM/dd/uuuu");

    private static DateTimeFormatter createFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern, Locale.US)
                .withResolverStyle(ResolverStyle.STRICT);
    }

    // Chuyển ngày người dùng nhập thành LocalDate.
    public static LocalDate parseDate(String input)
            throws DateTimeParseException {

        if (input == null || input.trim().isEmpty()) {
            throw new DateTimeParseException(
                    "Date cannot be empty.",
                    input == null ? "" : input,
                    0
            );
        }

        String dateText = input.trim();

        for (DateTimeFormatter formatter : INPUT_FORMATTERS) {
            try {
                return LocalDate.parse(dateText, formatter);
            } catch (DateTimeParseException e) {
                // Không đúng định dạng này thì thử định dạng tiếp theo.
            }
        }

        throw new DateTimeParseException(
                "Invalid date format.",
                dateText,
                0
        );
    }

    // Luôn in ngày theo định dạng MM/dd/yyyy.
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "N/A";
        }

        return date.format(OUTPUT_FORMATTER);
    }
}