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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ViewCourseActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1002;
    private String action;
    private Boolean edited = false;
    private CursorAdapter cA;
    private Intent intent;
    private String notes;
    private int courseId;
    private EditText cvNotes;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        intent = getIntent();

        Log.d("Course ID ViewCourse: ", intent.getIntExtra("CourseId", 0) + "");
        //Log.d("Course Name ViewCourse", intent.getStringExtra("CourseName"));

        courseId = intent.getIntExtra("CourseId", 0);

        cA = new ViewCourseCursorAdapter(this, null,0);
        list = findViewById(R.id.cvList);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finishEditing();
                break;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        finishEditing();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void finishEditing() {
        cvNotes = findViewById(R.id.cvNotes);

        if (cvNotes.getText() != null) {
            notes = cvNotes.getText().toString();
        }
        Log.d("ViewCourseActivity", notes);
        if (notes.length() == 0) {
            setResult(RESULT_CANCELED);
        } else {
            updateNotes(courseId, notes);
            setResult(RESULT_OK);
        }
        finish();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void updateNotes(long courseId, String note) {
        TextView notesList = findViewById(R.id.cvNotesList);
        String selection = "courses." + DBOpenHelper.COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTES, notesList.getText() + note + " \n\n");
        int noteInserted = getContentResolver().update(CourseProvider.COURSES_URI, values, selection, selectionArgs);
        //Log.d("ViewCourseActivity", "Note Inserted " + noteInserted);
    }

    public void editCourse(View view) {
        Intent intent = new Intent(this, EditCourseActivity.class);
        intent.putExtra("CourseId", courseId);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "courses." + DBOpenHelper.COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(intent.getIntExtra("CourseId", 0))};

        Log.d("ViewCourse Loader", selectionArgs.toString());
        return new CursorLoader(
                this,   // Parent activity context
                CourseWithMentorProvider.COURSE_WITH_MENTOR_URI,       // Table to query
                null,     // Projection to return
                selection,
                selectionArgs,
                null             // Default sort order
        );
    }

    public void addAssessment(View view) {
        Log.d("Add Assessment", "Adding Assessment");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add Assessment")
                .
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Do nothing, just close dialog
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                        }
                        catch (Exception e) {
                            e.getMessage();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cA.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cA.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("ViewActivityResult", requestCode + " " + resultCode);
        if(requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
