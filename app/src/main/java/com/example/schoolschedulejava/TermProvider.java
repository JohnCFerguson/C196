package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class TermProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.termprovider";
    private static final String TERMS_PATH = "terms";
    public static final Uri TERMS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + TERMS_PATH);


    //Constant that identifies requested operation
    private static final int TERM = 1;
    private static final int TERM_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TERMS_PATH, TERM);
        uriMatcher.addURI(AUTHORITY, TERMS_PATH + "/#", TERM_ID);
    }

    private SQLiteDatabase db;
    
    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        
        db = helper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri,  String[] projection,  String selection, String[] selectionArgs, String sortOrder) {
        return db.query(DBOpenHelper.TABLE_TERMS, DBOpenHelper.TERMS_COLUMNS, selection, null, null,
                null, DBOpenHelper.TERM_ID + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DBOpenHelper.TABLE_TERMS, null, values);
        return Uri.parse(TERMS_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(DBOpenHelper.TABLE_TERMS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(DBOpenHelper.TABLE_TERMS, values, selection, selectionArgs);
    }
}
