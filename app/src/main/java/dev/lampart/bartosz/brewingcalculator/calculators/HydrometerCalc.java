package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;

/**
 * Created by bartek on 25.07.2016.
 */
public class HydrometerCalc {

    // in celsius
    private static double calibratedTemp = 20;

    // Bw = B + (Tp - Ta)*0,05
    /**
     * Returns result in plato
     * @param measuredGravity
     * @param temp
     * @param gravityUnit
     * @param tempUnit
     * @return
     */
    public static double calcAdjustmentGravity(double measuredGravity, double temp,
                                          ExtractUnit gravityUnit, TemperatureUnit tempUnit) {

        switch (gravityUnit) {
            case Brix: measuredGravity = ExtractCalc.calcBrixToPlato(measuredGravity); break;
            case SG: measuredGravity = ExtractCalc.calcSGToPlato(measuredGravity); break;
        }

        if (tempUnit == TemperatureUnit.F) {
            temp = UnitCalc.calcFahrenheitToCelsius(temp);
        }

        return measuredGravity + ((temp - calibratedTemp) * 0.05);
    }
}
