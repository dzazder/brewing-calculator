package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

/**
 * Created by bartek on 27.04.2016.
 */
public class ExtractCalc {
    public static double calcBrixToPlato(double brix) {
        double plato = 1.04 * brix;
        return plato;
    }

    /*
    =1.000898+0.003859118*BRIX+0.00001370735*BRIX*BRIX+0.00000003742517*BRIX*BRIX*BRIX

if I remember correctly, this formula corrects for Plato:

=1.000019+(0.003865613*Plato+0.00001296425*Plato^2+0.00000005701128*Plato^3)

     */
    public static double calcBrixToSG(double brix) {
        double sg = 1 + (0.004 * brix);
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

    public static double calcPlatoToBrix(double plato) {
        double brix = 1.000019+(0.003865613*plato+0.00001296425*plato*plato+0.00000005701128*plato*plato*plato);
        return brix;
    }

}
