package com.aariyan.platformreturns.Database;

import static com.aariyan.platformreturns.Constant.Constant.DEFAULT;

import android.content.Context;

import com.aariyan.platformreturns.Constant.Constant;


public class SharedPreferences {

    private Context context;


    public SharedPreferences(Context context) {
        this.context = context;
    }

    public void saveURL(String KEY, String URL) {
        android.content.SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = preferences.edit();

        editor.putString(Constant.IP_URL, URL);
        editor.commit();
    }


    public String getURL (String KEY, String URL_IDENTIFIER) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY,Context.MODE_PRIVATE);
        return sharedPreferences.getString(URL_IDENTIFIER, DEFAULT);
    }

}
