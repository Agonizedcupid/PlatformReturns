package com.aariyan.platformreturns.Network;

import com.aariyan.platformreturns.Constant.Constant;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    @GET("GetRoutes")
    Observable<ResponseBody> getRoutes(@Header("Authorization") String authorization);

    @GET("GetStores/{date}/{id}")
    Observable<ResponseBody> getStores(@Header("Authorization") String authorization, @Path("date") String date, @Path("id") int routeId);

    @GET("OrdersAndOrderLines.php?")
    Call<ResponseBody> getOrderLines(@Query("routename") int routeName, @Query("ordertype") int orderType
            , @Query("deliverydate") String deliveryDate, @Query("userId") int userId);
}
