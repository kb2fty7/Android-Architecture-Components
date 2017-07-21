package com.kb2fty7.laboratory.livedata;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

public class MainActivity extends LifecycleActivity implements Observer<String> {
    private MediatorLiveData<String> mediatorLiveData;
    private TextView networkName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkName = (TextView) findViewById(R.id.network_name);
        mediatorLiveData = new MediatorLiveData<>();
        init();
    }


    private void init() {
        final LiveData<String> network = NetworkLiveData.getInstance(this);
        final LiveData<String> mobileNetwork = MobileNetworkLiveData.getInstance(this);
        Observer<String> networkObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!TextUtils.isEmpty(s))
                    mediatorLiveData.setValue(s);
                else
                    mediatorLiveData.setValue(mobileNetwork.getValue());
            }
        };
        Observer<String> mobileNetworkObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (TextUtils.isEmpty(network.getValue())) {
                    mediatorLiveData.setValue(s);
                }
            }
        };
        mediatorLiveData.addSource(network, networkObserver);
        mediatorLiveData.addSource(mobileNetwork, mobileNetworkObserver);
        mediatorLiveData.observe(this, this);
    }


    @Override
    public void onChanged(@Nullable String s) {
        networkName.setText(s);
    }
}
