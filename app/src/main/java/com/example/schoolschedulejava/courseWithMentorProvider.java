package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class courseWithMentorProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.courseswithmentorsprovider";
    private static final String COURSES_PATH = "courses";
    private static final String MENTORS_PATH = "mentors";
    public static final Uri COURSESWITHMENTORS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + COURSES_PATH + "with" + MENTORS_PATH);

    //Constant that identifies requested operation
    private static final int COURSE = 1;
    private static final int COURSE_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, COURSES_PATH, COURSE);
        uriMatcher.addURI(AUTHORITY, COURSES_PATH + "/#", COURSE_ID);
    }

    private SQLiteDatabase db;

    private static final String COURSE_WITH_MENTOR_QUERY = "";

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());

        db = helper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
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
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
