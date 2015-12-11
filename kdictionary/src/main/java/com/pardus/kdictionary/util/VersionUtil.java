package com.pardus.kdictionary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;


import com.pardus.kdictionary.AppInfo;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public enum VersionUtil {
    INSTANCE;

    private HashMap<String,String> map=new HashMap<>();

    private VersionUtil() {

    }

    public static String getChannel (){
        return getContent(AppInfo.getApp(),"Channel");
    }

    public static String getTestVersion (){
        return getContent(AppInfo.getApp(),"DebugVersion");
    }

    private static String getContent(Context context, String channelKey) {
        String result = INSTANCE.map.get(channelKey);
        if (!TextUtils.isEmpty(result)){
            return result;
        }

        //��apk���л�ȡ
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String sourceDir = applicationInfo.sourceDir;
        //ע�����Ĭ�Ϸ���meta-inf/� ������Ҫ��ƴ��һ��
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "DEV";
        if (split != null && split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }
        INSTANCE.map.put(channelKey,channel);
        return channel;
    }
}
