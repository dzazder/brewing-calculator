package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.Triple;

/**
 * Created by bartek on 23.11.2017.
 */

public class MaltExtractCalc extends Calc {
    public static Triple<Double, Double, Double> calcAddition(double volume, double measuredGravity, double targetGravity,
                                                              VolumeUnit volumeUnit, ExtractUnit measuredGravityUnit, ExtractUnit targetGravityUnit) {
        return new Triple<Double, Double, Double>(1., 1., 1.);  // // TODO: 23.11.2017  
    }
}
