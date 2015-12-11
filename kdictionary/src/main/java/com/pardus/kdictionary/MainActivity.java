package com.pardus.kdictionary;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.pardus.kdictionary.db.IEDictionaryProvider;
import com.pardus.kdictionary.db.IELibDatabaseHelper;
import com.pardus.kdictionary.db.IETable;
import com.pardus.kdictionary.util.CommonUtils;
import com.pardus.kdictionary.util.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {
    private final HashMap<String, String> map = new HashMap<>();
    private EditText srcEdit;
    private Button traslate;
    private TextView resultTv;
    private SQLiteDatabase db;
    private ListView mList;
    private DefinitionListAdapter adapter;
    private ProgressBar progressBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup wm;
        setContentView(R.layout.activity_main);
        srcEdit = (EditText) findViewById(R.id.et_y2e);
        traslate = (Button) findViewById(R.id.bt_go);
        mList = (ListView) findViewById(R.id.list);
        resultTv = (TextView) findViewById(R.id.tv_res);
        db = IELibDatabaseHelper.getInstance(this).getDatabase();
//        Glide
        final TranslateModule translateModule = new TranslateModule();

//        resultTv.setBackgroundR(CommonUtils.createRoundCornerShapeDrawable(5,5,getResources()
// .getColor(R.color.abc_primary_text_disable_only_material_dark)));
//      db=  new IELibDatabaseHelper(this).getReadableDatabase();
//     final Dict_bak dict = new Dict_bak(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                cvs2Data(dict);
//                dict.close();
//            };
//        }).start();
        //获取文件MD5测试
//        try {
//            String fileMd5 = MD5Util.getFileMd5(getResources().getAssets().open("Md5CheckerCn
// .exe"));
//            Log.e("liang","filemd5=="+fileMd5);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        test();
        traslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IEDictionaryProvider provider = new IEDictionaryProvider();
                CommonUtils.hideSoftInput(MainActivity.this);
                String src = srcEdit.getText().toString().trim();
                String englishDefinitions = "";
                resultTv.setVisibility(View.VISIBLE);
//                Cursor cursor = provider.query(IEDictionaryProvider.CONTENT_URI, null, null,
//                        new String[]{src}, null);
//                Cursor cursor = db.query(IETable.TABLE_IE_LIB,null,IETable.COLUMN_VALUE+"=?",
// new String[]{src},null,null,null);
//                if(cursor!=null){
//                    result.setText(cursor.getString(cursor.getColumnIndex(IETable.COLUMN_VALUE)));
//                }
//                mHandler.
//                String englishDefinitions = IELibDatabaseHelper.getInstance(MainActivity.this)
//                        .getEnglishDefinitions(src);
//                if(!TextUtils.isEmpty(englishDefinitions)){
//                    result.setText(englishDefinitions);
//                }else {
//                    result.setText("您搜索的词语暂时不存在，我们会努力为您完善。");
//                }
                if (TextUtils.isEmpty(src)) {
                    return;
                }
                if (CommonUtils.isNetworkConnected(MainActivity.this)) {
                    Log.e("liang", "network available");
//                    progressBar = new ProgressBar(MainActivity.this)
                    translateModule.translate(MainActivity.this, src, new TranslateModule
                            .TranslateSuccessListener() {

                        @Override
                        public void onTranslateSuccess(String result) {
                            Log.e("liang", "translate success");
                            if (!TextUtils.isEmpty(result)) {
                                resultTv.setVisibility(View.VISIBLE);
                                resultTv.setText(result);
                            }
                        }

                        @Override
                        public void onTranslateFailed() {
                            Log.e("liang", "translate failed");
                            Toast.makeText(MainActivity.this, getString(R.string
                                    .not_found_prompt), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {


                    Cursor cursor = getContentResolver().query(IEDictionaryProvider.CONTENT_URI,
                            null,
                            IETable.COLUMN_KEYWORD + "=?", new String[]{src}, null);
//                Log.e("liang", "count===" + cursor.getColumnIndex(IETable.COLUMN_VALUE));
                    if (!cursor.isAfterLast()) {
                        cursor.moveToFirst();
                        resultTv.setVisibility(View.GONE);
                        englishDefinitions = cursor.getString(cursor.getColumnIndex(IETable
                                .COLUMN_VALUE));

                    }
                    HashMap<String, String> map = null;
                    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,
                            String>>();
                    for (String def : CommonUtils.formatResult(englishDefinitions)) {
                        map = new HashMap<String, String>();
                        map.put(src, def);
                        list.add(map);
                    }

                    adapter = new DefinitionListAdapter(MainActivity.this, list);
                    if (!TextUtils.isEmpty(englishDefinitions)) {
//                    resultTv.setText(englishDefinitions);
                    } else {
//                    resultTv.setText("");
                        Toast.makeText(MainActivity.this, getString(R.string.not_found_prompt),
                                Toast.LENGTH_LONG).show();
                    }
                    mList.setAdapter(adapter);
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.testLogin("18500830925", "123456");
            }
        }).start();
    }

    public void cvs2Data(Dict_bak dict) {
        InputStream input = null;
        try {
            input = getResources().getAssets().open("id_en.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                Log.e("liang", temp);
//                dict.put(temp.substring(0, temp.indexOf(",")), temp.substring(temp.indexOf(",") +
//                        1, temp.length()));
                map.put(temp.substring(0, temp.indexOf(",")), temp.substring(temp.indexOf(",") +
                        1, temp.length()));
            }
//            dict.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 0, Menu.NONE, R.string.upgrade);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
            Log.e("liang", "upgrade selected");
            new AsyncTask<String, Integer, String>() {

                @Override
                protected String doInBackground(String... strings) {
                    return HttpUtil.getUpagradeInfo(Config.UPDATE_URL);
                }
            }.execute("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pardus.kdictionary/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pardus.kdictionary/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
