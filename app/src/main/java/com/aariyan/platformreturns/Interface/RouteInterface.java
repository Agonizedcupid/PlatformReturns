package com.aariyan.platformreturns.Interface;

import com.aariyan.platformreturns.Model.RouteModel;

import java.util.List;

public interface RouteInterface {

    void onSuccess(List<RouteModel> listOfRoutes);
    void onError(String errorMessage);
}
