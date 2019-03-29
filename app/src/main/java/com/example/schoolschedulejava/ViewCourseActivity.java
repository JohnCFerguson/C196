package com.example.schoolschedulejava;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;


import java.text.SimpleDateFormat;


public class ViewCourseActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1002;
    private String action;
    private CursorAdapter cA;
    private Intent intent;
    private String notes;
    private long courseId;
    private EditText cvNotes;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        intent = getIntent();

        Log.d("Course ID ViewCourse: ", intent.getLongExtra("CourseId", 0) + "");
        //Log.d("Course Name ViewCourse", intent.getStringExtra("CourseName"));

        courseId = intent.getLongExtra("CourseId", 0);

        cvNotes = findViewById(R.id.cvNotes);

        cA = new ViewCourseCursorAdapter(this, null,0);
        //cA = new TermCursorAdapter(this, null,0);
        ListView list = findViewById(R.id.cvList);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }

    private void finishEditing() {
        notes = cvNotes.getText().toString();
        if(notes.length() == 0){
            setResult(RESULT_CANCELED);
        }
        else {
            updateNotes(courseId, notes);
            setResult(RESULT_OK);
        }
        finish();
    }

    private void updateNotes(long courseId, String note) {
        String selection = "courses." + DBOpenHelper.COURSE_TITLE + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTES, note);
        int noteInserted = getContentResolver().update(CourseProvider.COURSES_URI, values, selection, selectionArgs);
        Log.d("ViewCourseActivity", "Note Inserted " + noteInserted);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "courses." + DBOpenHelper.COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(intent.getLongExtra("CourseId", 0))};

        return new CursorLoader(
                this,   // Parent activity context
                CourseWithMentorProvider.COURSE_WITH_MENTOR_URI,       // Table to query
                null,     // Projection to return
                selection,
                selectionArgs,
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
