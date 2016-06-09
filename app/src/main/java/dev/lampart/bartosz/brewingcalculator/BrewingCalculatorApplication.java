package dev.lampart.bartosz.brewingcalculator;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.entities.Language;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;

/**
 * Created by bartek on 09.06.2016.
 */
public class BrewingCalculatorApplication extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        //initSingletons();


        // todo move to app_start method
        AppConfiguration.getInstance().defaultExtractUnit = FileDB.getDefaultExtractUnit(this);
        AppConfiguration.getInstance().defaultLanguage = FileDB.getDefaultLanguage(this);
        setLanguage(AppConfiguration.getInstance().defaultLanguage);
    }

    public void setLanguage(Language language) {
        Locale locale = new Locale(language.getLocale());
        Log.d("LANG", "setLanguage in Application class");
        Log.d("LANG", "NEW: " +  locale.getLanguage());
        Log.d("LANG", "OLD: " + getResources().getConfiguration().locale.getLanguage());

        if (locale.getLanguage() != getResources().getConfiguration().locale.getLanguage()) {
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            this.getApplicationContext().getResources().updateConfiguration(config, null);

        }

    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
        //MySingleton.initInstance();
    }

    public void customAppMethod()
    {
        // Custom application method
    }
}
