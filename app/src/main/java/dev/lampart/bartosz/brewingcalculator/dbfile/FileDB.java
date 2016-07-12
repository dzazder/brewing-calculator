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

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dicts.DictLanguages;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.entities.BCalcConf;
import dev.lampart.bartosz.brewingcalculator.entities.Language;

/**
 * Created by bartek on 01.06.2016.
 */
public class FileDB {
    private static String filename = "brcalcdb.txt";

    private static String strUnit = "unit";
    private static String strLang = "lang";
    private static String strRefr = "refr";
    private static String strWortFactor = "wort";
    private static String strPrimingSize = "prsize";
    private static String strVolumeUnit = "volunit";
    private static String strTemperature = "temp";
    private static String strTempUnit = "tempunit";
    private static String cfgSep = ";";
    private static String cfgValStart = "=";

    public static void saveConfiguration(BCalcConf conf, Context context) {
        String unit = setCfgValue(strUnit, conf.getDefExtractUnit().toString(), context);
        String useRefr = setCfgValue(strRefr, conf.isDefUseRefractometer() ? "1" : "0", context);
        String wort = setCfgValue(strWortFactor, Double.toString(conf.getDefWortCorrectionFactor()), context);
        String primingSize = setCfgValue(strPrimingSize, Double.toString(conf.getDefPrimingSize()), context);
        String volUnit = setCfgValue(strVolumeUnit, conf.getDefVolumeUnit().toString(), context);
        String temperature = setCfgValue(strTemperature, Double.toString(conf.getDefTemperature()), context);
        String tempUnit = setCfgValue(strTempUnit, conf.getDefTempUnit().toString(), context);

        String confString = unit + cfgSep + useRefr + cfgSep + wort + cfgSep + primingSize +
                cfgSep + volUnit + cfgSep + temperature + cfgSep + tempUnit + cfgSep;

        SaveDBFileContent(context, confString);
    }

    public static void saveDefaultUnit(ExtractUnit extUnit, Context context) {
        setCfgValue(strUnit, extUnit.toString(), context);
    }

    public static void saveDefaultLanguage(Language language, Context context) {
        setCfgValue(strLang, language.getLocale(), context);
    }

    /**
     * Save content to the file
     * @param context application context
     * @param content text to save
     */
    private static void SaveDBFileContent(Context context, String content) {
        try {
            Log.d("FileDB", "Try to save configuration: " + content);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
            Log.d("FileDB", "Configuration file saved successfully");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed" + e.toString());
        }
    }

    /**
     * File format: unit=Brix;lang=pl
     * @param context application context
     * @return configuration file content
     */
    private static String ReadDBFileContent(Context context) {
        String result = "";

        try {
            Log.d("FileDB", "Try to read configuration from file");
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                result = bufferedReader.readLine().trim();
                inputStream.close();
            }
            Log.d("FileDB", "Configuration read successfully: " + result);
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
        Log.d("FileDB", "Read configuration default extract unit");
        ExtractUnit defUnit = ExtractUnit.Plato;

        String content = ReadDBFileContent(context);
        String readUnit = getCfgValue(strUnit, content);
        if (readUnit.length() > 0) {
            defUnit = ExtractUnit.valueOf(readUnit);
        }

        Log.d("FileDB", "Default unit is: " + defUnit.toString());
        return defUnit;
    }

    public static boolean getDefaultUsingRefractometer(Context context) {
        Log.d("FileDB", "Read configuration default using refractometer");
        boolean useRefr = false;

        String content = ReadDBFileContent(context);
        String readRefr = getCfgValue(strRefr, content);
        if (readRefr.length() > 0 && readRefr.equals("1")) {
            useRefr = true;
        }

        Log.d("FileDB", "Default using refractometer is: " + Boolean.toString(useRefr));
        return useRefr;
    }

    public static double getDefaultWortCorrectionFactor(Context context) {
        Log.d("FileDB", "Read configuration default wort correction factor");
        String defWort = context.getResources().getString(R.string.lbl_wort_correction_factor_default_value);
        double defWortFactor = Double.parseDouble(defWort);

        String content = ReadDBFileContent(context);
        String readWort = getCfgValue(strWortFactor, content);
        if (readWort.length() > 0) {
            try {
                defWortFactor = Double.parseDouble(readWort);
            }
            catch (NumberFormatException e) {
                Log.e("FileDB", "Cannot convert read wort to double: " + readWort);
            }
        }

        Log.d("FileDB", "Default wort correction factor is: " + Double.toString(defWortFactor));
        return defWortFactor;
    }

    public static double getDefaultPrimingSize(Context context) {
        Log.d("FileDB", "Read configuration default priming size");
        // todo
        return 0;
    }

    public static Language getDefaultLanguage(Context context) {
        Log.d("FileDB", "Read configuration default language");
        String defLang = "en";

        String content = ReadDBFileContent(context);
        Log.d("FileDB", "Content of cfg: " + content);
        String readLang = getCfgValue(strLang, content);
        Log.d("FileDB", "Read language from cfg: " + readLang);
        if (readLang.length() > 0) {
            defLang = readLang;
        }



        Log.d("FileDB", "Default language is: " + defLang);
        return DictLanguages.getLanguageByLocale(defLang);
    }

    private static String getCfgValue(String type, String content) {
        Log.d("FileDB", "Split cfg: " + content + " with sep: " + cfgSep);
        String[] cfgValues = content.split(cfgSep);
        Log.d("FileDB", "After split have items: " + cfgValues.length);
        for (String s: cfgValues) {
            Log.d("FileDB", "Item in splitted: " + s);
            if (s.length() > 0 && s.contains(cfgValStart)) {
                String keyVal[] = s.split(cfgValStart);
                if (keyVal.length == 2) {
                    if (keyVal[0].equals(type)) {
                        Log.d("FileDB", "Return cfg value: " + keyVal[1]);
                        return keyVal[1];
                    }
                    else {
                        Log.d("FileDB", "First val is not a key: " + keyVal[0]);
                    }
                }
                else {
                    Log.d("FileDB", "After split has no 2 values: " + s);
                }
            }
            else {
                Log.d("FileDB", "Read value has is not cfg pair: " + s);
            }
        }


        return "";
    }

    private static String setCfgValue(String type, String value, Context context) {
        return type + cfgValStart + value;
        /*String cfgContent = ReadDBFileContent(context);
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
            newCfgContent = newCfgContent.substring(0, newCfgContent.length() - 1);
        }

        SaveDBFileContent(context, newCfgContent);*/

    }

}
