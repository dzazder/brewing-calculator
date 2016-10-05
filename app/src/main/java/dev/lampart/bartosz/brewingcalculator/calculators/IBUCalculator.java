package dev.lampart.bartosz.brewingcalculator.calculators;

import android.util.Log;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

/**
 * Created by bartek on 30.09.2016.
 */
public class IBUCalculator extends Calc {

    private static double PELLET_FACTOR = 1.1488;

    public static double calcIBU(IBUData ibuData, double sg, double volume, ExtractUnit extractUnit,
                                 VolumeUnit volumeUnit) {
        switch (extractUnit) {
            case Brix:
                sg = ExtractCalc.calcBrixToSG(sg);
                break;
            case Plato:
                sg = ExtractCalc.calcPlatoToSG(sg);
                break;
        }
        if (volumeUnit == VolumeUnit.Liter) {
            volume = UnitCalc.calcLitresToGallons(volume);
        }

        double boilTimeFactor = (1 - Math.exp(-0.04 * ibuData.getTime())) / 4.15;
        double bignessFactor = 1.65 * Math.pow(0.000125, sg - 1);

        //return (ibuData.getAlpha()/100) * ibuData.getWeight() * (ibuData.getTime()/100);

        double ibu = (ibuData.getAlpha() * ibuData.getWeight() * 75 * boilTimeFactor * bignessFactor) / volume;

        if (ibuData.getHopType() == HopType.PELLETS) {
            ibu *= PELLET_FACTOR;
        }

        return ibu;
    }

    /**
     * Parameters values should be defined in american units
     * @param ibuDatas hop data, percentage of alpha acids, weight in oz, boiling time
     * @param sg graity after boil
     * @param volume volume after boil in gallons
     * @return
     */
    public static double calcIBU(ArrayList<IBUData> ibuDatas, double sg, double volume, ExtractUnit extractUnit,
                                 VolumeUnit volumeUnit) {
        double sum = 0;
        for (IBUData ibuData: ibuDatas) {
            sum += calcIBU(ibuData, sg, volume, extractUnit, volumeUnit);
        }
        return sum;
    }
}
