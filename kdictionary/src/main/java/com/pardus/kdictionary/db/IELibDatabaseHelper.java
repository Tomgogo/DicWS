package com.pardus.kdictionary.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.pardus.kdictionary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tom on 2015/5/11.
 */
public class IELibDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG ="IELibDatabaseHelper";
    private static final String DATABASE_NAME = "iedictionary.db";
    private static final int DATABASE_VERSION = 1;
    private  Context mContext = null;
    private static IELibDatabaseHelper sInstance;
    private  SQLiteDatabase database;
    public IELibDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    public static synchronized IELibDatabaseHelper  getInstance(Context context){
        if(sInstance ==null){
            sInstance = new IELibDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public   synchronized SQLiteDatabase getDatabase(){
        if(database ==null){
            database = getWritableDatabase();
        }
        return  database;
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.e("liang","oncreate");
        IETable.onCreate(database);
        loadDictionary(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        IETable.onUpgrade(database, oldVersion, newVersion);
    }
    private  void loadDictionary(final SQLiteDatabase database) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.e("liang", "insert db");
                    loadWords(database);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    public String getEnglishDefinitions(String sIndonesian){
        SQLiteDatabase database = getDatabase();
        String sDefinitions = "";
        Cursor cursor = database.query(IETable.TABLE_IE_LIB, null, IETable.COLUMN_KEYWORD + "=?",
                new String[]{sIndonesian}, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
             sDefinitions = cursor.getString(cursor.getColumnIndex(IETable.COLUMN_VALUE));
            }
        }
        return  sDefinitions;
    }

    private  void loadWords(SQLiteDatabase database) throws IOException {
        Log.d(TAG, "Loading words...");
        InputStream input  = null;
        try {
            input = mContext.getResources().getAssets().open("id_en.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String temp = null;
            long startTime = System.currentTimeMillis();
            Log.e("liang", "start time===" + startTime + "");
            database.beginTransaction();
            while ((temp = br.readLine()) != null) {

//                Log.e("liang",temp);
//                dict.put(temp.substring(0, temp.indexOf(",")), temp.substring(temp.indexOf(",") +
//                        1, temp.length()));
//                map.put(temp.substring(0, temp.indexOf(",")), temp.substring(temp.indexOf(",") +
//                        1, temp.length()));
                addWord(database,temp.substring(0, temp.indexOf(",")),temp.substring(temp.indexOf
                        (",") +
                        1, temp.length()));

            }

            database.setTransactionSuccessful();
//            Log.e("liang","end time==="+System.currentTimeMillis()+"");
//            LocalBroadcastManager.getInstance(this)
            Log.e("liang","duration==="+(System.currentTimeMillis()-startTime)+"");
//            dict.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("liang","exception=="+e.getMessage());
        }finally {
            database.endTransaction();
            if(input !=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        final Resources resources = mContext.getResources();
//
//        InputStream inputStream = resources.openRawResource(R.raw.definitions);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        try {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] strings = TextUtils.split(line, "-");
//                if (strings.length < 2) continue;
//                long id = addWord(database,strings[0].trim(), strings[1].trim());
//                if (id < 0) {
//                    Log.e(TAG, "unable to add word: " + strings[0].trim());
//                }
//            }
//        } finally {
//            reader.close();
//        }
        Log.d("liang", "DONE loading words.");
    }

    /**
     * Add a word to the dictionary.
     *
     * @return rowId or -1 if failed
     */
    public long addWord(SQLiteDatabase database,String word, String definition) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(IETable.COLUMN_KEYWORD, word);
        initialValues.put(IETable.COLUMN_VALUE, definition);

        return database.insert(IETable.TABLE_IE_LIB, null, initialValues);
    }

}
