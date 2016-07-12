package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 12.07.2016.
 */
public class CarbonationCalculator extends Calc {

    public static double calcSugarAmount(double primingSize, double co2, double beerTemp, SugarType sugarType,
                                         VolumeUnit primingUnit, TemperatureUnit tempUnit) {


        if (tempUnit == TemperatureUnit.C) {
            beerTemp = UnitCalc.calcCelsiusToFahrenheit(beerTemp);
        }
        if (primingUnit == VolumeUnit.Liter) {
            primingSize = UnitCalc.calcLitresToGallons(primingSize);
        }

        double dissolvedCO2 = 3.0378 - (0.050062 * beerTemp) + (0.00026555 * Math.pow(beerTemp, 2));
        //Log.d("Carbonation", "Dissolved CO2 in beer: " + dissolvedCO2);
        double standardAmount = (co2 - dissolvedCO2) / 0.0131686;    // dextrose, 5 gallons
        //Log.d("Carbonation", "Standard amount: " + standardAmount);
        double result = standardAmount * primingSize / 5;   // standard is for 5 gallons
        //Log.d("Carbonation", "Result: " + result);

        return result;
    }
}
