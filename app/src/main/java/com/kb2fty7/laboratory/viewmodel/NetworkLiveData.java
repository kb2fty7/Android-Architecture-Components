package com.kb2fty7.laboratory.viewmodel;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by KB2FTY7 on 8/5/2017.
 */

public class NetworkLiveData extends LiveData<String> {
    private Context context;
    private BroadcastReceiver broadcastReceiver;

    public NetworkLiveData(Context context) {
        this.context = context;
    }

    private void prepareReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                String name = wifiInfo.getSSID();
                if (name.isEmpty()) {
                    setValue(null);
                } else {
                    setValue(name);
                }
            }
        };
        context.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onActive() {
        super.onActive();
        prepareReceiver(context);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }
}
