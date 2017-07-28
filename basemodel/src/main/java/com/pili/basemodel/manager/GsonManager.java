package com.pili.basemodel.manager;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Gson辅助类
 * Created by lhw on 2016/11/1 0001.
 */

public class GsonManager<T> {
    private static final String TAG = "GsonManager";
    private static Gson sGson = new Gson();

    public String convert2String(List<T> list) {
        try {
            return sGson.toJson(list);
        } catch (Exception e) {
            Log.e(TAG, "GsonManager:convert2String " + e);
            return "";
        }
    }

    public List<T> fromJson(String json) {
        try {
            return sGson.fromJson(json, new TypeToken<ArrayList<T>>(){}.getType());
        } catch (Exception e) {
            Log.e(TAG, "GsonManager:fromJson " + e);
            return null;
        }
    }
}
