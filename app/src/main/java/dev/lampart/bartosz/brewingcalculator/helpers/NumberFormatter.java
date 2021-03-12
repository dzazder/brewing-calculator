package dev.lampart.bartosz.brewingcalculator.helpers;

import android.widget.EditText;

/**
 * Created by bartek on 17.05.2016.
 */
public class NumberFormatter {
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static double getDoubleValue(EditText field) {
        if (isNumeric(field.getText().toString())) {
            return Double.parseDouble(field.getText().toString());
        }

        return 0;
    }
}
