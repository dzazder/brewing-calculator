package dev.lampart.bartosz.brewingcalculator.entities;

public class ExtractRaws {
    private double liquidMaltExtract;
    private double dryMaltExtract;
    private double cornSugar;

    public ExtractRaws() {}

    public ExtractRaws(double liquidMaltExtract, double dryMaltExtract, double cornSugar) {
        this.liquidMaltExtract = liquidMaltExtract;
        this.dryMaltExtract = dryMaltExtract;
        this.cornSugar = cornSugar;
    }

    public double getLiquidMaltExtract() {
        return liquidMaltExtract;
    }

    public void setLiquidMaltExtract(double liquidMaltExtract) {
        this.liquidMaltExtract = liquidMaltExtract;
    }

    public double getDryMaltExtract() {
        return dryMaltExtract;
    }

    public void setDryMaltExtract(double dryMaltExtract) {
        this.dryMaltExtract = dryMaltExtract;
    }

    public double getCornSugar() {
        return cornSugar;
    }

    public void setCornSugar(double cornSugar) {
        this.cornSugar = cornSugar;
    }
}
