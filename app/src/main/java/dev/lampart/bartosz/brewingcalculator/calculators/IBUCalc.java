package dev.lampart.bartosz.brewingcalculator.calculators;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

/**
 * Created by bartek on 30.09.2016.
 * Formulas to calc IBU: http://www.realbeer.com/hops/FAQ.html
 * https://hopsteiner.com/ibu-calculator/
 */
public class IBUCalc extends Calc {

    private static double PELLET_FACTOR = 1.15;

    public enum FormulaTypeIBU {
        RAGER, GARETZ, TINSETH
    }

    public static double calcIBU(IBUData ibuData, double sg, double volume, ExtractUnit extractUnit,
                                 VolumeUnit volumeUnit, FormulaTypeIBU formula) {
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

        double ibu = 0;

        switch (formula) {
            case GARETZ: ibu = calcIBUGaretz(); break;
            case TINSETH: ibu = calcIBUTinseth(ibuData, sg, volume); break;
            case RAGER: ibu = calcIBURager(ibuData, sg, volume); break;
        }

        return ibu;
    }

    private static double calcIBUGaretz() {
        return 0;
    }

    private static double calcIBUTinseth(IBUData ibuData, double sg, double volume) {
        double weight = ibuData.getWeight();
        if (ibuData.getWeightUnit() == WeightUnit.G) {
            weight = UnitCalc.calcGramsToOunces(ibuData.getWeight());
        }

        double boilTimeFactor = (1 - Math.exp(-0.04 * ibuData.getTime())) / 4.15;
        double bignessFactor = 1.65 * Math.pow(0.000125, sg - 1);

        double ibu = (ibuData.getAlpha() * weight * 74.9 * boilTimeFactor * bignessFactor) / volume;

        // is correct in TINSETH?????
        if (ibuData.getHopType() == HopType.PELLETS) {
            ibu *= PELLET_FACTOR;
        }

        return ibu;
    }

    private static double calcIBURager(IBUData ibuData, double sg, double volume) {
        double weight = ibuData.getWeight();
        if (ibuData.getWeightUnit() == WeightUnit.G) {
            weight = UnitCalc.calcGramsToOunces(weight);
        }
        
        double utilization = 18.11 + (13.86 * Math.tanh((ibuData.getTime() - 31.32)/18.27));
        //Log.d("IBU", "Utilization: " + utilization);
        double ga = sg > 1.05 ? (sg - 1.05) / 0.2 : 0;
        //Log.d("IBU", "GA: " + ga);

        double ibu = (weight * (utilization/100) * (ibuData.getAlpha()/100) * 7462) / (volume * (1 + ga));
        //Log.d("IBU", "IBU RAGER: " + ibu);
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
                                 VolumeUnit volumeUnit, FormulaTypeIBU formula ) {
        double sum = 0;
        for (IBUData ibuData: ibuDatas) {
            sum += calcIBU(ibuData, sg, volume, extractUnit, volumeUnit, formula);
        }
        return sum;
    }
}
