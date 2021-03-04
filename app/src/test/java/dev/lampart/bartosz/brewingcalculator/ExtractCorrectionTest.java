package dev.lampart.bartosz.brewingcalculator;

import org.junit.Test;

import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCorrectionCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.ExtractRaws;

import static junit.framework.Assert.assertEquals;

public class ExtractCorrectionTest {
    @Test
    public void extractCorrectionTest() throws Exception {
        ExtractCorrectionCalc calc = new ExtractCorrectionCalc();
        ExtractRaws result = calc.calculateExtract(5, VolumeUnit.Gallon, 1.04, ExtractUnit.SG, 1.05, ExtractUnit.SG);

        assertEquals(1.43, result.getLiquidMaltExtract(), 1);
        assertEquals(1.14, result.getDryMaltExtract(), 1);
        assertEquals(1.35, result.getCornSugar(), 1);
    }

    @Test
    public void extractCorrectionTest2() throws Exception {
        ExtractCorrectionCalc calc = new ExtractCorrectionCalc();
        ExtractRaws result = calc.calculateExtract(20, VolumeUnit.Liter, 1.04, ExtractUnit.SG, 1.07, ExtractUnit.SG);

        assertEquals(4.53, result.getLiquidMaltExtract(), 1);
        assertEquals(3.6, result.getDryMaltExtract(), 1);
        assertEquals(4.28, result.getCornSugar(), 1);
    }
}
