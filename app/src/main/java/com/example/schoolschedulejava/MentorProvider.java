package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MentorProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.mentorprovider";
    private static final String MENTORS_PATH = "mentors";
    public static final Uri MENTORS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MENTORS_PATH);

    //Constant that identifies requested operation
    private static final int MENTOR = 1;
    private static final int MENTOR_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, MENTORS_PATH, MENTOR);
        uriMatcher.addURI(AUTHORITY, MENTORS_PATH + "/#", MENTOR_ID);
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
        return db.query(DBOpenHelper.TABLE_MENTORS, DBOpenHelper.MENTORS_COLUMNS, selection, selectionArgs, null,
                null, DBOpenHelper.MENTOR_NAME + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DBOpenHelper.TABLE_MENTORS, null, values);
        return Uri.parse(MENTORS_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(DBOpenHelper.TABLE_MENTORS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(DBOpenHelper.TABLE_MENTORS, values, selection, selectionArgs);
    }
}
