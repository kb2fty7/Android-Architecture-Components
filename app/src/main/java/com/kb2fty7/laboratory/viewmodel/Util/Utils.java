package com.kb2fty7.laboratory.viewmodel.Util;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by KB2FTY7 on 8/5/2017.
 */

public class Utils {
    public static boolean isGpsEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
