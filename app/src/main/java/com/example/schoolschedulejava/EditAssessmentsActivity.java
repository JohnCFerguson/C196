package com.example.schoolschedulejava;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

class EditAssessmentsActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

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

        assessmentName = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NAME));
        String assessmentType = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TYPE));
        String assessmentDate = assessmentCursor.getString(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATE));

        EditText editAssessmentName = findViewById(R.id.editAssessmentName);
        Spinner editAssessmentType = findViewById(R.id.editAssessmentType);
        CalendarView editAssessmentDate = findViewById(R.id.editAssessmentDate);

        editAssessmentName.setText(assessmentName);
        try {
            editAssessmentDate.setDate(sdf.parse(assessmentDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
