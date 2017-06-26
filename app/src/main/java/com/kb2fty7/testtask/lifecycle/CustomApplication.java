package com.kb2fty7.testtask.lifecycle;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Intent;

/**
 * Created by KB2FTY7 on 6/26/2017.
 */

public class CustomApplication extends Application {
    private SomeObserver processObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        processObserver = new SomeObserver(ProcessLifecycleOwner.get().getLifecycle(), SomeObserver.Owner.PROCESS);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

}
