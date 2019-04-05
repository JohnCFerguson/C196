package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CourseWithExtrasProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.coursewithextrasprovider";
    private static final String COURSE_WITH_EXTRAS_PATH = "coursewithextras";
    public static final Uri COURSE_WITH_EXTRAS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + COURSE_WITH_EXTRAS_PATH);

    //Constant that identifies requested operation
    private static final int COURSE = 1;
    private static final int COURSE_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, COURSE_WITH_EXTRAS_PATH, COURSE);
        uriMatcher.addURI(AUTHORITY, COURSE_WITH_EXTRAS_PATH + "/#", COURSE_ID);
    }

    private SQLiteDatabase db;

    private static final String COURSE_WITH_EXTRAS_QUERY =
            "SELECT courses." + DBOpenHelper.COURSE_ID + ", courses." + DBOpenHelper.TERMID +
            ", courses." + DBOpenHelper.MENTORID + ", courses." + DBOpenHelper.COURSE_TITLE +
            ", courses." + DBOpenHelper.COURSE_START + ", courses." + DBOpenHelper.COURSE_END +
            ", courses." + DBOpenHelper.COURSE_STATUS + ", courses." + DBOpenHelper.COURSE_NOTES +
            ", mentors." + DBOpenHelper.MENTOR_NAME + ", mentors." + DBOpenHelper.MENTOR_EMAIL +
            ", mentors." + DBOpenHelper.MENTOR_PHONE + ", GROUP_CONCAT(assessments." + DBOpenHelper.ASSESSMENT_NAME +
            ") AS assessments FROM courses LEFT JOIN mentors ON courses." + DBOpenHelper.MENTORID + " = mentors." +
            DBOpenHelper.MENTOR_ID + " LEFT JOIN assessments " + " ON assessments." +
            DBOpenHelper.COURSEID + " = courses." + DBOpenHelper.COURSE_ID + " WHERE ";

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());

        db = helper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d("CourseWithMentorQuery", COURSE_WITH_EXTRAS_QUERY + selection);
        Cursor cursor = db.rawQuery(COURSE_WITH_EXTRAS_QUERY + selection, selectionArgs);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DBOpenHelper.TABLE_COURSES, null, values);
        return Uri.parse(COURSE_WITH_EXTRAS_PATH + "/" + id);
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
