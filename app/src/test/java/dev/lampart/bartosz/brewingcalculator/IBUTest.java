package dev.lampart.bartosz.brewingcalculator;

import org.junit.Test;

import dev.lampart.bartosz.brewingcalculator.calculators.HydrometerCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.IBUCalculator;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bartek on 02.10.2016.
 */
public class IBUTest {

    @Test
    public void ibu1() throws Exception {
        assertEquals(53.1,
                IBUCalculator.calcIBU(new IBUData(10, 2, 30, HopType.PELLETS), 1.055, 5.5),
                1);

        assertEquals(46.2,
                IBUCalculator.calcIBU(new IBUData(10, 2, 30, HopType.WHOLE_HOPS), 1.055, 5.5),
                1);
    }
}
