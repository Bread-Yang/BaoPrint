package com.MDGround.HaiLanPrint.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    List<UserKidList> UserKidList;

    public List<com.MDGround.HaiLanPrint.models.UserKidList> getUserKidList() {
        return UserKidList;
    }

    public void setUserKidList(List<com.MDGround.HaiLanPrint.models.UserKidList> userKidList) {
        UserKidList = userKidList;
    }

    public int UserID;

    public int UserRole;

    public String WXOpenID;

    public String WBUID;

    public String QQOpenID;

    public String Phone;

    public String Password;

    public int Status;

    public String UserName;

    public String UserNickName;

    public String DOB;

    public int Gender;

    public int PhotoID;

    public int PhotoSID;

    public int CountryID;

    public int ProvinceID;

    public int CityID;

    public int DistrictID;

    public int SystemSetting;

    public String InvitationCode;

    public String UpdatedTime;

    public String CreatedTime;

    public int DeviceID;

    public String ServiceToken;

    public String ChildName;

    public String ChildDOB;

    public String ChildSchool;

    public String ChildClass;

    public User() {
        InvitationCode = "";
        ChildDOB = "";
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getUserRole() {
        return UserRole;
    }

    public void setUserRole(int userRole) {
        UserRole = userRole;
    }

    public String getWXOpenID() {
        return WXOpenID;
    }

    public void setWXOpenID(String WXOpenID) {
        this.WXOpenID = WXOpenID;
    }

    public String getWBUID() {
        return WBUID;
    }

    public void setWBUID(String WBUID) {
        this.WBUID = WBUID;
    }

    public String getQQOpenID() {
        return QQOpenID;
    }

    public void setQQOpenID(String QQOpenID) {
        this.QQOpenID = QQOpenID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
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

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public int getSystemSetting() {
        return SystemSetting;
    }

    public void setSystemSetting(int systemSetting) {
        SystemSetting = systemSetting;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        InvitationCode = invitationCode;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        UpdatedTime = updatedTime;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public int getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(int deviceID) {
        DeviceID = deviceID;
    }

    public String getServiceToken() {
        return ServiceToken;
    }

    public String getChildName() {
        return ChildName;
    }

    public void setChildName(String childName) {
        ChildName = childName;
    }

    public String getChildDOB() {
        return ChildDOB;
    }

    public void setChildDOB(String childDOB) {
        ChildDOB = childDOB;
    }

    public String getChildSchool() {
        return ChildSchool;
    }

    public void setChildSchool(String childSchool) {
        ChildSchool = childSchool;
    }

    public String getChildClass() {
        return ChildClass;
    }

    public void setChildClass(String childClass) {
        ChildClass = childClass;
    }

    public void setServiceToken(String serviceToken) {
        ServiceToken = serviceToken;
    }
}
