package com.MDGround.HaiLanPrint.models;

/**
 * Created by yoghourt on 6/1/16.
 */

public class Coupon {

    private String ActivationCode;

    private int AutoID;

    private String CouponNo;

    private int CouponStatus;

    private String CreatedTime;

    private String ExpireTime;

    private int Price;

    private int PriceLimit;

    private int UserID;

    public String getActivationCode() {
        return ActivationCode;
    }

    public void setActivationCode(String activationCode) {
        ActivationCode = activationCode;
    }

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public String getCouponNo() {
        return CouponNo;
    }

    public void setCouponNo(String couponNo) {
        CouponNo = couponNo;
    }

    public int getCouponStatus() {
        return CouponStatus;
    }

    public void setCouponStatus(int couponStatus) {
        CouponStatus = couponStatus;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getExpireTime() {
        return ExpireTime;
    }

    public void setExpireTime(String expireTime) {
        ExpireTime = expireTime;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getPriceLimit() {
        return PriceLimit;
    }

    public void setPriceLimit(int priceLimit) {
        PriceLimit = priceLimit;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
