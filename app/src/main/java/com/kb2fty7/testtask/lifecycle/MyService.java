package com.kb2fty7.testtask.lifecycle;

import android.arch.lifecycle.LifecycleService;
import android.content.Intent;
import android.os.Handler;

public class MyService extends LifecycleService {
    SomeObserver someObserver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         someObserver = new SomeObserver(getLifecycle(), SomeObserver.Owner.SERVICE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

}
