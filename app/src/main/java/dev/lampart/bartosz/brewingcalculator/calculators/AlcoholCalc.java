package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * Created by bartek on 01.07.2016.
 */
public class AlcoholCalc {

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
                                                  boolean useRefractometer) {
        double alc = 0;
        double att = 0;

        if (useRefractometer) {
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

            alc = 100 * ((extBefore - extAfter) / 0.75);
            att = ((extBefore - 1) - (extAfter - 1)) / (extBefore - 1) * 100;
        }
        else {
            switch (extBeforeUnit) {
                case Brix:
                    extBefore = ExtractCalc.calcBrixToPlato(extBefore);
                    break;
                case Plato:
                    extBefore = ExtractCalc.calcPlatoToSG(extBefore);
                    break;
            }

            switch (extAfterUnit) {
                case Brix:
                    extAfter = ExtractCalc.calcBrixToPlato(extAfter);
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
}
