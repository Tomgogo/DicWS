package com.pardus.kdictionary.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Tom on 2015/5/11.
 */
public class IETable {
    private static final String TAG = "IETable";
    public static final String TABLE_IE_LIB = "ielib";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEYWORD = "keyword";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String DATA_CREATE = "CREATE TABLE " +
            TABLE_IE_LIB
            + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_KEYWORD + " text not null,"
            + COLUMN_VALUE + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATA_CREATE);
//        loadDictionary(database);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_IE_LIB);
        onCreate(database);
    }

}
