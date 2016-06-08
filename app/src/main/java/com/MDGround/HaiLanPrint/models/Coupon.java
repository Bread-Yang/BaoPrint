package com.MDGround.HaiLanPrint.models;

/**
 * Created by yoghourt on 6/1/16.
 */

public class Coupon {

    private String ActiveTime;

    private int AutoID;

    private int CouponID;

    private String CouponName;

    private String CouponNo;

    private int CouponStatus;

    private String CreatedTime;

    private String ExpireTime;

    private int Price;

    private int PriceLimit;

    private int UserID;

    public String getActiveTime() {
        return ActiveTime;
    }

    public void setActiveTime(String activeTime) {
        ActiveTime = activeTime;
    }

    public int getAutoID() {
        return AutoID;
    }

    public void setAutoID(int autoID) {
        AutoID = autoID;
    }

    public int getCouponID() {
        return CouponID;
    }

    public void setCouponID(int couponID) {
        CouponID = couponID;
    }

    public String getCouponName() {
        return CouponName;
    }

    public void setCouponName(String couponName) {
        CouponName = couponName;
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
