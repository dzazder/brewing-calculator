package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * Created by bartek on 01.07.2016.
 */
public class AlcoholCalc extends Calc {

    /**
     * Calculates Alcohol by volume and Attenuation
     * @param extBefore
     * @param extAfter
     * @param extBeforeUnit
     * @param extAfterUnit
     * @param useRefractometer
     * @return
     */
    public static Tuple<Double, Double> CalculateAlcohol(double extBefore, double extAfter,
                                                  ExtractUnit extBeforeUnit, ExtractUnit extAfterUnit,
                                                  boolean useRefractometer, double wortFactor) {
        double alc = 0;
        double att = 0;

        if (useRefractometer) {
            switch (extBeforeUnit) {
                case SG:
                    extBefore = ExtractCalc.calcSGToBrix(extBefore);
                    break;
                case Plato:
                    extBefore = ExtractCalc.calcPlatoToBrix(extBefore);
                    break;
            }
            switch (extAfterUnit) {
                case SG:
                    extAfter = ExtractCalc.calcSGToBrix(extAfter);
                    break;
                case Plato:
                    extAfter = ExtractCalc.calcPlatoToBrix(extAfter);
                    break;
            }

            return calcAlcoholWithRefractometer(extBefore, extAfter, wortFactor);
        }
        else {
            switch (extBeforeUnit) {
                case Brix:
                    extBefore = ExtractCalc.calcBrixToSG(extBefore);
                    break;
                case Plato:
                    extBefore = ExtractCalc.calcPlatoToSG(extBefore);
                    break;
            }

            switch (extAfterUnit) {
                case Brix:
                    extAfter = ExtractCalc.calcBrixToSG(extAfter);
                    break;
                case Plato:
                    extAfter = ExtractCalc.calcPlatoToSG(extAfter);
                    break;
            }

            alc = (76.08 * (extBefore - extAfter) / (1.775 - extBefore)) * (extAfter / 0.794);
            att = ((extBefore - 1) - (extAfter - 1)) / (extBefore - 1) * 100;
        }

        return new Tuple<>(alc, att);
    }

    private static Tuple<Double, Double> calcAlcoholWithRefractometer(double brixOriginal, double brixFinal, double wortCorrectionFactor) {
        Log.d("CALC ALC", "==========");
        Log.d("CALC ALC", "Brix original: " + brixOriginal);
        Log.d("CALC ALC", "Brix final: " + brixFinal);
        double og = (brixOriginal/wortCorrectionFactor)/(258.6 - (((brixOriginal/wortCorrectionFactor)/258.2)*227.1))+1;
        double fg = 1 - 0.0044993 * (brixOriginal / wortCorrectionFactor) + 0.0117741 * (brixFinal / wortCorrectionFactor)
                + 0.000275806 * Math.pow(brixOriginal / wortCorrectionFactor, 2)
                - 0.00127169 * Math.pow(brixFinal / wortCorrectionFactor, 2)
                - 0.00000727999 * Math.pow(brixOriginal / wortCorrectionFactor, 3)
                + 0.0000632929 * Math.pow(brixFinal / wortCorrectionFactor, 3);

        Log.d("CALC ALC", "OG: " + og);
        Log.d("CALC ALC", "FG: " + fg);

        double alc = (76.08 * (og - fg) / (1.775 - og)) * (fg / 0.794);
        double att = ((og - 1) - (fg - 1)) / (og - 1) * 100;

        Log.d("CALC ALC", "ALC: " + alc);
        Log.d("CALC ALC", "ATT: " + att);

        return new Tuple<>(alc, att);
    }
}
