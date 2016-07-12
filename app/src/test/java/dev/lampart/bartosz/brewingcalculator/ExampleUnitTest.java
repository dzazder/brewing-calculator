package dev.lampart.bartosz.brewingcalculator;

import org.junit.Test;

import dev.lampart.bartosz.brewingcalculator.calculators.CarbonationCalculator;
import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void carbonation_isCorrect() throws Exception {
        assertEquals(124.5, CarbonationCalculator.calcSugarAmount(5, 2.5, 68, SugarType.TableSugar,
                VolumeUnit.Gallon, TemperatureUnit.F), 0.1);
    }
}