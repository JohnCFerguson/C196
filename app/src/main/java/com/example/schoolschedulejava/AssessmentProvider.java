package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AssessmentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.assessmentprovider";
    private static final String ASSESSMENTS_PATH = DBOpenHelper.TABLE_ASSESSMENTS;
    public static final Uri ASSESSMENTS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENTS_PATH);

    //Constant that identifies requested operation
    private static final int ASSESSMENT = 1;
    private static final int ASSESSMENT_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, ASSESSMENTS_PATH, ASSESSMENT);
        uriMatcher.addURI(AUTHORITY, ASSESSMENTS_PATH + "/#", ASSESSMENT_ID);
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
        return db.query(DBOpenHelper.TABLE_ASSESSMENTS, DBOpenHelper.ASSESSMENTS_COLUMNS, selection, selectionArgs, null,
                null, DBOpenHelper.ASSESSMENT_TYPE + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DBOpenHelper.TABLE_ASSESSMENTS, null, values);
        return Uri.parse(ASSESSMENTS_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(DBOpenHelper.TABLE_ASSESSMENTS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(DBOpenHelper.TABLE_ASSESSMENTS, values, selection, selectionArgs);
    }
}
