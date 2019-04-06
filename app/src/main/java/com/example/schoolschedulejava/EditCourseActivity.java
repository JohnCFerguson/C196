package com.example.schoolschedulejava;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditCourseActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private int courseId;
    private Intent intent;
    private Calendar calStartDate = Calendar.getInstance();
    private Calendar calEndDate = Calendar.getInstance();
    private Spinner statusSpinner;
    private EditText assessmentsView;
    private EditText mentorNameView;
    private EditText mentorEmailView ;
    private EditText mentorPhoneView;
    private EditText notesView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        ArrayList<String> status = new ArrayList<>();

        status.add("Planning to Take");
        status.add("In Progress");
        status.add("Dropped");
        status.add("Complete");

        intent = getIntent();

        courseId = intent.getIntExtra("CourseId", 0);

        String[] selectionArgs = {String.valueOf(courseId)};

        Cursor courseCursor = getContentResolver().query(CourseWithExtrasProvider.COURSE_WITH_EXTRAS_URI, null,
                "courses." + DBOpenHelper.COURSE_ID + " = ?", selectionArgs, null, null);

        courseCursor.moveToNext();

        String courseName = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_TITLE));

        String startDate = courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_START)
        );

        String endDate =  courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_END)
        );

        String courseStatus = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_STATUS));

        String mentorName = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.MENTOR_NAME));
        String mentorEmail = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.MENTOR_EMAIL));
        String mentorPhone = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.MENTOR_PHONE));

        String notes = "";
        if(courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_NOTES)) != null) {
            notes = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_NOTES));
        }

        //Log.d("ViewCourseCursorAdapter", "Notes: " + notes);

        TextView courseNameView = findViewById(R.id.ceCourseName);
        CalendarView startDateCal = findViewById(R.id.ceCourseStart);
        CalendarView endDateCal = findViewById(R.id.ceCourseEnd);
        statusSpinner = findViewById(R.id.ceStatusSpinner);
        mentorNameView = findViewById(R.id.ceMentorName);
        mentorEmailView = findViewById(R.id.ceMentorEmail);
        mentorPhoneView = findViewById(R.id.ceMentorPhone);
        notesView = findViewById(R.id.ceNotesList);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, status);
        statusSpinner.setAdapter(adapter);

        int spinnerPos = adapter.getPosition(courseStatus);

        courseNameView.setText(courseName);
        try {
            startDateCal.setDate(sdf.parse(startDate).getTime());
            startDateCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    //Log.d("AddTermActivity", "Cal date is: " + year + " / " + (month + 1) + " / " + dayOfMonth);
                    calStartDate.set(year,month,dayOfMonth);
                }
            });
            calStartDate.setTimeInMillis(startDateCal.getDate());
            endDateCal.setDate(sdf.parse(endDate).getTime());
            calEndDate.setTimeInMillis(endDateCal.getDate());
            endDateCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    //Log.d("AddTermActivity", "Cal date is: " + year + " / " + (month + 1) + " / " + dayOfMonth);
                    calEndDate.set(year, month, dayOfMonth);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

        statusSpinner.setSelection(spinnerPos, true);
        mentorNameView.setText(mentorName);
        mentorEmailView.setText(mentorEmail);
        mentorPhoneView.setText(mentorPhone);
        notesView.setText(notes);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to update this course?")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(RESULT_OK);
                        try {
                            finishEditing();
                            finish();
                        }
                        catch (Exception e) {
                            e.getMessage();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void finishEditing() {

        String mentorName = mentorNameView.getText().toString().trim();
        String mentorEmail = mentorEmailView.getText().toString().trim();
        String mentorPhone = mentorPhoneView.getText().toString().trim();


        String strStartDate = sdf.format(calStartDate.getTime());
        String strEndDate = sdf.format(calEndDate.getTime());

        String status = statusSpinner.getSelectedItem().toString().trim();

        int mentorId = getMentorId(mentorName, mentorEmail, mentorPhone);
        String notes = notesView.getText().toString().trim();

        String selection = "courses." + DBOpenHelper.COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};
        ContentValues values = new ContentValues();

        values.put(DBOpenHelper.COURSE_START, strStartDate);
        values.put(DBOpenHelper.COURSE_END, strEndDate);
        values.put(DBOpenHelper.COURSE_STATUS, status);
        values.put(DBOpenHelper.MENTORID, mentorId);
        values.put(DBOpenHelper.COURSE_NOTES, notes);
        values.put(DBOpenHelper.MENTORID, mentorId);
        int courseUri = getContentResolver().update(CourseProvider.COURSES_URI, values, selection, selectionArgs);
        Log.d("EditCourseActivity", "Updated Course " + courseUri);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private int getMentorId(String strMentorName, String strMentorEmail, String strMentorPhone) {
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
                Log.d("AddCourseActivity", "Inserted Mentor " + mentorUri.getLastPathSegment());
            }
        }
        catch (NullPointerException e) {
            Log.e("AddCourseActivity", "Error moving to next mentor " + e);
        }

        return mentorId;
    }
}
