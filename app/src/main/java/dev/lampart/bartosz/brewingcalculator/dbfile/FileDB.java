package dev.lampart.bartosz.brewingcalculator.dbfile;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.AccessController;

import dev.lampart.bartosz.brewingcalculator.dicts.DictLanguages;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.entities.Language;

/**
 * Created by bartek on 01.06.2016.
 */
public class FileDB {
    private static String filename = "brcalcdb.txt";

    private static String strUnit = "unit";
    private static String strLang = "lang";
    private static String cfgSep = ";";
    private static String cfgValStart = "=";

    // todo: wykorzystanie setCfgValue
    public static void saveDefaultUnit(ExtractUnit extUnit, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(extUnit.toString());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed" + e.toString());
        }
    }

    public static void saveDefaultLanguage(Language language, Context context) {
        setCfgValue(strLang, language.getLocale(), context);
    }

    private static void SaveDBFileContent(Context context, String content) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed" + e.toString());
        }
    }

    /**
     * File format: unit=Brix;lang=pl
     * @param context
     * @return
     */
    private static String ReadDBFileContent(Context context) {
        String result = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                result = bufferedReader.readLine().trim();
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Read db", "Configuration file not found" + e.toString());
        }
        catch (IOException e) {
            Log.e("Read db", "File read failed" + e.toString());
        }

        return result;
    }

    public static ExtractUnit getDefaultExtractUnit(Context context) {
        Log.d("CFG", "Read configuration default extract unit");
        ExtractUnit defUnit = ExtractUnit.Plato;

        String content = ReadDBFileContent(context);
        String readUnit = getCfgValue(strUnit, content);
        if (readUnit.length() > 0) {
            defUnit = ExtractUnit.valueOf(readUnit);
        }

        Log.d("CFG", "Default unit is: " + defUnit.toString());
        return defUnit;
    }

    public static Language getDefaultLanguage(Context context) {
        Log.d("CFG", "Read configuration default language");
        String defLang = "en";

        String content = ReadDBFileContent(context);
        String readLang = getCfgValue(strLang, content);
        if (readLang.length() > 0) {
            defLang = readLang;
        }

        Log.d("CFG", "Default language is: " + defLang);
        return DictLanguages.getLanguageByLocale(defLang);
    }

    private static String getCfgValue(String type, String content) {
        String[] cfgValues = content.split(cfgSep);
        for (String s: cfgValues) {
            if (s.length() > 0 && s.contains(cfgValStart)) {
                String keyVal[] = s.split(cfgValStart);
                if (keyVal.length == 2) {
                    if (keyVal[0] == type) {
                        return keyVal[1];
                    }
                }
                else {
                    Log.d("CFG", "After split has no 2 values: " + s);
                }
            }
            else {
                Log.d("CFG", "Read value has is not cfg pair: " + s);
            }
        }

        return "";
    }

    private static void setCfgValue(String type, String value, Context context) {
        String cfgContent = ReadDBFileContent(context);
        boolean changed = false;

        String newCfgContent = "";
        String replaceCfg = "";

        String cfgs[] = cfgContent.split(cfgSep);
        for (String cfg: cfgs) {
            if (cfg.startsWith(type)) {
                newCfgContent += (type + cfgValStart + value + cfgSep);
                changed = true;
            }
            else {
                newCfgContent += cfg + cfgSep;
            }
        }

        if (!changed) {
            newCfgContent += cfgSep + type + cfgValStart + value;
        }

        if (newCfgContent.length() > 1 && newCfgContent.endsWith(cfgSep)) {
            newCfgContent = newCfgContent.substring(0, newCfgContent.length() - 2);
        }

        SaveDBFileContent(context, newCfgContent);
    }
}
