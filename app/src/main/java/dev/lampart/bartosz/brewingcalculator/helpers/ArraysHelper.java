package dev.lampart.bartosz.brewingcalculator.helpers;

import android.content.Context;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;

/**
 * Created by bartek on 27.11.2016.
 */
public class ArraysHelper {
    public static WeightUnit getWeightUnit(String value, Context context) {
        if (value.equals(context.getResources().getString(R.string.weight_unit_oz))) {
            return WeightUnit.OZ;
        }
        if (value.equals(context.getResources().getString(R.string.weight_unit_g))) {
            return WeightUnit.G;
        }

        return AppConfiguration.getInstance().defaultSettings.getDefWeightUnit();
    }

    public static HopType getHopType(String value, Context context) {
        if (value.equals(context.getResources().getString(R.string.hop_type_pellet))) {
            return HopType.PELLETS;
        }
        if (value.equals(context.getResources().getString(R.string.hop_type_whole_hops))) {
            return HopType.WHOLE_HOPS;
        }

        return HopType.PELLETS;
    }
}
