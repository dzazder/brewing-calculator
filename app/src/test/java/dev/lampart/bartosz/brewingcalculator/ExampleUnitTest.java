package dev.lampart.bartosz.brewingcalculator;

import org.junit.Test;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.calculators.CarbonationCalculator;
import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

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
        ArrayList<Tuple<SugarType, Double>> result = CarbonationCalculator.calcSugarAmount(5, 2.5, 68, SugarType.TableSugar,
                VolumeUnit.Gallon, TemperatureUnit.F);

        assertEquals(124.5, result.get(0).y , 0.1);
    }
}