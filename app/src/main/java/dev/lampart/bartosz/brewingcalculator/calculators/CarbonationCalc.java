package dev.lampart.bartosz.brewingcalculator.calculators;

import java.util.ArrayList;
import java.util.List;

import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * Created by bartek on 12.07.2016.
 */
public class CarbonationCalc extends Calc {

    public static List<Tuple<SugarType, Double>> calcSugarAmount(double primingSize, double co2, double beerTemp,
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

        List<Tuple<SugarType, Double>> resultArray = new ArrayList<>();

        Tuple<SugarType, Double> tableSugarResult = new Tuple<>(SugarType.TableSugar, result);
        Tuple<SugarType, Double> cornSugarResult = new Tuple<>(SugarType.TableSugar, result);
        Tuple<SugarType, Double> cmeResult = new Tuple<>(SugarType.TableSugar, result);

        resultArray.add(tableSugarResult);
        resultArray.add(cornSugarResult);
        resultArray.add(cmeResult);

        return resultArray;
    }
}
