package dev.lampart.bartosz.brewingcalculator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;

/**
 * Created by bartek on 02.10.2016.
 */
public class IBUData implements Parcelable {
    private double alpha;
    private double weight;
    private double time;
    private WeightUnit weightUnit;
    private HopType hopType;

    public IBUData(double alpha, double weight, WeightUnit weightUnit, double time, HopType hopType) {
        this.alpha = alpha;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.time = time;
        this.hopType = hopType;
    }

    public IBUData(Parcel in) {
        this.alpha = in.readDouble();
        this.weight = in.readDouble();
        this.time = in.readDouble();
        this.weightUnit = (WeightUnit) in.readValue(WeightUnit.class.getClassLoader());
        this.hopType = (HopType) in.readValue(HopType.class.getClassLoader());
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

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public HopType getHopType() {
        return hopType;
    }

    public void setHopType(HopType hopType) {
        this.hopType = hopType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(alpha);
        parcel.writeDouble(weight);
        parcel.writeDouble(time);
        parcel.writeValue(weightUnit);
        parcel.writeValue(hopType);
    }

    public static final Parcelable.Creator<IBUData> CREATOR = new Parcelable.Creator<IBUData>() {
        public IBUData createFromParcel(Parcel in) {
            return new IBUData(in);
        }

        public IBUData[] newArray(int size) {
            return new IBUData[size];
        }
    };
}
