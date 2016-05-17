package dev.lampart.bartosz.brewingcalculator.helpers;

/**
 * Created by bartek on 17.05.2016.
 */
public class NumberFormatter {
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
