package dev.lampart.bartosz.brewingcalculator.calculators;

import javax.inject.Inject;

/**
 * Created by bartek on 11.07.2016.
 */
public class UnitCalc extends Calc {

    @Inject
    public UnitCalc() {}

    public double calcLitresToGallons(double litres) {
        return litres / 3.785411784;
    }

    public double calcGallonsToLitres(double gallons) {
        return gallons * 3.785411784;
    }

    public double calcCelsiusToFahrenheit(double c) {
        return (c * ((double)9 / 5)) + 32;
    }

    public double calcFahrenheitToCelsius(double f) {
        return (f - 32) * (5 / 9);
    }

    public double calcGramsToOunces(double g) {
        return g / 28.3495231;
    }

    public double calcOuncesToGrams(double o) {
        return o * 28.3495231;
    }

    public double calcPoundsToKilograms(double p) { return p * 0.453592; }

    public double calcKilogramsToPounds(double k) { return k * 2.20462; }
}
