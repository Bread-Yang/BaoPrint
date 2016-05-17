package com.MDGround.HaiLanPrint.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.MDGround.HaiLanPrint.BR;

/**
 * Created by yoghourt on 5/12/16.
 */
public class MDImage extends BaseObservable implements Parcelable {

    private int AutoID;

    private int PhotoID;

    private int PhotoSID;

    private int PhotoCount;

    private boolean Shared;

    private String imageLocalPath;

    private long duration;

    private long lastUpdateAt;

    public MDImage(String imageLocalPath, long lastUpdateAt, long duration) {
        this.imageLocalPath = imageLocalPath;
        this.duration = duration;
        this.lastUpdateAt = lastUpdateAt;
    }

    protected MDImage(Parcel in) {
        AutoID = in.readInt();
        PhotoID = in.readInt();
        PhotoSID = in.readInt();
        PhotoCount = in.readInt();
        Shared = in.readByte() != 0;
        imageLocalPath = in.readString();
        duration = in.readLong();
        lastUpdateAt = in.readLong();
    }

    public static final Creator<MDImage> CREATOR = new Creator<MDImage>() {
        @Override
        public MDImage createFromParcel(Parcel in) {
            return new MDImage(in);
        }

        @Override
        public MDImage[] newArray(int size) {
            return new MDImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(AutoID);
        dest.writeInt(PhotoID);
        dest.writeInt(PhotoSID);
        dest.writeInt(PhotoCount);
        dest.writeByte((byte) (Shared ? 1 : 0));
        dest.writeString(imageLocalPath);
        dest.writeLong(duration);
        dest.writeLong(lastUpdateAt);
    }

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
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

    @Bindable
    public int getPhotoCount() {
        return PhotoCount;
    }

    public void setPhotoCount(int photoCount) {
        PhotoCount = photoCount;
        notifyPropertyChanged(BR.photoCount);
    }

    public boolean isShared() {
        return Shared;
    }

    public void setShared(boolean shared) {
        Shared = shared;
    }

    public String getImageLocalPath() {
        return imageLocalPath;
    }

    public void setImageLocalPath(String imageLocalPath) {
        this.imageLocalPath = imageLocalPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
