package dev.lampart.bartosz.brewingcalculator.calculators;

/**
 * Created by bartek on 11.07.2016.
 */
public class UnitCalc extends Calc {
    public static double calcLitresToGallons(double litres) {
        return litres / 3.785411784;
    }

    public static double calcGallonsToLitres(double gallons) {
        return gallons * 3.785411784;
    }

    public static double calcCelsiusToFahrenheit(double c) {
        return (c * ((double)9 / 5)) + 32;
    }

    public static double calcFahrenheitToCelsius(double f) {
        return (f - 32) * (5 / 9);
    }

    public static double calcGramsToOunces(double g) {
        return g / 28.3495231;
    }

    public static double calcOuncesToGrams(double o) {
        return o * 28.3495231;
    }
}
