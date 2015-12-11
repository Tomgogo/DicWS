package com.pardus.kdictionary;

import android.app.ActionBar;
import android.app.Application;

import com.pardus.kdictionary.AppInfo;

/**
 * Created by Tom on 2015/5/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInfo.setApp(this);
    }
}
