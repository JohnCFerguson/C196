package com.example.schoolschedulejava;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewAssessmentsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private Intent intent;
    private int courseId;
    private CursorAdapter cA;
    private ListView list;
    private int EDITOR_REQUEST_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assessments);

        intent = getIntent();

        Log.d("Course ID ViewCourse: ", intent.getIntExtra("CourseId", 0) + "");
        //Log.d("Course Name ViewCourse", intent.getStringExtra("CourseName"));

        courseId = intent.getIntExtra("CourseId", 0);

        cA = new ViewAssessmentsAdapter(this, null,0);
        list = findViewById(R.id.eaList);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finishEditing();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }

    private void finishEditing() {
        setResult(RESULT_OK);
        finish();
    }


    public void setReminder(View view) {
        Intent notificationIntent = new Intent(this, AssessmentNotifyService.class);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);

        TextView timeView = findViewById(R.id.eaAssessmentDate);

        String time = timeView.getText().toString();

        Calendar notificationDate = Calendar.getInstance();
        try{
            notificationDate.setTime(sdf.parse(time));
        }
        catch (Exception e) {
            e.getMessage();
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Reminder set for this Assessment!",
                Toast.LENGTH_LONG).show();
    }

    public void editAssessment(View view) {
        TextView assessmentNameView = findViewById(R.id.eaAssessmentName);
        String assessmentName = assessmentNameView.getText().toString();

        try{
            Intent intent = new Intent(this, EditAssessmentsActivity.class);
            intent.putExtra("CourseId", courseId);
            intent.putExtra("AssessmentName", assessmentName);
            startActivityForResult(intent, EDITOR_REQUEST_CODE);
        }
        catch (Exception e){
            e.getMessage();
        }

    }

    public void deleteAssessment(View view) {
        TextView assessmentNameView = findViewById(R.id.eaAssessmentName);
        String assessmentName = assessmentNameView.getText().toString();
        String[] selectionArgs = {assessmentName, courseId + ""};
        String selection = DBOpenHelper.ASSESSMENT_NAME + " = ? AND " + DBOpenHelper.COURSEID + " = ?";

        getContentResolver().delete(AssessmentProvider.ASSESSMENTS_URI, selection, selectionArgs);

        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "assessments." + DBOpenHelper.COURSEID + " = ?";
        String[] selectionArgs = {String.valueOf(intent.getIntExtra("CourseId", 0))};

        return new CursorLoader(
                this,   // Parent activity context
                AssessmentProvider.ASSESSMENTS_URI,       // Table to query
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
