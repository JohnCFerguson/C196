package com.example.schoolschedulejava;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ViewCourseActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 1002;
    private String action;
    private CursorAdapter cA;
    private Intent intent;
    private String notes;
    private int courseId;
    private EditText cvNotes;
    private ListView list;
    private Calendar courseStart;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        courseStart = Calendar.getInstance();

        getLoaderManager().initLoader(0, null, this);

        String courseDates = intent.getStringExtra("Dates");
        String strCourseStart = courseDates.substring(0, courseDates.indexOf(' '));

        Log.d("ViewCourseActivity", strCourseStart);
        try {
            courseStart.setTime(sdf.parse(strCourseStart));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    public void restartLoader() {
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

    public void shareNotes(View view) {
        TextView notesList = findViewById(R.id.cvNotesList);
        String strNotes = notesList.getText().toString();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, strNotes);
        startActivity(Intent.createChooser(sharingIntent, "Share Notes via"));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "courses." + DBOpenHelper.COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(intent.getIntExtra("CourseId", 0))};

        Log.d("ViewCourse Loader", selectionArgs.toString());
        return new CursorLoader(
                this,   // Parent activity context
                CourseWithExtrasProvider.COURSE_WITH_EXTRAS_URI,       // Table to query
                null,     // Projection to return
                selection,
                selectionArgs,
                null             // Default sort order
        );
    }

    public void addAssessment(View view) {
        Log.d("Add Assessment", "Adding Assessment");
        AssessmentsDialogFragment assessmentsDialogFragment = new AssessmentsDialogFragment();
        assessmentsDialogFragment.show(getSupportFragmentManager(), "AssessmentDialogFragment");
        Intent intent = new Intent();
        intent.putExtra("CourseId", courseId);
    }

    public void editAssessments(View view) {
        Intent intent = new Intent(this, ViewAssessmentsActivity.class);
        intent.putExtra("CourseId", courseId);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public void setReminder(View view) {
        Intent notificationIntent = new Intent(this, CourseNotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, courseStart.getTimeInMillis(), pendingIntent);
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
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
