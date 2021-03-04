package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;

/**
 * Created by bartek on 25.07.2016.
 */
public class HydrometerCalc extends Calc {

    // in celsius
    private static double calibratedTemp = 20;

    // Bw = B + (Tp - Ta)*0,05

    public static double calcAdjustmentGravity(double measuredGravity, double temp,
                                               ExtractUnit gravityUnit, TemperatureUnit tempUnit,
                                               ExtractUnit resultUnit) {

        switch (gravityUnit) {
            case Brix: measuredGravity = ExtractCalc.calcBrixToSG(measuredGravity); break;
            case Plato: measuredGravity = ExtractCalc.calcPlatoToSG(measuredGravity); break;
        }

        if (tempUnit == TemperatureUnit.C) {
            temp = UnitCalc.calcCelsiusToFahrenheit(temp);
        }
        //SG(true) = SG(indic) x [ 1.0 - 0.000025[ T(act) - T(calib) ]]
        double resultGravity = measuredGravity * ( 1 + (0.000025 * (temp - UnitCalc.calcCelsiusToFahrenheit(calibratedTemp))));  // in SG

        switch (resultUnit) {
            case Brix: resultGravity = ExtractCalc.calcSGToBrix(resultGravity); break;
            case Plato: resultGravity = ExtractCalc.calcSGToPlato(resultGravity); break;
        }

        return resultGravity;
    }
}
