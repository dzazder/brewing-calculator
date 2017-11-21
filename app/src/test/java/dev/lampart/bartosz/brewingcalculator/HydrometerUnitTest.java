package dev.lampart.bartosz.brewingcalculator;

import org.junit.Test;

import dev.lampart.bartosz.brewingcalculator.calculators.HydrometerCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by bartek on 29.07.2016.
 */
public class HydrometerUnitTest  {
    /*
    @Test
    public void celsiusBrixCorrect() throws Exception {
        assertEquals(20.47,
                HydrometerCalc.calcAdjustmentGravity(20, 30, ExtractUnit.Brix, TemperatureUnit.C, ExtractUnit.Brix),
                0.01);
    }
    @Test
    public void celsiusBrixCorrect3() throws Exception {
        assertEquals(19.8,
                HydrometerCalc.calcAdjustmentGravity(20, 10, ExtractUnit.Brix, TemperatureUnit.C, ExtractUnit.Brix),
                0.01);
    }
    @Test
    public void celsiusBrixCorrect4() throws Exception {
        assertEquals(14.86,
                HydrometerCalc.calcAdjustmentGravity(15, 15, ExtractUnit.Brix, TemperatureUnit.C, ExtractUnit.Brix),
                0.01);
    }
    @Test
    public void celsiusBrixCorrect5() throws Exception {
        assertEquals(15.49,
                HydrometerCalc.calcAdjustmentGravity(15, 30, ExtractUnit.Brix, TemperatureUnit.C, ExtractUnit.Brix),
                0.01);
    }
    @Test
    public void celsiusBrixCorrect6() throws Exception {
        assertEquals(11.23,
                HydrometerCalc.calcAdjustmentGravity(10, 40, ExtractUnit.Brix, TemperatureUnit.C, ExtractUnit.Brix),
                0.01);
    }
    @Test
    public void celsiusBrixCorrect7() throws Exception {
        assertEquals(10.22,
                HydrometerCalc.calcAdjustmentGravity(10, 25, ExtractUnit.Brix, TemperatureUnit.C, ExtractUnit.Brix),
                0.01);
    }
    @Test
    public void fahrSGCorrect() throws Exception {
        assertEquals(1.041,
                HydrometerCalc.calcAdjustmentGravity(1.04, 80, ExtractUnit.SG, TemperatureUnit.F, ExtractUnit.SG),
                0.0001);
    }
    @Test
    public void fahrSGCorrect3() throws Exception {
        assertEquals(1.039,
                HydrometerCalc.calcAdjustmentGravity(1.04, 50, ExtractUnit.SG, TemperatureUnit.F, ExtractUnit.SG),
                0.0001);
    }
    @Test
    public void fahrSGCorrect4() throws Exception {
        assertEquals(14.86,
                HydrometerCalc.calcAdjustmentGravity(15, 15, ExtractUnit.SG, TemperatureUnit.F, ExtractUnit.SG),
                0.0001);
    }
    @Test
    public void fahrSGCorrect5() throws Exception {
        assertEquals(15.49,
                HydrometerCalc.calcAdjustmentGravity(15, 30, ExtractUnit.SG, TemperatureUnit.F, ExtractUnit.SG),
                0.01);
    }
    @Test
    public void fahrSGCorrect6() throws Exception {
        assertEquals(11.23,
                HydrometerCalc.calcAdjustmentGravity(10, 40, ExtractUnit.SG, TemperatureUnit.F, ExtractUnit.SG),
                0.01);
    }
    @Test
    public void fahrSGCorrect7() throws Exception {
        assertEquals(10.22,
                HydrometerCalc.calcAdjustmentGravity(10, 25, ExtractUnit.SG, TemperatureUnit.F, ExtractUnit.SG),
                0.01);
    }
    */
}
