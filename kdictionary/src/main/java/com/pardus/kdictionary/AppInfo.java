package com.pardus.kdictionary;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by Tom on 2015/5/15.
 */
public class AppInfo {
    private static Application _app;
    private static String appSecret;

    private static String appKey;
    private static int version;
    private static String versionName;
    public static void setApp(Application app){
        _app = app;
    }

    public static Application getApp(){
        return  _app;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = _app.getPackageManager().getPackageInfo(_app.getPackageName(),
                    0).versionCode;
        } catch (Exception e) {

        }
        return versionCode;
    }

    public static String getAppSecret() {
        if (TextUtils.isEmpty(appSecret)) {
            readConfig();
        }
        return appSecret;
    }

    public static String getAppKey() {
        if (TextUtils.isEmpty(appKey)) {
            readConfig();
        }
        return appKey;
    }
    private static void readConfig() {
        try {
            Bundle info = _app.getPackageManager().getApplicationInfo(_app.getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            appSecret = info.getString("app_secret");
            appKey = info.getString("app_key");
            version = _app.getPackageManager().getPackageInfo(_app.getPackageName(),
                    0).versionCode;
            versionName = _app.getPackageManager().getPackageInfo(_app.getPackageName(),
                    0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
