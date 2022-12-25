package com.example.mypersonalwardrobe;

import android.app.Application;
import android.content.Context;

public class MyPersonalWardrobe extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyPersonalWardrobe.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyPersonalWardrobe.context;
    }
}