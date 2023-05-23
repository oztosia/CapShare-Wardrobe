package com.oztosia.capsharewardrobe;

import android.app.Application;
import android.content.Context;

public class CapShareWardrobe extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        CapShareWardrobe.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CapShareWardrobe.context;
    }
}