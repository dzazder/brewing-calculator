package dev.lampart.bartosz.brewingcalculator;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import dagger.hilt.android.HiltAndroidApp;
import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;

/**
 * Created by bartek on 09.06.2016.
 */
@HiltAndroidApp
public class BrewingCalculatorApplication extends Application {

    private static Context mContext = null;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (AppConfiguration.getInstance().defaultSettings == null) {
            Log.d("CONF", "default settings is null");
        }
        else {
            Log.d("CONF", "default settings is NOT null");
        }
        AppConfiguration.getInstance().defaultSettings.setDefExtractUnit(FileDB.getDefaultExtractUnit(this));
        AppConfiguration.getInstance().defaultSettings.setDefUseRefractometer(FileDB.getDefaultUsingRefractometer(this));
        AppConfiguration.getInstance().defaultSettings.setDefWortCorrectionFactor(FileDB.getDefaultWortCorrectionFactor(this));
    }

}
