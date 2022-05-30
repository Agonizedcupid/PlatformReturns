package com.aariyan.platformreturns.Interface;

import com.aariyan.platformreturns.Model.StoreModel;

import java.util.List;

public interface CustomerInterface {

    void onSuccess(List<StoreModel> listOfStore);
    void onError(String errorMessage);
}
