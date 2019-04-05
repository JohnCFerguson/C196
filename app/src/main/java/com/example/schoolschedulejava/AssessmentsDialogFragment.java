package com.example.schoolschedulejava;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AssessmentsDialogFragment extends DialogFragment {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_assessment, null);

        final Spinner assSpinner = view.findViewById(R.id.addAssessmentSpinner);
        final EditText assNameView = view.findViewById(R.id.assessmentName);

        String[] assTypes = {"Objective", "Performance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, assTypes);
        assSpinner.setAdapter(adapter);

        final Calendar assDate = Calendar.getInstance();
        CalendarView assDateView = view.findViewById(R.id.assessmentDate);
        assDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                assDate.set(year,month,dayOfMonth);
                Log.d("AddTermActivity", sdf.format(assDate.getTime()));
            }
        });

        builder.setView(view)
                .setPositiveButton(R.string.add_assessment, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String assName = assNameView.getText().toString().trim();
                        String strAssDate = sdf.format(assDate.getTime());
                        String strAssType = assSpinner.getSelectedItem().toString();
                        addAssessment(assName, strAssDate, strAssType);

                        setReminder(strAssDate);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Don't really need to do anything here, this should close the dialog
                    }
                });

        return builder.create();
    }

    private void setReminder(String time) {
        Intent notificationIntent = new Intent(getActivity(), AssessmentNotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, notificationIntent, 0);

        Calendar notificationDate = Calendar.getInstance();
        try{
            notificationDate.setTime(sdf.parse(time));
        }
        catch (Exception e) {
            e.getMessage();
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addAssessment(String name, String time, String type) {
        if(name != "" || name != null) {
            ContentValues values = new ContentValues();

            values.put(DBOpenHelper.ASSESSMENT_NAME, name);
            values.put(DBOpenHelper.ASSESSMENT_DATE, time);
            values.put(DBOpenHelper.ASSESSMENT_TYPE, type);
            values.put(DBOpenHelper.COURSEID,
                    getActivity().getIntent().getIntExtra("CourseId", 0));

            getActivity().getContentResolver().insert(AssessmentProvider.ASSESSMENTS_URI, values);

            TextView cvAssessment = getActivity().findViewById(R.id.cvAssessments);
            String currentAssessments = cvAssessment.getText().toString();
            String newAssessmentString = currentAssessments + "\n" + name;
            cvAssessment.setText(newAssessmentString);
        }
    }
}
