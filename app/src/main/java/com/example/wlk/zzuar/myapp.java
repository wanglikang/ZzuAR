package com.example.wlk.zzuar;

import android.app.Application;
import android.content.Context;


public class myapp extends Application {
    private static  myapp app;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static myapp getApp() {
        return app;
    }
}
