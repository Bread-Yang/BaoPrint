package com.MDGround.HaiLanPrint.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yoghourt on 5/24/16.
 */

public class DeliveryAddress implements Parcelable {

    private int AutoID;

    private int CityID;

    private int CountryID;

    private int DistrictID;

    private String Phone;

    private int ProvinceID;

    private String Receiver;

    private String Street;

    private Date UpdatedTime;

    private int UserID;

    public DeliveryAddress() {}

    protected DeliveryAddress(Parcel in) {
        AutoID = in.readInt();
        CityID = in.readInt();
        CountryID = in.readInt();
        DistrictID = in.readInt();
        Phone = in.readString();
        ProvinceID = in.readInt();
        Receiver = in.readString();
        Street = in.readString();
        UserID = in.readInt();
    }

    public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
        @Override
        public DeliveryAddress createFromParcel(Parcel in) {
            return new DeliveryAddress(in);
        }

        @Override
        public DeliveryAddress[] newArray(int size) {
            return new DeliveryAddress[size];
        }
    };

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public Date getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        UpdatedTime = updatedTime;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(AutoID);
        dest.writeInt(CityID);
        dest.writeInt(CountryID);
        dest.writeInt(DistrictID);
        dest.writeString(Phone);
        dest.writeInt(ProvinceID);
        dest.writeString(Receiver);
        dest.writeString(Street);
        dest.writeInt(UserID);
    }
}
