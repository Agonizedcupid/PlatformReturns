package com.aariyan.platformreturns.Interface;

import androidx.lifecycle.MutableLiveData;

import com.aariyan.platformreturns.Model.UserModel;

import java.util.List;

public interface UserList {

    void gotUserList(List<UserModel> listOfUsers);
    void error(String error);
}
