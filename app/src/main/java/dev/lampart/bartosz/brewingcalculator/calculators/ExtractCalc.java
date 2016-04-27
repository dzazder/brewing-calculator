package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

/**
 * Created by bartek on 27.04.2016.
 */
public class ExtractCalc {
    public static double calcBrixToPlato(double brix) {

        return 1;
    }

    public static double calcBrixToSG(double brix) {
        double sg = 1 + (0.0004 * brix);
        Log.d("CALC", "Brix: " + brix + " , SG: " + sg);
        return sg;
    }

    public static double calcSGToBrix(double sg) {
        return 1;
    }

    public static double calcSGToPlato(double sg) {
        double plato = 258.6 - (258.6/sg);
        Log.d("CALC", "SG: " + sg + " , Plato: " + plato);
        return plato;
    }

    public static double calcPlatoToSG(double plato) {
        double sg = ((182.4601 * plato - 775.6821) * plato + 1262.7794) * plato - 669.5622;
        Log.d("CALC", "Plato: " + plato + " , SG: " + sg);
        return sg;
    }


}
