package com.example.seonoh2.smarttoliet01.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by 선오 on 2016-08-19.
 */
public class MyApplication extends Application{

    public  static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }
}
