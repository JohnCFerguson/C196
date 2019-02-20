package com.example.schoolschedulejava;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

        String[] from = {DBOpenHelper.TERM_TITLE, DBOpenHelper.TERM_START};
        int[] to = {android.R.id.text1};
        cA = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, from, to, 0);
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
    }

    private Uri insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        Uri termUri = getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + termUri.getLastPathSegment());

        return termUri;
    }

    private Uri insertAssessment(String assessmentName, String assessmentType) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, assessmentType);
        Uri assessmentUri = getContentResolver().insert(AssessmentProvider.ASSESSMENTS_URI, values);
        Log.d("MainActivity", "Inserted Assessment " + assessmentUri.getLastPathSegment());

        return assessmentUri;
    }

    private Uri insertMentor(String mentorName, String mentorEmail, String mentorPhone) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.MENTOR_PHONE, mentorPhone);
        Uri mentorUri = getContentResolver().insert(MentorProvider.MENTORS_URI, values);
        Log.d("MainActivity", "Inserted Term " + mentorUri.getLastPathSegment());

        return mentorUri;
    }

    private Uri insertCourse(int termId, int mentorId, String courseTitle, String courseStart,
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
        Uri courseUri = getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + courseUri.getLastPathSegment());

        return courseUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertSampleData() {
       Uri termUri = insertTerm("Term 1", "1/1/2019", "5/1/2019");
       Uri mentorUri = insertMentor("Mentor Guy", "email@email.com", "123457890");
       Uri assessmentUri = insertAssessment("Course 1 Final Assessment", "Performance");
       Uri courseUri = insertCourse(Integer.parseInt(termUri.getLastPathSegment()),
               Integer.parseInt(mentorUri.getLastPathSegment()),"Course 1", "1/1/2019",
               "1/31/2019", "Active", assessmentUri.getLastPathSegment(), "This course is tough");

        getLoaderManager().restartLoader(0, null, this);

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
