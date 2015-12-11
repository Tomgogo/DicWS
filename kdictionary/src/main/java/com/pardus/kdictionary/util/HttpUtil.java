package com.pardus.kdictionary.util;

import android.os.Build;
import android.util.Log;


import com.pardus.kdictionary.AppInfo;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tom on 2015/5/13.
 */
public class HttpUtil {

    private static Header[] headers = new BasicHeader[11];

    static {
        headers[0] = new BasicHeader("Appkey", "");
        headers[1] = new BasicHeader("Udid", "");
        headers[2] = new BasicHeader("Os", "");
        headers[3] = new BasicHeader("Osversion", "");
        headers[4] = new BasicHeader("Appversion", "");
        headers[5] = new BasicHeader("Sourceid", "");
        headers[6] = new BasicHeader("Ver", "");
        headers[7] = new BasicHeader("Userid", "");
        headers[8] = new BasicHeader("Usersession", "");
        headers[9] = new BasicHeader("Unique", "");
        headers[10] = new BasicHeader("Cookie", "");

    }

    public static String sendDataByHttpClientPost(String path, String name, String password)
            throws Exception {

        //1. ��ȡ��һ���������ʵ��
        HttpClient client = new DefaultHttpClient();
        //2. ׼��Ҫ����� �������
        HttpPost httppost = new HttpPost(path);
        // ��ֵ��
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        parameters.add(new BasicNameValuePair("name", name));
        parameters.add(new BasicNameValuePair("password", password));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");


        //3.����post��������ʵ��
        httppost.setEntity(entity);

        //4. ������ݸ������
        HttpResponse ressponse = client.execute(httppost);
        int code = ressponse.getStatusLine().getStatusCode();
        if (code == 200) {
            InputStream is = ressponse.getEntity().getContent();
            byte[] result = StreamTool.getBytes(is);
            return new String(result);
        } else {
            throw new IllegalStateException("������״̬�쳣");
        }
    }

    public static void testLogin(String userName, String password) {
        Log.e("liang", userName + " " + password);
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://mapi.qianggongzhang.com/?method=app.worker.blogin");
        List<NameValuePair> paramters = new ArrayList<>();

        paramters.add(new BasicNameValuePair("plat", "2"));
        paramters.add(new BasicNameValuePair("os", "Android" + Build.VERSION.SDK_INT));
        paramters.add(new BasicNameValuePair("channel", VersionUtil.getChannel()));
        paramters.add(new BasicNameValuePair("ctype", "2"));
        paramters.add(new BasicNameValuePair("cspec", ""));
        paramters.add(new BasicNameValuePair("cver", String.valueOf(AppInfo.getVersionCode())));
        paramters.add(new BasicNameValuePair("token", getAppToken(paramters)));
        paramters.add(new BasicNameValuePair("app_key", AppInfo.getAppKey()));
        paramters.add(new BasicNameValuePair("devicetoken", CommonUtils.getIMEI(AppInfo.getApp())));
        paramters.add(new BasicNameValuePair("username", userName));
        paramters.add(new BasicNameValuePair("password", password));
//        getAppToken(paramters);
        try {
            HttpEntity entity = new UrlEncodedFormEntity(paramters);
            post.setEntity(entity);
            Log.e("liang", EntityUtils.toString(entity, "utf-8"));
            HttpResponse response = client.execute(post);
            Log.e("liang", "response code = " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                InputStream inputStream = response.getEntity().getContent();
//                new String(StreamTool.getBytes(inputStream), "utf-8");
//                EntityUtils.toString(response.getEntity(),"utf-8");
                Log.e("liang", "response===" + EntityUtils.toString(response.getEntity(), "utf-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("liang", "response exception = " + e.getLocalizedMessage());
        }
    }

    private static String getAppToken(List<NameValuePair> paramters) {
        ArrayList<String> keys = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (NameValuePair nvp : paramters) {
            keys.add(nvp.getName());
//            builder.append()
        }
        Collections.sort(keys);
        for (String key : keys) {
            builder.append(key);
        }
        Log.e("liang", "token key==" + builder.toString());
        builder.append(AppInfo.getAppSecret());
        return MD5Util.toMD5(builder.toString()).toUpperCase();
    }

    /*
    versionCode=10000&channel_id=1000&phone_app_id=25

     */
    public static String getUpagradeInfo(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> paramters = new ArrayList<>();

//        paramters.add(new BasicNameValuePair("mac_addr", CommonUtils.getMacAddress(AppInfo.getApp
//                ())));
//        paramters.add(new BasicNameValuePair("os_version", "Android" + Build.VERSION.SDK_INT));
//        paramters.add(new BasicNameValuePair("versionName", AppInfo.getVersionCode() + ""));
//        paramters.add(new BasicNameValuePair("uuid", CommonUtils.getIMEI(AppInfo.getApp())));
//        paramters.add(new BasicNameValuePair("src_md5", resMd5));

//        paramters.add(new BasicNameValuePair("versionCode", 10000+""));
//        paramters.add(new BasicNameValuePair("channel_id", 1000+ ""));
//        paramters.add(new BasicNameValuePair("phone_app_id", 25+""));
        for (NameValuePair bnp : paramters) {
            bnp.getName();
        }

        Log.e("liang", "request------");
//        HttpParams params = new HttpParams
        try {
            HttpEntity entity = new UrlEncodedFormEntity(paramters, "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
//                InputStream content = response.getEntity().getContent();
                Log.e("liang", "result==" + result);
                return result;

            }

        } catch (UnsupportedEncodingException e) {
            Log.e("liang", "UnsupportedEncodingException------" + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("liang", "ClientProtocolException------" + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("liang", "IOException------" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "";
    }

//    public static Object post(RequestVo vo) {
//        DefaultHttpClient client = new DefaultHttpClient();
//        String url = vo.context.getString(R.string.app_host).concat(vo.context.getString(vo
// .requestUrl));
//        Logger.d(TAG, "Post " + url);
//        HttpPost post = new HttpPost(url);
//        post.setHeaders(headers);
//        Object obj = null;
//        try {
//            if (vo.requestDataMap != null) {
//                HashMap<String, String> map = vo.requestDataMap;
//                ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
//                for (Map.Entry<String, String> entry : map.entrySet()) {
//                    BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
// .getValue());
//                    pairList.add(pair);
//                }
//                HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
//                post.setEntity(entity);
//            }
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                setCookie(response);
//                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
//                try {
//                    if (invilidateLogin(result)) {
//                        return Status.Login;
//                    }
//                    obj = vo.jsonParser.parseJSON(result);
//                } catch (JSONException e) {
//                    Logger.e(TAG, e.getLocalizedMessage(), e);
//                }
//                return obj;
//            }
//        } catch (ClientProtocolException e) {
//            Logger.e(TAG, e.getLocalizedMessage(), e);
//        } catch (IOException e) {
//            Logger.e(TAG, e.getLocalizedMessage(), e);
//        }
//        return null;
//    }
}
