package com.kb2fty7.laboratory.viewmodel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KB2FTY7 on 8/5/2017.
 */

public interface IRepository {
    int getRadius();

    LatLng getPoint();

    String getNetworkName();

    void saveRadius(int radius);

    void savePoint(LatLng point);

    void saveNetworkName(String networkName);
}
