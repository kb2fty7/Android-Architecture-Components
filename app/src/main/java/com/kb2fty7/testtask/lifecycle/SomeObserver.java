package com.kb2fty7.testtask.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

/**
 * Created by KB2FTY7 on 6/25/2017.
 */

public class SomeObserver implements LifecycleObserver {
    private Owner owner;

    public SomeObserver(Lifecycle lifecycle, Owner owner) {
        this.owner = owner;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Log.d("Observer", owner + ": onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        Log.d("Observer", owner + ": onStop");
    }

    enum Owner {
        ACTIVITY, FRAGMENT, PROCESS, SERVICE
    }
}
