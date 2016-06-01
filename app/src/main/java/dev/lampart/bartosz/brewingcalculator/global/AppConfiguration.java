package dev.lampart.bartosz.brewingcalculator.global;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;

/**
 * Created by bartek on 01.06.2016.
 */
public class AppConfiguration {
    private static AppConfiguration mInstance = null;

    public ExtractUnit defaultExtractUnit;

    protected AppConfiguration() {}

    public static synchronized AppConfiguration getInstance() {
        if (null == mInstance) {
            mInstance = new AppConfiguration();
        }
        return mInstance;
    }
}
