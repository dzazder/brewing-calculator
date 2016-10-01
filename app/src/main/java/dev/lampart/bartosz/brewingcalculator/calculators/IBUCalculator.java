package dev.lampart.bartosz.brewingcalculator.calculators;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

/**
 * Created by bartek on 30.09.2016.
 */
public class IBUCalculator extends Calc {
    public static double calcIBU(IBUData ibuData) {
        return (ibuData.getAlpha()/100) * ibuData.getWeight() * (ibuData.getTime()/100);
    }

    public static double calcIBU(ArrayList<IBUData> ibuDatas) {
        double sum = 0;
        for (IBUData ibuData: ibuDatas) {
            sum += calcIBU(ibuData);
        }
        return sum;
    }
}
