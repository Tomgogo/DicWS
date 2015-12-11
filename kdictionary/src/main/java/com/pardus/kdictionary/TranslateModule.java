package com.pardus.kdictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


public class TranslateModule {
	private AsyncTask<Void, Void, String> execute;

	private static class TranslateTask extends AsyncTask<Void, Void, String> {
		TranslateSuccessListener mOnSuccessListener;
		private String mText;

		public TranslateTask(Context context, String text, TranslateSuccessListener onSuccessListener) {
			super();
			mOnSuccessListener = onSuccessListener;
			mText = text;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (!TextUtils.isEmpty(result)) {
				if (mOnSuccessListener != null) {
					mOnSuccessListener.onTranslateSuccess(result);
				}
			} else {
				if (mOnSuccessListener != null) {
					mOnSuccessListener.onTranslateFailed();
				}
			}
		}

		private String getContent(String url, String text) {
			final HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000*10);
			DefaultHttpClient client = new DefaultHttpClient(httpParams);
			HttpPost getData = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("q", text));
			try {
				getData.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));

				getData.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				getData.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
				HttpResponse execute;
				String data = null;
				execute = client.execute(getData);
				HttpEntity entity = execute.getEntity();
				data = EntityUtils.toString(entity, "utf-8");
				return data;

			} catch (ClientProtocolException e) {
//				LogUtil.e(e);
			} catch (IOException e) {
//				LogUtil.e(e);
			} catch (SecurityException e) {
//				LogUtil.e(e);
			}
			return null;

		}

		@Override
		protected String doInBackground(Void... params) {
			// ��ȡϵͳ����
//			Locale locale = mContext.getResources().getConfiguration().locale;
//			String language = locale.getLanguage() + "_" + locale.getCountry();
			String language = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
			String content = getContent(
					String.format("http://translate.google.com/translate_a/single?client=t&sl=auto&tl=%s&dt=t&ie=UTF-8&oe=UTF-8", language),
					mText);
			Log.e("liang","content=="+content);
			JSONArray array = null;
			StringBuilder sb = new StringBuilder();
			try {
				array = new JSONArray(content);
				JSONArray translateArray = array.getJSONArray(0);
				JSONArray translateItem;
				for (int i = 0; i < translateArray.length(); i++) {
					translateItem = translateArray.getJSONArray(i);
					if (translateItem.length() >= 2) {
						sb.append(translateItem.getString(0));
					}
				}
			} catch (Exception e) {
//				LogUtil.e(e);
			}
//			LogUtil.d("" + array);
			return sb.toString();
		}

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void startMyTask(AsyncTask<Void, Void, String> asyncTask) {
		if (asyncTask.getStatus() != Status.PENDING) {
			return;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}
	}

	public interface TranslateSuccessListener {

		public void onTranslateSuccess(String result);

		public void onTranslateFailed();

	}

	public void translate(final Context context, final String text, final TranslateSuccessListener onSuccessListener) {
		if (execute != null) {
			execute.cancel(true);
		}
		execute = new TranslateTask(context, text, onSuccessListener);
		startMyTask(execute);
	}
}
