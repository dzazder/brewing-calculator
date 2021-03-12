package dev.lampart.bartosz.brewingcalculator.calculators;

import javax.inject.Inject;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.ExtractRaws;

public class GravityCorrectionCalc extends Calc {

    private final ExtractCalc extractCalcService;
    private final UnitCalc unitCalcService;

    private final int _liquidExtractCorrectionFactor = 35;
    private final int _dryExtractCorrectionFactor = 44;
    private final int _cornSugarCorrectionFactor = 37;
    private final int _tableSugarCorrectionFactor = 46;

    @Inject
    public GravityCorrectionCalc(ExtractCalc extractCalc, UnitCalc unitCalcService) {
        this.extractCalcService = extractCalc;
        this.unitCalcService = unitCalcService;
    }

    /**
     *
     * @param batchSize
     * @param batchUnit
     * @param gravity
     * @param gravityUnit
     * @param expextedGravity
     * @param expectedGravityUnit
     * @return result in lbs
     */
    public ExtractRaws calculateExtract(double batchSize, VolumeUnit batchUnit,
                                        double gravity, ExtractUnit gravityUnit,
                                        double expextedGravity, ExtractUnit expectedGravityUnit) {

        if (batchUnit == VolumeUnit.Liter) {
            batchSize = unitCalcService.calcLitresToGallons(batchSize);
        }
        if (gravityUnit == ExtractUnit.Brix) {
            gravity = extractCalcService.calcBrixToSG(gravity);
        }
        if (gravityUnit == ExtractUnit.Plato) {
            gravity = extractCalcService.calcPlatoToSG(gravity);
        }
        if (expectedGravityUnit == ExtractUnit.Brix) {
            expextedGravity = extractCalcService.calcBrixToSG(expextedGravity);
        }
        if (expectedGravityUnit == ExtractUnit.Plato) {
            expextedGravity = extractCalcService.calcPlatoToSG(expextedGravity);
        }

        double expectedRaw = 1000 * batchSize * (expextedGravity - gravity);

        double expectedLiquidMalt = expectedRaw / _liquidExtractCorrectionFactor;
        double expectedDryExtract = expectedRaw / _dryExtractCorrectionFactor;
        double expectedCornSugar = expectedRaw / _cornSugarCorrectionFactor;
        double expectedTableSugar = expectedRaw / _tableSugarCorrectionFactor;

        return new ExtractRaws(expectedLiquidMalt, expectedDryExtract, expectedCornSugar, expectedTableSugar);
    }

    public double calcAdditionalWater(double batchSize, VolumeUnit batchUnit,
                                      double gravity, ExtractUnit gravityUnit,
                                      double expextedGravity, ExtractUnit expectedGravityUnit) {
        if (batchUnit == VolumeUnit.Gallon) {
            batchSize = unitCalcService.calcGallonsToLitres(batchSize);
        }
        if (gravityUnit == ExtractUnit.Brix) {
            gravity = extractCalcService.calcBrixToPlato(gravity);
        }
        if (gravityUnit == ExtractUnit.SG) {
            gravity = extractCalcService.calcSGToPlato(gravity);
        }
        if (expectedGravityUnit == ExtractUnit.Brix) {
            expextedGravity = extractCalcService.calcBrixToPlato(expextedGravity);
        }
        if (expectedGravityUnit == ExtractUnit.SG) {
            expextedGravity = extractCalcService.calcSGToPlato(expextedGravity);
        }

        double waterLiters = (batchSize * gravity) / expextedGravity;

        return waterLiters - batchSize;
    }

    public double calcExtractAfterEvaporation(double currentSize, VolumeUnit currentBatchUnit,
                                              double expectedSize, VolumeUnit expectedBatchUnit,
                                              double currentGravity, ExtractUnit currentGravityUnit) {
        if (currentBatchUnit == VolumeUnit.Gallon) {
            currentSize = unitCalcService.calcGallonsToLitres(currentSize);
        }

        if (expectedBatchUnit == VolumeUnit.Gallon) {
            expectedSize = unitCalcService.calcGallonsToLitres(expectedSize);
        }

        if (currentGravityUnit == ExtractUnit.Brix) {
            currentGravity = extractCalcService.calcBrixToPlato(currentGravity);
        }

        if (currentGravityUnit == ExtractUnit.SG) {
            currentGravity = extractCalcService.calcSGToPlato(currentGravity);
        }

        double expectedGravity = (currentSize * currentGravity) / expectedSize;

        if (currentGravityUnit == ExtractUnit.Brix) {
            expectedGravity = extractCalcService.calcPlatoToBrix(expectedGravity);
        }

        if (currentGravityUnit == ExtractUnit.SG) {
            expectedGravity = extractCalcService.calcPlatoToSG(expectedGravity);
        }

        return expectedGravity;
    }
}
