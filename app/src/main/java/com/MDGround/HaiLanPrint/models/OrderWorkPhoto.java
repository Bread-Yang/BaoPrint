package com.MDGround.HaiLanPrint.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoghourt on 5/24/16.
 */

public class OrderWorkPhoto implements Parcelable{

    private int AutoID;

    private int Photo1ID;

    private int Photo1SID;

    private int Photo2ID;

    private int Photo2SID;

    private int photoCount;

    private int PhotoIndex;

    private String tDescription;

    private int WorkOID;

    public OrderWorkPhoto() {

    }

    protected OrderWorkPhoto(Parcel in) {
        AutoID = in.readInt();
        Photo1ID = in.readInt();
        Photo1SID = in.readInt();
        Photo2ID = in.readInt();
        Photo2SID = in.readInt();
        photoCount = in.readInt();
        PhotoIndex = in.readInt();
        tDescription = in.readString();
        WorkOID = in.readInt();
    }

    public static final Creator<OrderWorkPhoto> CREATOR = new Creator<OrderWorkPhoto>() {
        @Override
        public OrderWorkPhoto createFromParcel(Parcel in) {
            return new OrderWorkPhoto(in);
        }

        @Override
        public OrderWorkPhoto[] newArray(int size) {
            return new OrderWorkPhoto[size];
        }
    };

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public int getPhoto1ID() {
        return Photo1ID;
    }

    public void setPhoto1ID(int photo1ID) {
        Photo1ID = photo1ID;
    }

    public int getPhoto1SID() {
        return Photo1SID;
    }

    public void setPhoto1SID(int photo1SID) {
        Photo1SID = photo1SID;
    }

    public int getPhoto2ID() {
        return Photo2ID;
    }

    public void setPhoto2ID(int photo2ID) {
        Photo2ID = photo2ID;
    }

    public int getPhoto2SID() {
        return Photo2SID;
    }

    public void setPhoto2SID(int photo2SID) {
        Photo2SID = photo2SID;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public int getPhotoIndex() {
        return PhotoIndex;
    }

    public void setPhotoIndex(int photoIndex) {
        PhotoIndex = photoIndex;
    }

    public String gettDescription() {
        return tDescription;
    }

    public void settDescription(String tDescription) {
        this.tDescription = tDescription;
    }

    public int getWorkOID() {
        return WorkOID;
    }

    public void setWorkOID(int workOID) {
        WorkOID = workOID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(AutoID);
        dest.writeInt(Photo1ID);
        dest.writeInt(Photo1SID);
        dest.writeInt(Photo2ID);
        dest.writeInt(Photo2SID);
        dest.writeInt(photoCount);
        dest.writeInt(PhotoIndex);
        dest.writeString(tDescription);
        dest.writeInt(WorkOID);
    }
}
