package dev.lampart.bartosz.brewingcalculator.dicts;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.entities.Language;

/**
 * Created by bartek on 08.06.2016.
 */
public class DictLanguages {
    public static final Language LANGUAGE_PL = new Language("Polski", "pl");
    public static final Language LANGUAGE_EN = new Language("English", "en");

    public static void setSpinner(Spinner spinner, Context context) {
        ArrayAdapter<String> spinnerArrayAdapter = getLanguageArrayAdapter(context);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public static ArrayAdapter<String> getLanguageArrayAdapter(Context context) {
        String[] languages = { LANGUAGE_EN.getLangName(), LANGUAGE_PL.getLangName() };
        //ArrayAdapter<String> retArray = new ArrayAdapter<String>(context, R.layout.bcalc_spinner_item, languages);
        ArrayAdapter<String> retArray = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, languages);
        retArray.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        return retArray;
    }

    public static Language getLanguageByName(String langName) {
        if (langName.equals(LANGUAGE_PL.getLangName())) {
            return LANGUAGE_PL;
        }
        if (langName.equals(LANGUAGE_EN.getLangName())) {
            return LANGUAGE_EN;
        }

        return LANGUAGE_EN;
    }

    public static Language getLanguageByLocale(String langLoc) {
        if (langLoc.equals(LANGUAGE_PL.getLocale())) {
            return LANGUAGE_PL;
        }
        if (langLoc.equals(LANGUAGE_EN.getLocale())) {
            return LANGUAGE_EN;
        }

        return LANGUAGE_EN;
    }
}
