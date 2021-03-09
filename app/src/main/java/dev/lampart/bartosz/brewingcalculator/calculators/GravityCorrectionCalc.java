package dev.lampart.bartosz.brewingcalculator.calculators;

import javax.inject.Inject;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.ExtractRaws;

public class GravityCorrectionCalc extends Calc {

    private final ExtractCalc extractCalcService;
    private final UnitCalc unitCalcService;

    private int _liquidExtractCorrectionFactor = 35;
    private int _dryExtractCorrectionFactor = 44;
    private int _cornSugarCorrectionFactor = 37;

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

        return new ExtractRaws(expectedLiquidMalt, expectedDryExtract, expectedCornSugar);
    }
}
