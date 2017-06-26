package com.kb2fty7.testtask.lifecycle;

import android.arch.lifecycle.LifecycleActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends LifecycleActivity {

    SomeObserver someObserver = new SomeObserver(getLifecycle(), SomeObserver.Owner.ACTIVITY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Owner", "onCreate");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Owner", "onStop");
    }
}
