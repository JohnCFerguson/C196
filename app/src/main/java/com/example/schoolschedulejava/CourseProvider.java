package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CourseProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.courseprovider";
    private static final String COURSES_PATH = "courses";
    public static final Uri COURSES_URI =
            Uri.parse("content://" + AUTHORITY + "/" + COURSES_PATH);

    //Constant that identifies requested operation
    private static final int COURSE = 1;
    private static final int COURSE_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String COURSE_ITEM_TYPE = "course";

    static {
        uriMatcher.addURI(AUTHORITY, COURSES_PATH, COURSE);
        uriMatcher.addURI(AUTHORITY, COURSES_PATH + "/#", COURSE_ID);
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
        return db.query(DBOpenHelper.TABLE_COURSES, DBOpenHelper.COURSES_COLUMNS, selection, selectionArgs, null,
                null, DBOpenHelper.COURSE_START + " ASC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DBOpenHelper.TABLE_COURSES, null, values);
        return Uri.parse(COURSES_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(DBOpenHelper.TABLE_COURSES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(DBOpenHelper.TABLE_COURSES, values, selection, selectionArgs);
    }
}
