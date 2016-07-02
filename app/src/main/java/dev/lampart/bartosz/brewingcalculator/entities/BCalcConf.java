package dev.lampart.bartosz.brewingcalculator.entities;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;

/**
 * Created by bartek on 01.07.2016.
 */
public class BCalcConf {
    private ExtractUnit defExtractUnit;
    private boolean defUseRefractometer;
    private double defWortCorrectionFactor;

    public BCalcConf() {
        this.defExtractUnit = ExtractUnit.Plato;
        this.defUseRefractometer = false;
        this.defWortCorrectionFactor = 1.04;
    }

    public BCalcConf(ExtractUnit defExtractUnit, boolean defUseRefractometer, double defWortCorrectionFactor) {
        this.defExtractUnit = defExtractUnit;
        this.defUseRefractometer = defUseRefractometer;
        this.defWortCorrectionFactor = defWortCorrectionFactor;
    }

    public ExtractUnit getDefExtractUnit() {
        return defExtractUnit;
    }

    public void setDefExtractUnit(ExtractUnit defExtractUnit) {
        this.defExtractUnit = defExtractUnit;
    }

    public boolean isDefUseRefractometer() {
        return defUseRefractometer;
    }

    public void setDefUseRefractometer(boolean defUseRefractometer) {
        this.defUseRefractometer = defUseRefractometer;
    }

    public double getDefWortCorrectionFactor() {
        return defWortCorrectionFactor;
    }

    public void setDefWortCorrectionFactor(double defWortCorrectionFactor) {
        this.defWortCorrectionFactor = defWortCorrectionFactor;
    }
}
