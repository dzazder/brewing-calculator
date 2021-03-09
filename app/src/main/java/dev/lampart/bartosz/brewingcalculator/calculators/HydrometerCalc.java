package dev.lampart.bartosz.brewingcalculator.calculators;

import javax.inject.Inject;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;

/**
 * Created by bartek on 25.07.2016.
 */
public class HydrometerCalc extends Calc {

    private final ExtractCalc extractCalcService;
    private final UnitCalc unitCalcService;

    // in celsius
    private final double calibratedTemp = 20;

    @Inject
    public HydrometerCalc(ExtractCalc extractCalc, UnitCalc unitCalcService) {
        this.extractCalcService = extractCalc;
        this.unitCalcService = unitCalcService;
    }

    // Bw = B + (Tp - Ta)*0,05

    public double calcAdjustmentGravity(double measuredGravity, double temp,
                                               ExtractUnit gravityUnit, TemperatureUnit tempUnit,
                                               ExtractUnit resultUnit) {

        switch (gravityUnit) {
            case Brix: measuredGravity = extractCalcService.calcBrixToSG(measuredGravity); break;
            case Plato: measuredGravity = extractCalcService.calcPlatoToSG(measuredGravity); break;
        }

        if (tempUnit == TemperatureUnit.C) {
            temp = unitCalcService.calcCelsiusToFahrenheit(temp);
        }
        //SG(true) = SG(indic) x [ 1.0 - 0.000025[ T(act) - T(calib) ]]
        double resultGravity = measuredGravity * ( 1 + (0.000025 * (temp - unitCalcService.calcCelsiusToFahrenheit(calibratedTemp))));  // in SG

        switch (resultUnit) {
            case Brix: resultGravity = extractCalcService.calcSGToBrix(resultGravity); break;
            case Plato: resultGravity = extractCalcService.calcSGToPlato(resultGravity); break;
        }

        return resultGravity;
    }
}
