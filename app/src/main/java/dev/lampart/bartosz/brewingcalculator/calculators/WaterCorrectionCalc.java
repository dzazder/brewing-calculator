package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 29.04.2017.
 */
public class WaterCorrectionCalc extends Calc {

    /**
     * Returns value as liters
     * @param batchSize
     * @param gravity
     * @param gravityUnit
     * @param expextedGravity
     * @param expectedGravityUnit
     * @return
     */
    public static double calcAdditionalWater(double batchSize, VolumeUnit batchUnit,
                                        double gravity, ExtractUnit gravityUnit,
                                        double expextedGravity, ExtractUnit expectedGravityUnit) {
        if (batchUnit == VolumeUnit.Gallon) {
            batchSize = UnitCalc.calcGallonsToLitres(batchSize);
        }
        if (gravityUnit == ExtractUnit.Brix) {
            gravity = ExtractCalc.calcBrixToPlato(gravity);
        }
        if (gravityUnit == ExtractUnit.SG) {
            gravity = ExtractCalc.calcSGToPlato(gravity);
        }
        if (expectedGravityUnit == ExtractUnit.Brix) {
            expextedGravity = ExtractCalc.calcBrixToPlato(expextedGravity);
        }
        if (expectedGravityUnit == ExtractUnit.SG) {
            expextedGravity = ExtractCalc.calcSGToPlato(expextedGravity);
        }

        double waterLiters = (batchSize * gravity) / expextedGravity;

        return waterLiters - batchSize;
    }
}
