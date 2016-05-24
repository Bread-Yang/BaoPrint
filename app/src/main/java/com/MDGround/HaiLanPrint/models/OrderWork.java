package com.MDGround.HaiLanPrint.models;

import java.util.Date;

/**
 * Created by yoghourt on 5/24/16.
 */

public class OrderWork {

    private int WorkOID;

    private int OrderID;

    private int TypeID;

    private String TypeName;

    private int PhotoCover;

    private int PhotoCount;

    private int OrderCount;

    private int TemplateID;

    private String WorkMaterial;

    private String WorkFormat;

    private String WorkStyle;

    private Date CreateTime;

    public OrderWork() {
        WorkMaterial = "";
        WorkFormat = "";
        WorkStyle = "";
    }

    public int getWorkOID() {
        return WorkOID;
    }

    public void setWorkOID(int workOID) {
        WorkOID = workOID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
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

    public int getPhotoCover() {
        return PhotoCover;
    }

    public void setPhotoCover(int photoCover) {
        PhotoCover = photoCover;
    }

    public int getPhotoCount() {
        return PhotoCount;
    }

    public void setPhotoCount(int photoCount) {
        PhotoCount = photoCount;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public int getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(int templateID) {
        TemplateID = templateID;
    }

    public String getWorkMaterial() {
        return WorkMaterial;
    }

    public void setWorkMaterial(String workMaterial) {
        WorkMaterial = workMaterial;
    }

    public String getWorkFormat() {
        return WorkFormat;
    }

    public void setWorkFormat(String workFormat) {
        WorkFormat = workFormat;
    }

    public String getWorkStyle() {
        return WorkStyle;
    }

    public void setWorkStyle(String workStyle) {
        WorkStyle = workStyle;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }
}
