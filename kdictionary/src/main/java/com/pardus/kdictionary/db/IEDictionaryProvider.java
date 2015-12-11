package com.pardus.kdictionary.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Tom on 2015/5/11.
 *
 */
public class IEDictionaryProvider extends ContentProvider {
    private IELibDatabaseHelper helper;
    private static final int TRASLATE = 1;
    public static String AUTHORITY = "com.leju.kdictionary.db";
    private static final String BASEPATH = "ielib";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASEPATH);
    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE;
    private static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sUriMatcher.addURI(AUTHORITY, BASEPATH, TRASLATE);
    }

    @Override
    public boolean onCreate() {
        helper = new IELibDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(IETable.TABLE_IE_LIB);
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case TRASLATE:
                break;
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        String[] available = {IETable.COLUMN_KEYWORD, IETable.COLUMN_VALUE,
                IETable.COLUMN_ID};
        if (projection != null) {
            HashSet<String> requestColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            if (!availableColumns.containsAll(requestColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}