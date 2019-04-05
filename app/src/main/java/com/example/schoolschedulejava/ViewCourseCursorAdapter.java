package com.example.schoolschedulejava;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ViewCourseCursorAdapter extends CursorAdapter {

    ViewCourseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.view_course_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        /*String courseId = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_ID));

        Log.d("Course Id", courseId);*/

        String[] colNames = cursor.getColumnNames();
        for (String col : colNames) {
            Log.d("ViewCourseCursor", col);
        }


        String courseName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_TITLE));

        String courseDates = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.COURSE_START)

        ) + " - " + cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.COURSE_END)
        );

        String courseStatus = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_STATUS));


        String mentorName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_NAME));
        String mentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_EMAIL));
        String mentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_PHONE));

        String assessments = cursor.getString(cursor.getColumnIndex("assessments"));
        String newAssessment = "";
        if(assessments != null) {
            newAssessment = assessments.replace(',', '\n');
            Log.d("ViewCourseAdapter", newAssessment);
        }

        String notes = "";
        if(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTES)) != null) {
            notes = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTES));
        }

        //Log.d("ViewCourseCursorAdapter", "Notes: " + notes);
        //TextView courseIdView = view.findViewById(R.id.cvCourseId);
        TextView courseNameView = view.findViewById(R.id.courseTitle);
        TextView courseDatesView = view.findViewById(R.id.courseDates);
        TextView courseStatusView = view.findViewById(R.id.cvStatus);
        TextView mentorNameView = view.findViewById(R.id.cvMentorName);
        TextView mentorEmailView = view.findViewById(R.id.cvMentorEmail);
        TextView mentorPhoneView = view.findViewById(R.id.cvMentorPhone);
        TextView assessmentsView = view.findViewById(R.id.cvAssessments);
        TextView notesView = view.findViewById(R.id.cvNotesList);

        //courseIdView.setText(courseId);
        courseNameView.setText(courseName);
        courseDatesView.setText(courseDates);
        courseStatusView.setText(courseStatus);
        mentorNameView.setText(mentorName);
        mentorEmailView.setText(mentorEmail);
        mentorPhoneView.setText(mentorPhone);
        assessmentsView.setText(newAssessment);
        notesView.setText(notes);
    }
}
