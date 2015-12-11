package com.pardus.kdictionary.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Tom on 2015/5/13.
 */
public class CommonUtils {
    private  static  int startNum = 1;
    public static void hideSoftInput(Activity context) {
        InputMethodManager imm = ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE));
        View focus = context.getCurrentFocus();
        if (focus != null) {
            imm.hideSoftInputFromWindow(focus.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static void showSoftInput(Activity context, View view) {
        view.setFocusable(true);
        view.requestFocus();
        InputMethodManager imm = ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.showSoftInput(view, 0);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager
                .getActiveNetworkInfo();
        return mNetworkInfo != null ? mNetworkInfo.isAvailable() : false;
    }

    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (!TextUtils.isEmpty(manager.getDeviceId())) {
            return manager.getDeviceId();
        } else {
            final String tmDevice, tmSerial, tmPhone, androidId;

            tmDevice = "" + manager.getDeviceId();

            tmSerial = "" + manager.getSimSerialNumber();

            androidId = ""
                    + android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

            String uniqueId = deviceUuid.toString();

            // LogUtils.e("android deviceId is null, uuid=" + uniqueId);
            return uniqueId;
        }
    }

    public static ArrayList<String> formatResult(String result){
        ArrayList<String> list = new ArrayList<>();
        int startNum = 1;
        if(!TextUtils.isEmpty(result)){
           if(result.startsWith("\"")){
               result = result.substring(1,result.length()-1);
               Log.e("liang", "result str ==" + result);

//               subResult(result,startNum);
           }
            String temp = "";
               while (!TextUtils.isEmpty(result)&&(result.startsWith(startNum+" ")||result.startsWith(startNum+"."))){
                   temp = result.substring(result.indexOf(startNum + ""),result.indexOf(++startNum+" ")>0?result.indexOf(startNum+" "):result.indexOf(startNum+".")>0?result.indexOf(startNum+"."):result.length());
                   list.add(temp);
//                   Log.e("liang","add str=="+result.substring(result.indexOf(startNum+""),result.indexOf(++startNum)>0?result.indexOf(startNum):result.length()));
//                   Log.e("liang","ss str=="+(result.indexOf(++startNum+"")>0?result.indexOf(startNum+"")+"gaga":result.length()+"haha"));

//                   Log.e("liang","res2==="+(result.indexOf(startNum)>0? result.substring(result.indexOf(startNum),result.length()):""));
//                   result = result.indexOf(startNum+"")>0? result.substring(result.indexOf(startNum+""),result.length()):"";
                    result = result.substring(result.indexOf(temp)+temp.length(),result.length());
                   Log.e("liang",startNum+"");
               }
            Log.e("liang","list size==="+list.size());
        }

        return list;
    }

    private static String subResult(String result,int startNum) {
        if(result.startsWith(startNum+"")){
            return result.substring(result.indexOf(startNum+++""),result.indexOf(startNum)>0?result.indexOf(startNum):result.length());
        }
        return result;
    }
    public static ShapeDrawable createRoundCornerShapeDrawable(float radius, float borderLength, int borderColor) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = radius + borderLength;
            innerRadii[i] = radius;
        }

        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderLength, borderLength,
                borderLength, borderLength), innerRadii));
        sd.getPaint().setColor(borderColor);

        return sd;
    }
//    File path = Environment.getExternalStorageDirectory();
//    StatFs stat = new StatFs(path.getPath());
//    long blockSize;
//    long totalBlocks;
//    long availableBlocks;
//
//    //判断手机Android版本
//    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
//        blockSize= stat.getBlockSizeLong();
//        totalBlocks = stat.getBlockCountLong();
//        availableBlocks = stat.getAvailableBlocksLong();
//    }else{
//        blockSize= stat.getBlockSize();
//        totalBlocks = stat.getBlockCount();
//        availableBlocks = stat.getAvailableBlocks();
//    }
//
//    String sd_avail = formatSize(availableBlocks * blockSize);
//    TextView tv = (TextView) findViewById(R.id.tv_sd);
//    tv.setText("SD卡剩余容量 " + sd_avail);
//}
//
//    public String formatSize(long size){
//        return Formatter.formatFileSize(this, size);
//    }



}
