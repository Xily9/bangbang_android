package com.autowheel.bangbang;

import android.app.Application;

import com.didichuxing.doraemonkit.DoraemonKit;

/**
 * Created by Xily on 2020/3/5.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DoraemonKit.install(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
