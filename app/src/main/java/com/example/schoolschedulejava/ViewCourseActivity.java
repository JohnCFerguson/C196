package com.example.schoolschedulejava;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewCourseActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1002;
    private CursorAdapter cA;
    private Intent intent;
    private long courseId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        intent = getIntent();


        Log.d("Term ID in View Term: ", intent.getLongExtra("CourseId", 0) + "");

        courseId = intent.getLongExtra("CourseId", 0);

        String selection = DBOpenHelper.TERM_ID + " = ?";
        String[] selectionArgs = {courseId + ""};

        Cursor courseQuery = getContentResolver().query(CourseProvider.COURSES_URI, DBOpenHelper.COURSES_COLUMNS,
                selection, selectionArgs, null, null);

        String courseName = "";
        String courseDates = "";
        DBOpenHelper.Course

        ArrayList<String> courseList = new ArrayList<String>();

        try {
            while(courseQuery.moveToNext()) {
                courseName = courseQuery.getString(courseQuery.getColumnIndex(DBOpenHelper.COURSE_TITLE));
                courseDates = courseQuery.getString(courseQuery.getColumnIndex(DBOpenHelper.COURSE_START)) + " - " +
                        courseQuery.getString(courseQuery.getColumnIndex(DBOpenHelper.COURSE_END));

            }
        }
        catch (Exception e) {
            Log.e("ViewTermActivity", "Error moving to next " + e);
        }

        TextView termNameView = findViewById(R.id.termTitle);
        TextView termDatesVew = findViewById(R.id.termDates);

        termNameView.setText(termName);
        termDatesVew.setText(termDates);

        cA = new CourseCursorAdapter(this, null,0);


        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
