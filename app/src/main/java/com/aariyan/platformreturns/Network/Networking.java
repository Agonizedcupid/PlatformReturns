package com.aariyan.platformreturns.Network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.aariyan.platformreturns.Interface.UserList;
import com.aariyan.platformreturns.Model.UserModel;

import org.json.JSONArray;
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
    APIs apIs;

    public Networking() {
        apIs = ApiClient.getClient().create(APIs.class);
    }


    //Getting the user list:
    public void getUserList(UserList userListInterface) {
        userDisposable.add(apIs.getUser()
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
            Log.d("TEST_RESULT", "1: "+userList.size());
            userList.clear();
            Log.d("TEST_RESULT", "2: "+userList.size());

            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject single = array.getJSONObject(i);
                    //taking particular element:
                    String UserName = single.getString("UserName");
                    int TabletUser = single.getInt("TabletUser");
                    int UserID = single.getInt("UserID");
                    int PinCode = single.getInt("PinCode");
                    String strQRCode = single.getString("strQRCode");
                    int GroupId = single.getInt("GroupId");

                    UserModel model = new UserModel(UserName, TabletUser, UserID, PinCode, strQRCode, GroupId);
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
}
