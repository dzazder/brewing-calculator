package dev.lampart.bartosz.brewingcalculator.entities;

/**
 * Created by bartek on 08.06.2016.
 */
public class Language {
    private String langName;
    private String locale;

    public Language(String langName, String locale) {
        this.langName = langName;
        this.locale = locale;
    }

    public Language() {
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return langName;
    }
}
