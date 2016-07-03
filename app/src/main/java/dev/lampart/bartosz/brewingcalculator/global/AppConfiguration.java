package dev.lampart.bartosz.brewingcalculator.global;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.entities.BCalcConf;
import dev.lampart.bartosz.brewingcalculator.entities.Language;

/**
 * Created by bartek on 01.06.2016.
 */
public class AppConfiguration {
    private static AppConfiguration mInstance = null;

    public BCalcConf defaultSettings;

    protected AppConfiguration() {
        defaultSettings = new BCalcConf();
    }

    public static synchronized AppConfiguration getInstance() {
        if (null == mInstance) {
            mInstance = new AppConfiguration();
        }
        return mInstance;
    }
}
