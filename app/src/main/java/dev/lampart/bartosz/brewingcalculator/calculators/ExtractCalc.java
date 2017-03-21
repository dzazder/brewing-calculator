package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

/**
 * Created by bartek on 27.04.2016.
 */
public class ExtractCalc extends Calc {
    public static double calcBrixToPlato(double brix) {
        double plato = brix / 1.04;
        return plato;
    }

    public static double calcBrixToSG(double brix) {
        double plato = brix / 1.04;
        double sg = calcPlatoToSG(plato);
        //Log.d("CALC", "Brix: " + brix + " , SG: " + sg);
        return sg;
    }

    public static double calcSGToBrix(double sg) {
        double brix = 1.04 *  ((-1 * 616.868) + (1111.14 * sg) - (630.272 * Math.pow(sg, 2)) + (135.997 * Math.pow(sg, 3)));
        //Log.d("CALC", "SG: " + sg + " , Brix: " + brix);
        return brix;
    }

    /**
     * Formula from this site:
     * http://www.brewersfriend.com/plato-to-sg-conversion-chart/
     * @param sg
     * @return
     */
    public static double calcSGToPlato(double sg) {
        double plato = (-1 * 616.868) + (1111.14 * sg) - (630.272 * Math.pow(sg, 2)) + (135.997 * Math.pow(sg, 3));
        //Log.d("CALC", "SG: " + sg + " , Plato: " + plato);
        return plato;
    }

    /**
     * Formula from this site:
     * http://www.brewersfriend.com/plato-to-sg-conversion-chart/
     * @param plato
     * @return
     */
    public static double calcPlatoToSG(double plato) {
        double sg = 1 + (plato / (258.6 - ((plato / 258.2) * 227.1)));
        //Log.d("CALC", "Plato: " + plato + " , SG: " + sg);
        return sg;
    }

    public static double calcPlatoToBrix(double plato) {
        double brix = 1.04 * plato;
        return brix;
    }



}
