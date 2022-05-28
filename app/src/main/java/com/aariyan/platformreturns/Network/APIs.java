package com.aariyan.platformreturns.Network;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIs {

    //old API for getting the user list:
//    @GET("users.php")
//    Call<ResponseBody> getUser();
    @GET("users.php")
    Observable<ResponseBody> getUser();

    //New api but for now it's not working:
    @GET("GetUsers/")
    Observable<ResponseBody> getUsers();

    @GET("OrdersAndOrderLines.php?")
    Call<ResponseBody> getOrderLines(@Query("routename") int routeName, @Query("ordertype") int orderType
            , @Query("deliverydate") String deliveryDate, @Query("userId") int userId);
}
