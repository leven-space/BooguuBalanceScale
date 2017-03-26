package com.leven.booguubalancescale.test.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 传输结果对象
 * Created by thinkpad on 2017/3/26.
 */

public class ResultEntity implements Parcelable {

    private ArrayList<DataEntity> dataEntities;
    private ArrayList<PointEntity> pointEntities;
    private float xPoint;
    private float yPoint;

    public ResultEntity() {
        dataEntities = new ArrayList<>();
        pointEntities = new ArrayList<>();
    }

    public void add(DataEntity data) {
        dataEntities.add(data);
    }

    public void add(PointEntity point) {
        pointEntities.add(point);
    }

    public ArrayList<DataEntity> getDataEntities() {
        return dataEntities;
    }

    public void setDataEntities(ArrayList<DataEntity> dataEntities) {
        this.dataEntities = dataEntities;
    }

    public ArrayList<PointEntity> getPointEntities() {
        return pointEntities;
    }

    public void setPointEntities(ArrayList<PointEntity> pointEntities) {
        this.pointEntities = pointEntities;
    }

    public float getxPoint() {
        return xPoint;
    }

    public void setxPoint(float xPoint) {
        this.xPoint = xPoint;
    }

    public float getyPoint() {
        return yPoint;
    }

    public void setyPoint(float yPoint) {
        this.yPoint = yPoint;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.dataEntities);
        dest.writeTypedList(this.pointEntities);
        dest.writeFloat(this.xPoint);
        dest.writeFloat(this.yPoint);
    }

    protected ResultEntity(Parcel in) {
        this.dataEntities = in.createTypedArrayList(DataEntity.CREATOR);
        this.pointEntities = in.createTypedArrayList(PointEntity.CREATOR);
        this.xPoint = in.readFloat();
        this.yPoint = in.readFloat();
    }

    public static final Parcelable.Creator<ResultEntity> CREATOR = new Parcelable.Creator<ResultEntity>() {
        @Override
        public ResultEntity createFromParcel(Parcel source) {
            return new ResultEntity(source);
        }

        @Override
        public ResultEntity[] newArray(int size) {
            return new ResultEntity[size];
        }
    };
}
