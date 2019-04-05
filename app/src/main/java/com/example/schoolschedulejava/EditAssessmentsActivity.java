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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class EditAssessmentsActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private EditText editAssessmentName;
    private Spinner editAssessmentType;
    private final Calendar assDate = Calendar.getInstance();
    private String assessmentId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessments);

        String[] assTypes = {"Objective", "Performance"};

        Intent intent = getIntent();

        Log.d("Course ID ViewCourse: ", intent.getIntExtra("CourseId", 0) + "");
        //Log.d("Course Name ViewCourse", intent.getStringExtra("CourseName"));
        String assessmentName = intent.getStringExtra("AssessmentName");
        int courseId = intent.getIntExtra("CourseId", 0);

        String[] selectionArgs = {assessmentName, courseId + ""};

        String selection = "assessments." + DBOpenHelper.ASSESSMENT_NAME + " = ? AND assessments." + DBOpenHelper.COURSEID + " = ?";

        Cursor assessmentCursor = getContentResolver().query(AssessmentProvider.ASSESSMENTS_URI, null,
                selection, selectionArgs, null, null);


        assessmentCursor.moveToNext();

        assessmentId = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_ID));
        assessmentName = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NAME));
        String assessmentType = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TYPE));
        String assessmentDate = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATE));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, assTypes);
        int spinnerPos = adapter.getPosition(assessmentType);

        editAssessmentName = findViewById(R.id.editAssessmentName);
        editAssessmentType = findViewById(R.id.editAssessmentType);
        CalendarView editAssessmentDate = findViewById(R.id.editAssessmentDate);

        editAssessmentName.setText(assessmentName);
        editAssessmentType.setAdapter(adapter);
        editAssessmentType.setSelection(spinnerPos, true);

        try {
            editAssessmentDate.setDate(sdf.parse(assessmentDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        editAssessmentDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Log.d("AddTermActivity", "Cal date is: " + year + " / " + (month + 1) + " / " + dayOfMonth);
                assDate.set(year, month, dayOfMonth);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to update this assessment?")
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

        String assessmentName = editAssessmentName.getText().toString().trim();
        String assessmentType = editAssessmentType.getSelectedItem().toString().trim();
        String assessmentDate = sdf.format(assDate.getTime());

        String selection = "assessments." + DBOpenHelper.ASSESSMENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(assessmentId)};
        ContentValues values = new ContentValues();

        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, assessmentType);
        values.put(DBOpenHelper.ASSESSMENT_DATE, assessmentDate);
        int assUri = getContentResolver().update(AssessmentProvider.ASSESSMENTS_URI, values, selection, selectionArgs);
        Log.d("EditAssessmentActivity", "Updated Assessment " + assUri);
    }
}
