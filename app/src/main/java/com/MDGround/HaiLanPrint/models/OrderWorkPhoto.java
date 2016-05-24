package com.MDGround.HaiLanPrint.models;

/**
 * Created by yoghourt on 5/24/16.
 */

public class OrderWorkPhoto {

    private int AutoID;

    private int WorkOID;

    private int Photo1ID;

    private int Photo1SID;

    private int Photo2ID;

    private int Photo2SID;

    private int PhotoIndex;

    public int getPhoto2ID() {
        return Photo2ID;
    }

    public void setPhoto2ID(int photo2ID) {
        Photo2ID = photo2ID;
    }

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public int getWorkOID() {
        return WorkOID;
    }

    public void setWorkOID(int workOID) {
        WorkOID = workOID;
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

    public int getPhoto2SID() {
        return Photo2SID;
    }

    public void setPhoto2SID(int photo2SID) {
        Photo2SID = photo2SID;
    }

    public int getPhotoIndex() {
        return PhotoIndex;
    }

    public void setPhotoIndex(int photoIndex) {
        PhotoIndex = photoIndex;
    }
}
