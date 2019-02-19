package com.example.schoolschedulejava;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = getContentResolver().query(TermProvider.TERMS_URI,
                DBOpenHelper.TERMS_COLUMNS, null, null, null,
                null);

        String[] from = {DBOpenHelper.TERM_TITLE};
        int[] to = {android.R.id.text1};
        CursorAdapter cA = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                cursor, from, to, 0);
        ListView list = findViewById(android.R.id.list);
    }

    private void insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        Uri termUri = getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + termUri.getLastPathSegment());
    }

    private void insertAssessment(String assessmentName, String assessmentType) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, assessmentType);
        Uri assessmentUri = getContentResolver().insert(AssessmentProvider.ASSESSMENTS_URI, values);
        Log.d("MainActivity", "Inserted Assessment " + assessmentUri.getLastPathSegment());
    }

    private void insertMentor(String mentorName, String mentorEmail, String mentorPhone) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.MENTOR_PHONE, mentorPhone);
        Uri mentorUri = getContentResolver().insert(MentorProvider.MENTORS_URI, values);
        Log.d("MainActivity", "Inserted Term " + mentorUri.getLastPathSegment());
    }

    private void insertCourse(int termId, int mentorId, String courseTitle, String courseStart,
                              String courseEnd, String courseStatus, String courseAssessments,
                              String courseNotes ) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERMID, termId);
        values.put(DBOpenHelper.MENTORID, mentorId);
        values.put(DBOpenHelper.COURSE_TITLE, courseTitle);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_STATUS, courseStatus);
        values.put(DBOpenHelper.COURSE_ASSESSMENTS, courseAssessments);
        values.put(DBOpenHelper.COURSE_NOTES, courseNotes);
        Uri termUri = getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + termUri.getLastPathSegment());
    }

}
