package dev.lampart.bartosz.brewingcalculator;

import org.junit.Test;

import dev.lampart.bartosz.brewingcalculator.calculators.IBUCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bartek on 02.10.2016.
 */
public class IBUTest {

    @Test
    public void ibuWholeHops() throws Exception {
        // http://www.hopsteiner.com/ibu-calculator/
        IBUData ibuData5 = new IBUData(5, 1, 10, HopType.WHOLE_HOPS);
        IBUData ibuData7 = new IBUData(7, 2, 20, HopType.WHOLE_HOPS);
        IBUData ibuData10 = new IBUData(10, 2, 30, HopType.WHOLE_HOPS);
        IBUData ibuData15 = new IBUData(15, 1, 60, HopType.WHOLE_HOPS);

        double sg14 = 1.04;


        assertEquals(5, IBUCalc.calcIBU(ibuData5, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);
        assertEquals(22, IBUCalc.calcIBU(ibuData7, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);
        assertEquals(69.2, IBUCalc.calcIBU(ibuData15, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);
        assertEquals(51.3, IBUCalc.calcIBU(ibuData10, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);

        assertEquals(6.9, IBUCalc.calcIBU(ibuData5, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);
        assertEquals(32, IBUCalc.calcIBU(ibuData7, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);
        assertEquals(56.7, IBUCalc.calcIBU(ibuData15, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);
        assertEquals(58.1, IBUCalc.calcIBU(ibuData10, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);

    }

    @Test
    public void ibuPellets() throws Exception {
        // http://www.hopsteiner.com/ibu-calculator/
        IBUData ibuData5 = new IBUData(5, 1, 10, HopType.PELLETS);
        IBUData ibuData7 = new IBUData(7, 2, 20, HopType.PELLETS);
        IBUData ibuData10 = new IBUData(10, 2, 30, HopType.PELLETS);
        IBUData ibuData15 = new IBUData(15, 1, 60, HopType.PELLETS  );

        double sg14 = 1.04;


        assertEquals(5.8, IBUCalc.calcIBU(ibuData5, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);
        assertEquals(25.3, IBUCalc.calcIBU(ibuData7, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);
        assertEquals(79.6, IBUCalc.calcIBU(ibuData15, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);
        assertEquals(58.9, IBUCalc.calcIBU(ibuData10, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.RAGER),1);

        assertEquals(7.9, IBUCalc.calcIBU(ibuData5, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);
        assertEquals(36.9, IBUCalc.calcIBU(ibuData7, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);
        assertEquals(65.2, IBUCalc.calcIBU(ibuData15, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);
        assertEquals(66.8, IBUCalc.calcIBU(ibuData10, sg14, 5, ExtractUnit.SG, VolumeUnit.Gallon, IBUCalc.FormulaTypeIBU.TINSETH),1);

    }
}
