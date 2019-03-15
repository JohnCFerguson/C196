package com.example.schoolschedulejava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {

    private static final String SET_DEBUG_TAG = "Jank is borked in AddCourseActivity";
    private String action;
    private EditText courseName;
    private EditText mentorName;
    private EditText mentorEmail;
    private EditText mentorPhone;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private Intent intent;
    private int termId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        intent = getIntent();

        termId = (int) intent.getLongExtra("TermId", 0);

        courseName = findViewById(R.id.courseName);
        CalendarView courseStart = findViewById(R.id.courseStart);
        CalendarView courseEnd = findViewById(R.id.courseEnd);
        mentorName = findViewById(R.id.mentorName);
        mentorEmail = findViewById(R.id.mentorEmail);
        mentorPhone = findViewById(R.id.mentorPhone);


        courseStart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Log.d("AddTermActivity", "Cal date is: " + year + " / " + (month + 1) + " / " + dayOfMonth);
                startDate.set(year, month, dayOfMonth);
            }
        });

        courseEnd.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Log.d("AddTermActivity", "Cal date is: " + year + " / " + (month + 1) + " / " + dayOfMonth);
                endDate.set(year, month, dayOfMonth);
            }
        });


        Uri uri = intent.getParcelableExtra(CourseProvider.COURSE_ITEM_TYPE);

        if(uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_term);
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
        String newCourse = courseName.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String strStartDate =  sdf.format(startDate.getTime());
        String strEndDate = sdf.format(endDate.getTime());

        int mentorId = getMentorId();

        String courseStatus = "Planning to Take";
        String courseAssessments = "";
        String courseNotes = "";

        switch(action) {
            case Intent.ACTION_INSERT:
                if(newCourse.length() == 0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    insertCourse(termId, mentorId, newCourse, strStartDate, strEndDate,
                            courseStatus, courseAssessments, courseNotes);
                }
                break;
            case Intent.ACTION_EDIT:
                break;
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private int getMentorId() {
        String strMentorName = mentorName.getText().toString().trim();
        String strMentorEmail = mentorEmail.getText().toString().trim();
        String strMentorPhone = mentorPhone.getText().toString().trim();

        int mentorId = 0;
        String selection = DBOpenHelper.MENTOR_NAME + " = ?  AND " + DBOpenHelper.MENTOR_PHONE + " = ? AND " +
                DBOpenHelper.MENTOR_PHONE + " = ?";
        String[] selectionArgs = {strMentorName, strMentorEmail, strMentorPhone};

        Cursor mentorQuery = getContentResolver().query(MentorProvider.MENTORS_URI, DBOpenHelper.MENTORS_COLUMNS,
                selection, selectionArgs, null, null);


        try{
            if(mentorQuery.moveToNext()){
                mentorId = mentorQuery.getInt(mentorQuery.getColumnIndex(DBOpenHelper.MENTOR_ID));
            }
            else {
                ContentValues values = new ContentValues();
                values.put(DBOpenHelper.MENTOR_NAME, strMentorName);
                values.put(DBOpenHelper.MENTOR_EMAIL, strMentorEmail);
                values.put(DBOpenHelper.MENTOR_PHONE, strMentorPhone);

                Uri mentorUri = getContentResolver().insert(MentorProvider.MENTORS_URI, values);

                mentorId = Integer.parseInt(mentorUri.getLastPathSegment());
                Log.d("MainActivity", "Inserted Mentor " + mentorUri.getLastPathSegment());
            }
        }
        catch (NullPointerException e) {
            Log.e("AddCourseActivity", "Error moving to next mentor " + e);
        }

        return mentorId;
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
        Uri courseUri = getContentResolver().insert(CourseProvider.COURSES_URI, values);
        Log.d("AddCourseActivity", "Inserted Course " + courseUri.getLastPathSegment());

    }
}
