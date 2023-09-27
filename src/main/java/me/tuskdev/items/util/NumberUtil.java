package me.tuskdev.items.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, Locale.forLanguageTag("pt-BR"));

    public static String format(double number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(long date) {
        return DATE_FORMAT.format(date);
    }

    public static int tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public static double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

}