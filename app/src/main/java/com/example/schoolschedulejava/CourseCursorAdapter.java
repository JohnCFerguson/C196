package com.example.schoolschedulejava;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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

        String termTitle = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_TITLE));

        String termDates = courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_START)

        ) + " - " + courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_END)
        );

        String courseStatus = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_STATUS));

       // String mentorName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_NAME));
       // String mentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_EMAIL));
       // String mentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_PHONE));


        TextView tvCourse = view.findViewById(R.id.tvCourse);
        TextView tvCourseDates = view.findViewById(R.id.tvCourseDates);
        TextView tvCourseStatus = view.findViewById(R.id.tvStatus);
        //TextView tvMentorName = view.findViewById(R.id.tvMentorName);
        //TextView tvMentorEmail = view.findViewById(R.id.tvMentorEmail);
        //TextView tvMentorPhone = view.findViewById(R.id.tvMentorPhone);


        tvCourse.setText(termTitle);
        tvCourseDates.setText(termDates);
        tvCourseStatus.setText(courseStatus);
        //tvMentorName.setText(mentorName);
        //tvMentorEmail.setText(mentorEmail);
        //tvMentorPhone.setText(mentorPhone);
    }
}
