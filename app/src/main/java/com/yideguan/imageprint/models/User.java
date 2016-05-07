package com.yideguan.imageprint.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class User implements Parcelable{

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

    public Date DOB;

    public int Gender;

    public int PhotoID;

    public int PhotoSID;

    public int CountryID;

    public int ProvinceID;

    public int CityID;

    public int DistrictID;

    public int SystemSetting;

    public String InvitationCode;

    public Date UpdatedTime;

    public Date CreatedTime;

    public int DeviceID;

    public String ServiceToken;

    public User() {

    }

    protected User(Parcel in) {
        UserID = in.readInt();
        UserRole = in.readInt();
        WXOpenID = in.readString();
        WBUID = in.readString();
        QQOpenID = in.readString();
        Phone = in.readString();
        Password = in.readString();
        Status = in.readInt();
        UserName = in.readString();
        UserNickName = in.readString();
        Gender = in.readInt();
        PhotoID = in.readInt();
        PhotoSID = in.readInt();
        CountryID = in.readInt();
        ProvinceID = in.readInt();
        CityID = in.readInt();
        DistrictID = in.readInt();
        SystemSetting = in.readInt();
        InvitationCode = in.readString();
        DeviceID = in.readInt();
        ServiceToken = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
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

    public Date getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        UpdatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Date createdTime) {
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

    public void setServiceToken(String serviceToken) {
        ServiceToken = serviceToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(UserID);
        dest.writeInt(UserRole);
        dest.writeString(WXOpenID);
        dest.writeString(WBUID);
        dest.writeString(QQOpenID);
        dest.writeString(Phone);
        dest.writeString(Password);
        dest.writeInt(Status);
        dest.writeString(UserName);
        dest.writeString(UserNickName);
        dest.writeInt(Gender);
        dest.writeInt(PhotoID);
        dest.writeInt(PhotoSID);
        dest.writeInt(CountryID);
        dest.writeInt(ProvinceID);
        dest.writeInt(CityID);
        dest.writeInt(DistrictID);
        dest.writeInt(SystemSetting);
        dest.writeString(InvitationCode);
        dest.writeInt(DeviceID);
        dest.writeString(ServiceToken);
    }
}
