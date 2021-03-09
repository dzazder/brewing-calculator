package dev.lampart.bartosz.brewingcalculator.calculators;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dev.lampart.bartosz.brewingcalculator.dicts.BeerStyle;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 17.07.2016.
 */
public class YeastCalc {
    private static double dryGramCells = 20000000000.;
    private static double dayFlurryViabilityDecrease = 1.61857;
    private static double dayDryViabilityDecrease = 0.06;
    private static double dayLiquidViabilityDecrease = 0.43;
    private static double flurryMililiterCells = 2000000000.;
    private static double liquidPackCells = 100000000000.;
    private static double starterMililiterCells = 240641711.;

    private final ExtractCalc extractCalcService;
    private final UnitCalc unitCalcService;

    @Inject
    public YeastCalc(ExtractCalc extractCalcService, UnitCalc unitCalcService) {
        this.extractCalcService = extractCalcService;
        this.unitCalcService = unitCalcService;
    }

    /**
     * Calculates yeast needed to beer fermentation
     * @param beerAmount
     * @param gravity
     * @param volUnit
     * @param gravityUnit
     * @return
     */
    public long calcYeastCells(double beerAmount, double gravity, VolumeUnit volUnit,
                                      ExtractUnit gravityUnit, BeerStyle beerStyle) {
        if (volUnit == VolumeUnit.Gallon) {
            beerAmount = unitCalcService.calcGallonsToLitres(beerAmount);
        }
        switch (gravityUnit) {
            case SG: gravity = extractCalcService.calcSGToPlato(gravity); break;
            case Brix: gravity = extractCalcService.calcBrixToPlato(gravity); break;
        }
        beerAmount = beerAmount * 1000; // beer in ml
        return (long) (1000000 * beerAmount * gravity * getBeerStyleYeastRate(beerStyle));
    }

    public double calcGramsOfDryYeast(long yeastCells, Date productionDate) {
        double dryViability = calcDryViability(productionDate);
        double dryGram = dryGramCells * dryViability;

        return (double)yeastCells / dryGram;
    }

    public double calcMililitersOfSlurry(long yeastCells, Date harvestDate) {
        double slurryViability = calcSlurryViability(harvestDate);
        double mililiterCells = flurryMililiterCells * slurryViability;

        return (double)yeastCells / mililiterCells;
    }

    public double calcLiquidPackwWithoutStarter(long yeastCells, Date prodDate) {
        double yeastViability = calcLiquidViability(prodDate);

        return yeastCells / (yeastViability * liquidPackCells);

    }

    public double calcMililitersOfStarter(long yeastCells, Date prodDate) {
        double yeastViability = calcLiquidViability(prodDate);

        return yeastCells / (yeastViability * starterMililiterCells);
    }

    public double calcDryViability(Date productionDate) {
        Date today = new Date();
        long diff = today.getTime() - productionDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return (90 - ((double)daysDiff * dayDryViabilityDecrease)) / 100.;
    }

    public double calcLiquidViability(Date productionDate) {
        Date today = new Date();
        long diff = today.getTime() - productionDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return (96 - ((double)daysDiff * dayLiquidViabilityDecrease)) / 100.;
    }

    public double calcSlurryViability(Date harvestDate) {
        Date today = new Date();
        long diff = today.getTime() - harvestDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return (94 - ((double)daysDiff * dayFlurryViabilityDecrease)) / 100.;
    }

    private double getBeerStyleYeastRate(BeerStyle beerStyle) {
        switch (beerStyle) {
            case Ale: return 0.75;
            case Lager: return 1.5;
        }

        return 1;
    }
}
