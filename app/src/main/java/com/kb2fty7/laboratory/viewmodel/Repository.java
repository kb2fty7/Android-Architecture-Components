package com.kb2fty7.laboratory.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KB2FTY7 on 8/5/2017.
 */

public class Repository implements IRepository {
    private static final int DEFAULT_RADIUS = 300;
    private static final String DEFAULT_POINT = "0";
    private static final String SETTINGS = "settings_sp";
    private static final String RADIUS = "radius_sp";
    private static final String POINT_LONGITUDE = "point_sp_lng";
    private static final String POINT_LATITUDE = "point_sp_lat";
    private static final String NETWORK_NAME = "network_sp";
    private static Repository mInstance;
    private SharedPreferences sharedPref;

    private Repository(Context context) {
        sharedPref = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    public static Repository getInstance(Context context) {
        if (mInstance == null)
            mInstance = new Repository(context);
        return mInstance;
    }

    @Override
    public int getRadius() {
        return sharedPref.getInt(RADIUS, DEFAULT_RADIUS);
    }

    @Override
    public LatLng getPoint() {

        double longitude = Double.parseDouble(sharedPref.getString(POINT_LONGITUDE, DEFAULT_POINT));
        double latitude = Double.parseDouble(sharedPref.getString(POINT_LATITUDE, DEFAULT_POINT));
        return new LatLng(latitude, longitude);
    }

    @Override
    public String getNetworkName() {
        return sharedPref.getString(NETWORK_NAME, "");
    }

    @Override
    public void saveRadius(int radius) {
        sharedPref.edit().putInt(RADIUS, radius).apply();
    }

    @Override
    public void savePoint(LatLng point) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(POINT_LATITUDE, Double.toString(point.latitude));
        editor.putString(POINT_LONGITUDE, Double.toString(point.longitude));
        editor.apply();
    }

    @Override
    public void saveNetworkName(String networkName) {
        sharedPref.edit().putString(NETWORK_NAME, networkName).apply();
    }
}
