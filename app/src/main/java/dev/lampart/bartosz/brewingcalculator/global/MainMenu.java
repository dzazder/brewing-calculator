package dev.lampart.bartosz.brewingcalculator.global;

import android.app.Application;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.BrewingCalculatorApplication;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
import dev.lampart.bartosz.brewingcalculator.entities.BCalcConf;
import dev.lampart.bartosz.brewingcalculator.entities.MainMenuItem;

/**
 * Created by bartek on 03.07.2016.
 */
public class MainMenu {
    public ArrayList<MainMenuItem> items;

    private static MainMenu mInstance = null;

    protected MainMenu() {
        items = new ArrayList<MainMenuItem>();
        MainMenuItem calcExtract = new MainMenuItem(1,
                BrewingCalculatorApplication.getContext().getString(R.string.title_extract_calculator),
                BrewingCalculatorApplication.getContext().getString(R.string.description_extract_calculator),
                R.drawable.ic_extract_fragment, DictFragment.FRAGMENT_OG_PLATO);
        MainMenuItem calcAlcohol = new MainMenuItem(2,
                BrewingCalculatorApplication.getContext().getString(R.string.title_alcohol_calculator),
                BrewingCalculatorApplication.getContext().getString(R.string.description_alcohol_calculator),
                R.drawable.ic_alcohol_fragment, DictFragment.FRAGMENT_ALCOHOL);

        items.add(calcExtract);
        items.add(calcAlcohol);
    }

    public static synchronized MainMenu getInstance() {
        if (null == mInstance) {
            mInstance = new MainMenu();
        }
        return mInstance;
    }
}