package com.leven.booguubalancescale.test.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thinkpad on 2017/3/26.
 */

public class DataEntity implements Parcelable {

    private double xg;
    private double yg;
    private double zg;

    public DataEntity(double xg, double yg, double zg) {
        this.xg = xg;
        this.yg = yg;
        this.zg = zg;
    }

    public double getXg() {
        return xg;
    }

    public void setXg(double xg) {
        this.xg = xg;
    }

    public double getYg() {
        return yg;
    }

    public void setYg(double yg) {
        this.yg = yg;
    }

    public double getZg() {
        return zg;
    }

    public void setZg(double zg) {
        this.zg = zg;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "xg=" + xg +
                ", yg=" + yg +
                ", zg=" + zg +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.xg);
        dest.writeDouble(this.yg);
        dest.writeDouble(this.zg);
    }

    protected DataEntity(Parcel in) {
        this.xg = in.readDouble();
        this.yg = in.readDouble();
        this.zg = in.readDouble();
    }

    public static final Parcelable.Creator<DataEntity> CREATOR = new Parcelable.Creator<DataEntity>() {
        @Override
        public DataEntity createFromParcel(Parcel source) {
            return new DataEntity(source);
        }

        @Override
        public DataEntity[] newArray(int size) {
            return new DataEntity[size];
        }
    };
}
