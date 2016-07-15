package dev.lampart.bartosz.brewingcalculator.entities;

import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;

/**
 * Created by bartek on 01.07.2016.
 */
public class BCalcConf {
    private ExtractUnit defExtractUnit;
    private boolean defUseRefractometer;
    private double defWortCorrectionFactor;
    private double defPrimingSize;
    private VolumeUnit defVolumeUnit;
    private double defTemperature;
    private TemperatureUnit defTempUnit;

    public BCalcConf() {
        this.defExtractUnit = ExtractUnit.Plato;
        this.defUseRefractometer = false;
        this.defWortCorrectionFactor = 1.04;
        this.defPrimingSize = 20;
        this.defVolumeUnit = VolumeUnit.Liter;
        this.defTemperature = 20;
        this.defTempUnit = TemperatureUnit.C;
    }

    public BCalcConf(ExtractUnit defExtractUnit, boolean defUseRefractometer, double defWortCorrectionFactor,
                     double defPrimingSize, VolumeUnit defVolumeUnit, double defTemp, TemperatureUnit defTempUnit) {
        this.defExtractUnit = defExtractUnit;
        this.defUseRefractometer = defUseRefractometer;
        this.defWortCorrectionFactor = defWortCorrectionFactor;
        this.defPrimingSize = defPrimingSize;
        this.defVolumeUnit = defVolumeUnit;
        this.defTemperature = defTemp;
        this.defTempUnit = defTempUnit;
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

    public double getDefPrimingSize() {
        return defPrimingSize;
    }

    public void setDefPrimingSize(double defPrimingSize) {
        this.defPrimingSize = defPrimingSize;
    }

    public VolumeUnit getDefVolumeUnit() {
        return defVolumeUnit;
    }

    public void setDefVolumeUnit(VolumeUnit defVolumeUnit) {
        this.defVolumeUnit = defVolumeUnit;
    }

    public double getDefTemperature() {
        return defTemperature;
    }

    public void setDefTemperature(double defTemperature) {
        this.defTemperature = defTemperature;
    }

    public TemperatureUnit getDefTempUnit() {
        return defTempUnit;
    }

    public void setDefTempUnit(TemperatureUnit defTempUnit) {
        this.defTempUnit = defTempUnit;
    }
}
