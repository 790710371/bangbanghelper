package com.mero.wyt_register.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlei on 2016/10/28.
 */

public class DeviceModelBean implements Parcelable {

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String model;//型号
    public String brand;//品牌
    public String manufacturer;//制造商

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model);
        dest.writeString(this.brand);
        dest.writeString(this.manufacturer);
    }

    public DeviceModelBean() {
    }

    protected DeviceModelBean(Parcel in) {
        this.model = in.readString();
        this.brand = in.readString();
        this.manufacturer = in.readString();
    }

    public static final Parcelable.Creator<DeviceModelBean> CREATOR = new Parcelable.Creator<DeviceModelBean>() {
        @Override
        public DeviceModelBean createFromParcel(Parcel source) {
            return new DeviceModelBean(source);
        }

        @Override
        public DeviceModelBean[] newArray(int size) {
            return new DeviceModelBean[size];
        }
    };
}
