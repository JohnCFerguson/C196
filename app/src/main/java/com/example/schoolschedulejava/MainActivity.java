package com.example.schoolschedulejava;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission_group.CALENDAR;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String SET_DEBUG_TAG = "Jank is borked";
    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cA = new TermCursorAdapter(this, null,0);
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cA);
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                openTermViewForExistingTerm(view, id);
            }
        });


        getLoaderManager().initLoader(0, null, this);
    }

    private Uri insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        Uri termUri = getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + termUri.getLastPathSegment());

        restartLoader();

        return termUri;
    }

    private Uri insertAssessment(String assessmentName, String assessmentType) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, assessmentType);
        values.put(DBOpenHelper.ASSESSMENT_DATE, "13-May-2019");
        values.put(DBOpenHelper.COURSEID, 1);
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
        Log.d("MainActivity", "Inserted Mentor " + mentorUri.getLastPathSegment());

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
        values.put(DBOpenHelper.COURSE_NOTES, courseNotes);
        Uri courseUri = getContentResolver().insert(CourseProvider.COURSES_URI, values);
        Log.d("MainActivity", "Inserted Course " + courseUri.getLastPathSegment());

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
            case R.id.action_delete_all:
                deleteAllData();
                break;
            case R.id.action_add_new_term:
                //openTermEditorForNewTerm();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            getContentResolver().delete(
                                    TermProvider.TERMS_URI, null, null
                            );
                            getContentResolver().delete(
                                    CourseProvider.COURSES_URI, null, null
                            );
                            getContentResolver().delete(
                                    MentorProvider.MENTORS_URI, null, null
                            );
                            getContentResolver().delete(
                                    AssessmentProvider.ASSESSMENTS_URI, null, null
                            );

                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();


    }

    private void insertSampleData() {
        Calendar termStart = Calendar.getInstance();
        termStart.set(termStart.get(Calendar.YEAR), termStart.get(Calendar.MONTH), 1);
        Calendar termEnd = Calendar.getInstance();
        termEnd.add(Calendar.MONTH, 5);
        termEnd.set(termEnd.get(Calendar.YEAR), termEnd.get(Calendar.MONTH), termEnd.getActualMaximum(Calendar.DATE));

        Uri termUri = insertTerm("Term 1", "1/1/2019", "6/30/2019");
        Uri mentorUri = insertMentor("Mentor Guy", "email@email.com", "123457890");
        Uri assessmentUri = insertAssessment("Course 1 Final Assessment", "Performance");
        Uri courseUri = insertCourse(Integer.parseInt(termUri.getLastPathSegment()),
               Integer.parseInt(mentorUri.getLastPathSegment()),"Course 1", "1/1/2019",
               "1/31/2019", "Active", assessmentUri.getLastPathSegment(), "This course is tough");

        restartLoader();

    }

    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,   // Parent activity context
                TermProvider.TERMS_URI,       // Table to query
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

    public void openTermViewForExistingTerm(View view, long termId) {
        Intent intent = new Intent(this, ViewTermActivity.class);
        intent.putExtra("TermId", termId);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addNewTerm(View view) {
        TermsDialogFragment termDialogFragment = new TermsDialogFragment();
        termDialogFragment.show(getSupportFragmentManager(), "TermDialogFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("ActivityResult", requestCode + " " + resultCode);
        if(requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
