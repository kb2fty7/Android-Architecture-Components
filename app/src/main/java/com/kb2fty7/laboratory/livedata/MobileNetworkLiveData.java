package com.kb2fty7.laboratory.livedata;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by KB2FTY7 on 7/20/2017.
 */

public class MobileNetworkLiveData extends LiveData<String> {
    private static MobileNetworkLiveData instance;
    private Context context;

    private MobileNetworkLiveData(Context context) {
        this.context = context;
    }

    private MobileNetworkLiveData() {

    }

    @Override
    protected void onActive() {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = telephonyManager.getNetworkOperatorName();
        setValue(networkOperator);
    }

    public static MobileNetworkLiveData getInstance(Context context) {
        if (instance == null) {
            instance = new MobileNetworkLiveData(context);
        }
        return instance;
    }

}
