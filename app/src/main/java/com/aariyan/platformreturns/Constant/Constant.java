package com.aariyan.platformreturns.Constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Constant {

    public static String IP_URL = "IP_URL";
    public static String IP_MODE_KEY = "com.com.aariyan.platformreturns.BASE_URL";

    //public static String DEFAULT = "N/A";
    //public static String DEFAULT = "http://102.37.0.48/loadscan/";
    public static String DEFAULT = "https://linxsystems.flowgear.net/";
    public static String BASE_URL = "";

    public static String TOKENS = "Key=oWA1jvTk4ah8Uim7FTLl-GTSBpiwHP3UMIV3MCpyhbfaByH4vd3CX7NrwVRRfVr-Hn9dsWTAZmFjYX9UB2J9Yg";

    public static int userId = 1;

    public static String[] types = {"UPDATE"};
    public static int[] flag = {0, 1, 2};
    /*
    0 == Black zone
    1 == Green Zone
    2 == Red Zone
     */

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    //Checking internet is connected or not?
    public static boolean isInternetConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info!= null) {
                for (int i=0; i<info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
