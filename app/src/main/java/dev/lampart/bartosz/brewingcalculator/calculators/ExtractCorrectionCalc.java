package dev.lampart.bartosz.brewingcalculator.calculators;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.ExtractRaws;

public class ExtractCorrectionCalc extends Calc {

    private int _liquidExtractCorrectionFactor = 35;
    private int _dryExtractCorrectionFactor = 44;
    private int _cornSugarCorrectionFactor = 37;

    public ExtractRaws calculateExtract(double batchSize, VolumeUnit batchUnit,
                                        double gravity, ExtractUnit gravityUnit,
                                        double expextedGravity, ExtractUnit expectedGravityUnit) {

        if (batchUnit == VolumeUnit.Liter) {
            batchSize = UnitCalc.calcLitresToGallons(batchSize);
        }
        if (gravityUnit == ExtractUnit.Brix) {
            gravity = ExtractCalc.calcBrixToSG(gravity);
        }
        if (gravityUnit == ExtractUnit.Plato) {
            gravity = ExtractCalc.calcPlatoToSG(gravity);
        }
        if (expectedGravityUnit == ExtractUnit.Brix) {
            expextedGravity = ExtractCalc.calcBrixToSG(expextedGravity);
        }
        if (expectedGravityUnit == ExtractUnit.Plato) {
            expextedGravity = ExtractCalc.calcPlatoToSG(expextedGravity);
        }

        double expectedRaw = 1000 * batchSize * (expextedGravity - gravity);

        double expectedLiquidMalt = expectedRaw / _liquidExtractCorrectionFactor;
        double expectedDryExtract = expectedRaw / _dryExtractCorrectionFactor;
        double expectedCornSugar = expectedRaw / _cornSugarCorrectionFactor;

        return new ExtractRaws(expectedLiquidMalt, expectedDryExtract, expectedCornSugar);
    }
}
