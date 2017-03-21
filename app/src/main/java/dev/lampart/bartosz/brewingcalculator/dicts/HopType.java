package dev.lampart.bartosz.brewingcalculator.dicts;

import android.app.Application;
import android.content.Context;

import dev.lampart.bartosz.brewingcalculator.BrewingCalculatorApplication;
import dev.lampart.bartosz.brewingcalculator.R;

/**
 * Created by bartek on 02.10.2016.
 */
public enum HopType {
    WHOLE_HOPS(R.string.hop_type_whole_hops), PELLETS(R.string.hop_type_pellet);

    private int mResourceId;

    private HopType(int id) {
        this.mResourceId = id;
    }

    @Override
    public String toString() {
        return BrewingCalculatorApplication.getContext().getString(mResourceId);
    }
}
