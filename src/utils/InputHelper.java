package utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static String readString(String message) {
        System.out.print(message);
        return SCANNER.nextLine().trim();
    }

    public static String readRequiredString(String message) {
        while (true) {
            String value = readString(message);

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    public static int readInt(String message) {
        while (true) {
            String input = readString(message);

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public static long readLong(String message) {
        while (true) {
            String input = readString(message);

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public static int readIntInRange(
            String message, int minimum, int maximum) {

        while (true) {
            int value = readInt(message);

            if (value >= minimum && value <= maximum) {
                return value;
            }

            System.out.println(
                    "Please enter a number from "
                    + minimum + " to " + maximum + "."
            );
        }
    }

    public static LocalDate readDate(String message) {
    while (true) {
        String input = readString(
                message + " (multiple formats accepted): "
        );

        try {
            return DateUtils.parseDate(input);
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Invalid date. Try MM/dd/yyyy, dd/MM/yyyy, "
                    + "yyyy-MM-dd, dd-MM-yyyy, or dd.MM.yyyy."
            );
        }
    }
}

    public static boolean readYesNo(String message) {
        while (true) {
            String answer = readString(message).toUpperCase();

            if (answer.equals("Y") || answer.equals("YES")) {
                return true;
            }

            if (answer.equals("N") || answer.equals("NO")) {
                return false;
            }

            System.out.println("Please enter Y or N.");
        }
    }
}