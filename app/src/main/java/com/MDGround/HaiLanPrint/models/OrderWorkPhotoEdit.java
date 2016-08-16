package com.MDGround.HaiLanPrint.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoghourt on 8/5/16.
 */
public class OrderWorkPhotoEdit implements Parcelable{

    private int AutoID;
    private int ParentID;
    private int PhotoID;
    private int PhotoSID;

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public int getPhotoID() {
        return PhotoID;
    }

    public void setPhotoID(int photoID) {
        PhotoID = photoID;
    }

    public int getPhotoSID() {
        return PhotoSID;
    }

    public void setPhotoSID(int photoSID) {
        PhotoSID = photoSID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.AutoID);
        dest.writeInt(this.ParentID);
        dest.writeInt(this.PhotoID);
        dest.writeInt(this.PhotoSID);
    }

    public OrderWorkPhotoEdit() {
    }

    protected OrderWorkPhotoEdit(Parcel in) {
        this.AutoID = in.readInt();
        this.ParentID = in.readInt();
        this.PhotoID = in.readInt();
        this.PhotoSID = in.readInt();
    }

    public static final Creator<OrderWorkPhotoEdit> CREATOR = new Creator<OrderWorkPhotoEdit>() {
        @Override
        public OrderWorkPhotoEdit createFromParcel(Parcel source) {
            return new OrderWorkPhotoEdit(source);
        }

        @Override
        public OrderWorkPhotoEdit[] newArray(int size) {
            return new OrderWorkPhotoEdit[size];
        }
    };
}
