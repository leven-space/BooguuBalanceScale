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

    public ResultEntity() {
        dataEntities = new ArrayList<>();
    }

    public void add(DataEntity data) {
        dataEntities.add(data);
    }

    public ArrayList<DataEntity> getDataEntities() {
        return dataEntities;
    }

    public void setDataEntities(ArrayList<DataEntity> dataEntities) {
        this.dataEntities = dataEntities;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "dataEntities=" + dataEntities +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.dataEntities);
    }

    protected ResultEntity(Parcel in) {
        this.dataEntities = new ArrayList<DataEntity>();
        in.readList(this.dataEntities, DataEntity.class.getClassLoader());
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
