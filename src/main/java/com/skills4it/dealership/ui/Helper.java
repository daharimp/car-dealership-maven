package com.skills4it.dealership.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Helper {

    // ONE scanner for the whole app. All reads go through nextLine() so we
    // always consume the newline — that's what prevents the "phantom empty
    // input" bug where the next prompt gets an immediate Enter.
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("This field is required. Please try again.");
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            String input = readString(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public static int readPositiveInt(String prompt) {
        while (true) {
            int number = readInt(prompt);
            if (number >= 0) {
                return number;
            }
            System.out.println("Please enter a positive number.");
        }
    }

    public static int readYear(String prompt) {
        while (true) {
            int year = readInt(prompt);
            if (year >= 1886 && year <= 2100) {
                return year;
            }
            System.out.println("Please enter a realistic vehicle year between 1886 and 2100.");
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            String input = readString(prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number, for example 1995.00.");
            }
        }
    }

    public static double readPositiveDouble(String prompt) {
        while (true) {
            double number = readDouble(prompt);
            if (number >= 0) {
                return number;
            }
            System.out.println("Please enter a positive number.");
        }
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            String value = readRequiredString(prompt).toLowerCase();
            if (value.equals("yes") || value.equals("y")) return true;
            if (value.equals("no")  || value.equals("n")) return false;
            System.out.println("Please enter yes or no.");
        }
    }

    /**
     * Reads a date in YYYYMMDD format and returns it as a {@link LocalDate}.
     */
    public static LocalDate readLocalDate(String prompt) {
        while (true) {
            String value = readRequiredString(prompt);
            if (value.matches("\\d{8}")) {
                try {
                    return LocalDate.parse(value, YYYYMMDD);
                } catch (DateTimeParseException e) {
                    System.out.println("That is not a valid calendar date. Please try again.");
                    continue;
                }
            }
            System.out.println("Please use 8 digits in YYYYMMDD format, for example 20260514.");
        }
    }

    public static String readYyyyMmDdDate(String prompt) {
        while (true) {
            String value = readRequiredString(prompt);
            if (value.matches("\\d{8}")) {
                return value;
            }
            System.out.println("Please use 8 digits in YYYYMMDD format, for example 20260514.");
        }
    }

    public static void pause() {
        System.out.println();
        readString("Press Enter to continue...");
    }
}
