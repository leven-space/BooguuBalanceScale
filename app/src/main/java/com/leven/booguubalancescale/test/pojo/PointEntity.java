package com.leven.booguubalancescale.test.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BooguuBalanceScale
 *
 * @author leven.chen
 * @email chenlong_cl@foxmail.com
 * 2017/3/25.
 */

public class PointEntity implements Parcelable {

    private int x;
    private int y;

    public PointEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.x);
        dest.writeInt(this.y);
    }

    protected PointEntity(Parcel in) {
        this.x = in.readInt();
        this.y = in.readInt();
    }

    public static final Parcelable.Creator<PointEntity> CREATOR = new Parcelable.Creator<PointEntity>() {
        @Override
        public PointEntity createFromParcel(Parcel source) {
            return new PointEntity(source);
        }

        @Override
        public PointEntity[] newArray(int size) {
            return new PointEntity[size];
        }
    };
}
