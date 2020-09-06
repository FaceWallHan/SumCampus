package com.campus.sport;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/18.
 */

public class AppClient extends Application {
    private static SharedPreferences preferences;
    private static RequestQueue requestQueue;

    private List<String> Paths=new ArrayList<>();
    public List<String> getPaths() {
        return Paths;

    }

    public String getLc(){
        return preferences.getString("lc","0.0");
    }
    public String getSj(){
        return preferences.getString("sj","0");
    }

    public void setSj(String sj){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("sj",sj);
        editor.apply();
    }

    public String getCs(){
        return preferences.getString("cs","0");
    }

    public void setCs(String cs){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("cs",cs);
        editor.apply();
    }

    public void setLc(String lc){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("lc",lc);
        editor.apply();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue= Volley.newRequestQueue(this);
    }
    public static void setRequestQueue(JsonObjectRequest jsonObjectRequest){
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static void setParam(String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = preferences.edit();
        if ("String".equals(type)) {
            editor.putString(key,  object.toString());
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.apply();
    }
    public  static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return preferences.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return preferences.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return preferences.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return preferences.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return preferences.getLong(key, (Long) defaultObject);
        }

        return null;
    }
}
