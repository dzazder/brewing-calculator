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

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;

/**
 * Created by bartek on 01.06.2016.
 */
public class FileDB {
    private static String filename = "brcalcdb.txt";

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

    public static ExtractUnit getDefaultExtractUnit(Context context) {
        ExtractUnit defUnit = ExtractUnit.Plato;

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String resultFromFile = "";
                resultFromFile = bufferedReader.readLine().trim();
                inputStream.close();
                defUnit = ExtractUnit.valueOf(resultFromFile);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Read db", "Configuration file not found" + e.toString());
        }
        catch (IOException e) {
            Log.e("Read db", "File read failed" + e.toString());
        }

        return defUnit;
    }
}
