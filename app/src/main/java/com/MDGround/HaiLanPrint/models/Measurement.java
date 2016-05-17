package com.MDGround.HaiLanPrint.models;

import java.util.Date;

/**
 * Created by yoghourt on 5/16/16.
 */
public class Measurement {

    private int AutoID;

    private float Price;

    private String PriceDesc;

    private String Title;

    private String TitleSub;

    private int TypeID;

    private Date UpdatedTime;

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getPriceDesc() {
        return PriceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        PriceDesc = priceDesc;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTitleSub() {
        return TitleSub;
    }

    public void setTitleSub(String titleSub) {
        TitleSub = titleSub;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }

    public Date getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        UpdatedTime = updatedTime;
    }
}
