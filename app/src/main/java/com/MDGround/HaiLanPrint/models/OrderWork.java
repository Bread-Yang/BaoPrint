package com.MDGround.HaiLanPrint.models;

/**
 * Created by yoghourt on 5/24/16.
 */

public class OrderWork {

    private String CreateTime;

    private int OrderCount;

    private int OrderID;

    private int PhotoCount;

    private int PhotoCover;

    private int Price;

    private int TemplateID;

    private int TypeID;

    private String TypeName;

    private String WorkFormat;

    private String WorkMaterial;

    private int WorkOID;

    private String WorkStyle;

    public OrderWork() {
        WorkMaterial = "";
        WorkFormat = "";
        WorkStyle = "";
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getPhotoCount() {
        return PhotoCount;
    }

    public void setPhotoCount(int photoCount) {
        PhotoCount = photoCount;
    }

    public int getPhotoCover() {
        return PhotoCover;
    }

    public void setPhotoCover(int photoCover) {
        PhotoCover = photoCover;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(int templateID) {
        TemplateID = templateID;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getWorkFormat() {
        return WorkFormat;
    }

    public void setWorkFormat(String workFormat) {
        WorkFormat = workFormat;
    }

    public String getWorkMaterial() {
        return WorkMaterial;
    }

    public void setWorkMaterial(String workMaterial) {
        WorkMaterial = workMaterial;
    }

    public int getWorkOID() {
        return WorkOID;
    }

    public void setWorkOID(int workOID) {
        WorkOID = workOID;
    }

    public String getWorkStyle() {
        return WorkStyle;
    }

    public void setWorkStyle(String workStyle) {
        WorkStyle = workStyle;
    }
}
