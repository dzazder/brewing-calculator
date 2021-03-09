package dev.lampart.bartosz.brewingcalculator.calculators;

import javax.inject.Inject;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 29.04.2017.
 */
public class WaterCorrectionCalc extends Calc {

    private final ExtractCalc extractCalcService;
    private final UnitCalc unitCalcService;

    @Inject
    public WaterCorrectionCalc(ExtractCalc extractCalcService, UnitCalc unitCalcService) {
        this.extractCalcService = extractCalcService;
        this.unitCalcService = unitCalcService;
    }

    /**
     * Returns value as liters
     * @param batchSize
     * @param gravity
     * @param gravityUnit
     * @param expextedGravity
     * @param expectedGravityUnit
     * @return
     */
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
}
