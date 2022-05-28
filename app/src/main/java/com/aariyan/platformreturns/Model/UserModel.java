package com.aariyan.platformreturns.Model;

public class UserModel {
    private String UserName;
    private int TabletUser;
    private int UserID;
    private String PinCode;

    public UserModel() {}

    public UserModel(String userName, int tabletUser, int userID, String pinCode) {
        UserName = userName;
        TabletUser = tabletUser;
        UserID = userID;
        PinCode = pinCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getTabletUser() {
        return TabletUser;
    }

    public void setTabletUser(int tabletUser) {
        TabletUser = tabletUser;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }
}
