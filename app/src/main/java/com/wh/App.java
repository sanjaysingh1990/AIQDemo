package com.wh;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static Context context;
    private static boolean inForeground;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

    public static void setInForeground(boolean inForeground) {
        App.inForeground = inForeground;
    }

    public static boolean isInForeground() {
        return inForeground;
    }
}
