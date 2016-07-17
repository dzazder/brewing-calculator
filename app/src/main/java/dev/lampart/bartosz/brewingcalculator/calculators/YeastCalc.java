package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.BeerStyle;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 17.07.2016.
 */
public class YeastCalc {
    /**
     * Calculates yeast needed to beer fermentation
     * @param beerAmount
     * @param gravity
     * @param volUnit
     * @param gravityUnit
     * @return
     */
    public static long calcYeastCells(double beerAmount, double gravity, VolumeUnit volUnit,
                                      ExtractUnit gravityUnit, BeerStyle beerStyle) {
        if (volUnit == VolumeUnit.Gallon) {
            beerAmount = UnitCalc.calcGallonsToLitres(beerAmount);
        }
        switch (gravityUnit) {
            case SG: gravity = ExtractCalc.calcSGToPlato(gravity); break;
            case Brix: gravity = ExtractCalc.calcBrixToPlato(gravity); break;
        }
        beerAmount = beerAmount * 1000; // beer in ml
        return (long) (1000000 * beerAmount * gravity * getBeerStyleYeastRate(beerStyle));
    }

    private static double getBeerStyleYeastRate(BeerStyle beerStyle) {
        switch (beerStyle) {
            case Ale: return 0.75;
            case Lager: return 1.5;
        }

        return 1;
    }
}
