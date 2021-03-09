package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

import javax.inject.Inject;

import dev.lampart.bartosz.brewingcalculator.dicts.AlcFormula;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.Triple;

/**
 * Created by bartek on 01.07.2016.
 */
public class AlcoholCalc extends Calc {

    private final ExtractCalc extractCalcService;

    @Inject
    public AlcoholCalc(ExtractCalc extractCalc) {
        this.extractCalcService = extractCalc;
    }

    /**
     * Calculates Alcohol by volume and Attenuation
     * @param extBefore
     * @param extAfter
     * @param extBeforeUnit
     * @param extAfterUnit
     * @param useRefractometer
     * @return
     */
    public Triple<Double, Double, Double> CalculateAlcohol(double extBefore, double extAfter,
                                                                  ExtractUnit extBeforeUnit, ExtractUnit extAfterUnit,
                                                                  boolean useRefractometer, double wortFactor, AlcFormula formula) {
        double alc = 0;
        double att;

        if (useRefractometer) {
            switch (extBeforeUnit) {
                case SG:
                    extBefore = extractCalcService.calcSGToBrix(extBefore);
                    break;
                case Plato:
                    extBefore = extractCalcService.calcPlatoToBrix(extBefore);
                    break;
            }
            switch (extAfterUnit) {
                case SG:
                    extAfter = extractCalcService.calcSGToBrix(extAfter);
                    break;
                case Plato:
                    extAfter = extractCalcService.calcPlatoToBrix(extAfter);
                    break;
            }

            return calcAlcoholWithRefractometer(extBefore, extAfter, wortFactor);
        }
        else {
            switch (extBeforeUnit) {
                case Brix:
                    extBefore = extractCalcService.calcBrixToSG(extBefore);
                    break;
                case Plato:
                    extBefore = extractCalcService.calcPlatoToSG(extBefore);
                    break;
            }

            switch (extAfterUnit) {
                case Brix:
                    extAfter = extractCalcService.calcBrixToSG(extAfter);
                    break;
                case Plato:
                    extAfter = extractCalcService.calcPlatoToSG(extAfter);
                    break;
            }

            switch (formula) {
                case Standard: alc = (extBefore - extAfter) * 131.25; break;
                case Alternative: alc = (76.08 * (extBefore - extAfter) / (1.775 - extBefore)) * (extAfter / 0.794); break;
            }

            att = ((extBefore - 1) - (extAfter - 1)) / (extBefore - 1) * 100;
        }

        return new Triple<>(alc, att, extAfter);
    }

    private Triple<Double, Double, Double> calcAlcoholWithRefractometer(double brixOriginal, double brixFinal, double wortCorrectionFactor) {
        Log.d("CALC ALC", "==========");
        Log.d("CALC ALC", "Brix original: " + brixOriginal);
        Log.d("CALC ALC", "Brix final: " + brixFinal);
        double og = (brixOriginal/wortCorrectionFactor)/(258.6 - (((brixOriginal/wortCorrectionFactor)/258.2)*227.1))+1;

        double fg = 1.001843 - (0.002318474 * brixOriginal)-
                (0.000007775 * Math.pow(brixOriginal, 2)) -
                (0.000000034 * Math.pow(brixOriginal, 3)) +
                (0.00574 * brixFinal) +
                (0.00003344 * Math.pow(brixFinal, 2)) +
                (0.000000086 * Math.pow(brixFinal, 3));

        Log.d("CALC ALC", "OG: " + og);
        Log.d("CALC ALC", "FG: " + fg);

        return CalculateAlcohol(og, fg, ExtractUnit.SG, ExtractUnit.SG, false, 1, AlcFormula.Standard);
    }
}
