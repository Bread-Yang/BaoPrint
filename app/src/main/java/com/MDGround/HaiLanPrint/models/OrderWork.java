package com.MDGround.HaiLanPrint.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoghourt on 5/24/16.
 */

public class OrderWork implements Parcelable {

    private String CreateTime;

    private int OrderCount;

    private int OrderID;

    private List<OrderWorkPhoto> OrderWorkPhotos;

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

    protected OrderWork(Parcel in) {
        CreateTime = in.readString();
        OrderCount = in.readInt();
        OrderID = in.readInt();
        OrderWorkPhotos = in.createTypedArrayList(OrderWorkPhoto.CREATOR);
        PhotoCount = in.readInt();
        PhotoCover = in.readInt();
        Price = in.readInt();
        TemplateID = in.readInt();
        TypeID = in.readInt();
        TypeName = in.readString();
        WorkFormat = in.readString();
        WorkMaterial = in.readString();
        WorkOID = in.readInt();
        WorkStyle = in.readString();
    }

    public static final Creator<OrderWork> CREATOR = new Creator<OrderWork>() {
        @Override
        public OrderWork createFromParcel(Parcel in) {
            return new OrderWork(in);
        }

        @Override
        public OrderWork[] newArray(int size) {
            return new OrderWork[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CreateTime);
        dest.writeInt(OrderCount);
        dest.writeInt(OrderID);
        dest.writeTypedList(OrderWorkPhotos);
        dest.writeInt(PhotoCount);
        dest.writeInt(PhotoCover);
        dest.writeInt(Price);
        dest.writeInt(TemplateID);
        dest.writeInt(TypeID);
        dest.writeString(TypeName);
        dest.writeString(WorkFormat);
        dest.writeString(WorkMaterial);
        dest.writeInt(WorkOID);
        dest.writeString(WorkStyle);
    }
}
