package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * Created by bartek on 12.07.2016.
 */
public class CarbonationCalc extends Calc {

    public static ArrayList<Tuple<SugarType, Double>> calcSugarAmount(double primingSize, double co2, double beerTemp,
                                                                      VolumeUnit primingUnit, TemperatureUnit tempUnit) {

        if (tempUnit == TemperatureUnit.C) {
            beerTemp = UnitCalc.calcCelsiusToFahrenheit(beerTemp);
        }
        if (primingUnit == VolumeUnit.Liter) {
            primingSize = UnitCalc.calcLitresToGallons(primingSize);
        }

        double dissolvedCO2 = 3.0378 - (0.050062 * beerTemp) + (0.00026555 * Math.pow(beerTemp, 2));
        double standardAmount = (co2 - dissolvedCO2) / 0.0131686;    // dextrose, 5 gallons
        double result = standardAmount * primingSize / 5;   // standard is for 5 gallons
        //Log.d("Carbonation", "Result: " + result);
        // corn sugar 1.0985
        // lde 1.4705

        ArrayList<Tuple<SugarType, Double>> resultArray = new ArrayList<Tuple<SugarType, Double>>();

        resultArray.add(new Tuple(SugarType.TableSugar, result));
        resultArray.add(new Tuple(SugarType.CornSugar, 1.0985 * result));
        resultArray.add(new Tuple(SugarType.DME, 1.4705 * result));

        return resultArray;
    }
}
