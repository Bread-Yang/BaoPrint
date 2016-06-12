package com.MDGround.HaiLanPrint.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.MDGround.HaiLanPrint.BR;

/**
 * Created by yoghourt on 5/25/16.
 */

public class Template extends BaseObservable {

    private String MaterialDesc;

    private int MaterialType;

    private int PageCount;

    private int ParentID;

    private int Price;

    private String PriceString;

    private int Status;

    private String StatusString;

    private int TemplateID;

    private String TemplateName;

    private String TemplateType;

    private String TypeDesc;

    private int TypeDescID;

    private int TypeID;

    private String UpdatedTime;

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public int getMaterialType() {
        return MaterialType;
    }

    public void setMaterialType(int materialType) {
        MaterialType = materialType;
    }

    @Bindable
    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
        notifyPropertyChanged(BR.pageCount);
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getPriceString() {
        return PriceString;
    }

    public void setPriceString(String priceString) {
        PriceString = priceString;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusString() {
        return StatusString;
    }

    public void setStatusString(String statusString) {
        StatusString = statusString;
    }

    public int getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(int templateID) {
        TemplateID = templateID;
    }

    public String getTemplateName() {
        return TemplateName;
    }

    public void setTemplateName(String templateName) {
        TemplateName = templateName;
    }

    public String getTemplateType() {
        return TemplateType;
    }

    public void setTemplateType(String templateType) {
        TemplateType = templateType;
    }

    public String getTypeDesc() {
        return TypeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        TypeDesc = typeDesc;
    }

    public int getTypeDescID() {
        return TypeDescID;
    }

    public void setTypeDescID(int typeDescID) {
        TypeDescID = typeDescID;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        UpdatedTime = updatedTime;
    }
}
