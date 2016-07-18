package dev.lampart.bartosz.brewingcalculator.calculators;

import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dev.lampart.bartosz.brewingcalculator.dicts.BeerStyle;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 17.07.2016.
 */
public class YeastCalc {
    private static double dryGramCells = 20000000000.;
    private static double dayFlurryViabilityDecrease = 1.61857;
    private static double flurryMililiterCells = 1500000000.;
    private static double liquidPackCells = 100000000000.;
    private static double starterMililiterCells = 240641711.;

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

    public static double calcGramsOfDryYeast(long yeastCells) {
        return (double)yeastCells / dryGramCells;
    }

    public static double calcMililitersOfSlurry(long yeastCells, Date harvestDate) {
        double slurryViability = calcSlurryViability(harvestDate);
        double mililiterCells = flurryMililiterCells * slurryViability;

        return (double)yeastCells / mililiterCells;
    }

    public static double calcLiquidPackwWithoutStarter(long yeastCells, Date prodDate) {
        double yeastViability = calcLiquidViability(prodDate);

        return yeastCells / (yeastViability * liquidPackCells);

    }

    public static double calcMililitersOfStarter(long yeastCells, Date prodDate) {
        double yeastViability = calcLiquidViability(prodDate);
        double growthFactor = 3.0;

        return yeastCells / (yeastViability * starterMililiterCells);
    }

    public static double calcDryViability(Date productionDate) {
        return 1;
    }

    public static double calcLiquidViability(Date productionDate) {
        return 1;
    }

    public static double calcSlurryViability(Date harvestDate) {
        Date today = new Date();
        long diff = today.getTime() - harvestDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return (double)(94 - (daysDiff * dayFlurryViabilityDecrease)) / 100.;
    }

    private static double getBeerStyleYeastRate(BeerStyle beerStyle) {
        switch (beerStyle) {
            case Ale: return 0.75;
            case Lager: return 1.5;
        }

        return 1;
    }
}
