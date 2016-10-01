package dev.lampart.bartosz.brewingcalculator.entities;

/**
 * Created by bartek on 02.10.2016.
 */
public class IBUData {
    private double alpha;
    private double weight;
    private double time;

    public IBUData() {

    }

    public IBUData(double alpha, double weight, double time) {
        this.alpha = alpha;
        this.weight = weight;
        this.time = time;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
