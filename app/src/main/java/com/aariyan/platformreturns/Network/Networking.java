package com.aariyan.platformreturns.Network;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.aariyan.platformreturns.Constant.Constant;
import com.aariyan.platformreturns.Interface.CustomerInterface;
import com.aariyan.platformreturns.Interface.RouteInterface;
import com.aariyan.platformreturns.Interface.UserList;
import com.aariyan.platformreturns.Model.RouteModel;
import com.aariyan.platformreturns.Model.StoreModel;
import com.aariyan.platformreturns.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class Networking {

    private List<UserModel> userList = new ArrayList<>();
    private CompositeDisposable userDisposable = new CompositeDisposable();
    private CompositeDisposable routeDisposable = new CompositeDisposable();
    private CompositeDisposable storeDisposable = new CompositeDisposable();
    APIs apIs;

    public Networking() {
        apIs = ApiClient.getClient().create(APIs.class);
    }


    //Getting the user list:
    public void getUserList(UserList userListInterface) {
        userDisposable.add(apIs.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray array = new JSONArray(responseBody.string());
                        parseJson(array, userListInterface);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        userListInterface.error(throwable.getMessage());
                    }
                }));

    }

    //Parsing the user list
    private void parseJson(JSONArray array, UserList userListInterface) {
        try {
            Log.d("TEST_RESULT", "1: " + userList.size());
            userList.clear();
            Log.d("TEST_RESULT", "2: " + userList.size());

            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject single = array.getJSONObject(i);
                    //taking particular element:
                    String UserName = single.getString("UserName");
                    int TabletUser = single.getInt("TabletUser");
                    int UserID = single.getInt("UserID");
                    String PinCode = single.getString("PinCode");


                    UserModel model = new UserModel(UserName, TabletUser, UserID, PinCode);
                    userList.add(model);
                }
                userListInterface.gotUserList(userList);

            } else {
                userListInterface.error("No users found!");
            }

        } catch (Exception e) {
            userListInterface.error("Error: " + e.getMessage());

        }
    }

    /**
     *
     * Route
     * @param routeInterface
     */
    public void getRoutes(RouteInterface routeInterface) {
        routeDisposable.add(apIs.getRoutes(Constant.TOKENS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray array = new JSONArray(responseBody.string());
                        parseRoute(array, routeInterface);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        routeInterface.onError(throwable.getMessage());
                    }
                }));
    }

    private void parseRoute(JSONArray array, RouteInterface routeInterface) {
        List<RouteModel> listOfRoutes = new ArrayList<>();
        try {
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject single = array.getJSONObject(i);
                    int routeId = single.getInt("Routeid");
                    String routeName = single.getString("Route");

                    RouteModel model = new RouteModel(routeId, routeName);
                    listOfRoutes.add(model);
                }
                routeInterface.onSuccess(listOfRoutes);
                if (!routeDisposable.isDisposed()) {
                    routeDisposable.clear();
                }
            } else {
                routeInterface.onError("No root found");
            }
        } catch (Exception e) {
            routeInterface.onError(e.getMessage());
        }

    }


    /**
     * Store
     *
     */

    public void getStores(CustomerInterface customerInterface, String date, int routeId) {
        routeDisposable.add(apIs.getStores(Constant.TOKENS, date, routeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray array = new JSONArray(responseBody.string());
                        parseStore(array, customerInterface);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        customerInterface.onError(throwable.getMessage());
                    }
                }));
    }

    private void parseStore(JSONArray array, CustomerInterface customerInterface) {
        List<StoreModel> listOfStores = new ArrayList<>();
        try {
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject single = array.getJSONObject(i);
                    String DeliveryDate = single.getString("DeliveryDate");
                    int routeId = single.getInt("RouteId");
                    String customerId = single.getString("CustomerId");
                    String customerCode = single.getString("CustomerCode");
                    String storeName = single.getString("StoreName");

                    StoreModel model = new StoreModel(DeliveryDate, routeId,customerId,customerCode, storeName);
                    listOfStores.add(model);
                }
                Log.d("STORE_RESPONSE", "parseStore: "+listOfStores.size());
                customerInterface.onSuccess(listOfStores);
                if (!routeDisposable.isDisposed()) {
                    routeDisposable.clear();
                }
            } else {
                customerInterface.onError("No root found");
            }
        } catch (Exception e) {
            customerInterface.onError(e.getMessage());
        }

    }
}
