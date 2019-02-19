package com.example.schoolschedulejava;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CursorAdapter cA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //insertTerm("Term 1", "01/01/2019", "06/01/2019");

        String[] from = {DBOpenHelper.TERM_TITLE};
        int[] to = {android.R.id.text1};
        cA = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, from, to, 0);
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
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
                              String courseNotes) {
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,   // Parent activity context
                TermProvider.TERMS_URI,        // Table to query
                null,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cA.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cA.swapCursor(null);
    }
}
