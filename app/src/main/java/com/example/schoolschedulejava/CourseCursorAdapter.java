package com.example.schoolschedulejava;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseCursorAdapter extends CursorAdapter {

    CourseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.course_list_item, parent, false
        );
    }

    //@Override
    public void bindView(View view, Context context, Cursor courseCursor) {

        /*long courseId = courseCursor.getLong(courseCursor.getColumnIndex(DBOpenHelper.COURSE_ID));

        String[] columnNames = courseCursor.getColumnNames();

        for(String column : columnNames) {
            Log.d("Column Name: ", column);
        }

        Log.d("Course Id", String.valueOf(courseId));*/

        String courseTitle = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_TITLE));

        String courseDates = courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_START)

        ) + " - " + courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_END)
        );

        String courseStatus = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_STATUS));

        String termId = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.TERMID));

        String mentorName = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.MENTOR_NAME));
        String mentorEmail = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.MENTOR_EMAIL));
        String mentorPhone = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.MENTOR_PHONE));

        //TextView tvCourseId = view.findViewById(R.id.tvCourseId);
        TextView tvCourse = view.findViewById(R.id.tvCourse);
        TextView tvCourseDates = view.findViewById(R.id.tvCourseDates);
        TextView tvCourseStatus = view.findViewById(R.id.tvStatus);
        //TextView tvTermId = view.findViewById(R.id.tvTermId);

        TextView tvMentorName = view.findViewById(R.id.tvMentorName);
        TextView tvMentorEmail = view.findViewById(R.id.tvMentorEmail);
        TextView tvMentorPhone = view.findViewById(R.id.tvMentorPhone);

        //tvCourseId.setText(String.valueOf(courseId));
        tvCourse.setText(courseTitle);
        tvCourseDates.setText(courseDates);
        tvCourseStatus.setText(courseStatus);
        tvMentorName.setText(mentorName);
        tvMentorEmail.setText(mentorEmail);
        tvMentorPhone.setText(mentorPhone);
    }
}
