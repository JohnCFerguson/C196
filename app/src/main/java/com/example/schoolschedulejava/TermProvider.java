package com.example.schoolschedulejava;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class TermProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolschedulejava.termprovider";
    private static final String TERMS_PATH = "terms";
    private static final String COURSES_PATH = "courses";
    private static final String MENTORS_PATH = "mentors";
    private static final String ASSESSMENTS_PATH = "assessments";
    public static final Uri TERMS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + TERMS_PATH);
    public static final Uri COURSES_URI =
            Uri.parse("content://" + AUTHORITY + "/" + COURSES_PATH);
    public static final Uri MENTORS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MENTORS_PATH);
    public static final Uri ASSESSMENTS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENTS_PATH);


    //Constant that identifies requested operation
    private static final int TERM = 1;
    private static final int TERM_ID = 2;
    private static final int COURSE = 1;
    private static final int COURSE_ID = 2;
    private static final int MENTOR = 1;
    private static final int MENTOR_ID = 2;
    private static final int ASSESSMENT = 1;
    private static final int ASSESSMENT_ID = 2;


    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TERMS_PATH, TERM);
        uriMatcher.addURI(AUTHORITY, TERMS_PATH + "/#", TERM);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @androidx.annotation.Nullable
    @Override
    public Cursor query(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String[] projection, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs, @androidx.annotation.Nullable String sortOrder) {
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    public String getType(@androidx.annotation.NonNull Uri uri) {
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    public Uri insert(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        return 0;
    }
}
