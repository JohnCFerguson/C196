package com.example.schoolschedulejava;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseCursorAdapter extends CursorAdapter
    implements LoaderManager.LoaderCallbacks<Cursor>{

    CourseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private CursorAdapter mCA;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.course_list_item, parent, false
        );
    }

    //@Override
    public void bindView(View view, Context context, Cursor courseCursor) {

        String courseTitle = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_TITLE));

        String courseDates = courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_START)

        ) + " - " + courseCursor.getString(
                courseCursor.getColumnIndex(DBOpenHelper.COURSE_END)
        );

        String courseStatus = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSE_STATUS));

        String termId = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.TERMID));

        TextView tvCourse = view.findViewById(R.id.tvCourse);
        TextView tvCourseDates = view.findViewById(R.id.tvCourseDates);
        TextView tvCourseStatus = view.findViewById(R.id.tvStatus);
        TextView tvTermId = view.findViewById(R.id.tvTermId);

        tvCourse.setText(courseTitle);
        tvCourseDates.setText(courseDates);
        tvCourseStatus.setText(courseStatus);
        tvTermId.setText(termId);

        mCA = new MentorCursorAdapter(context, null, 0);
        ListView list = view.findViewById(R.id.mentorList);
        list.setAdapter(mCA);

    }
}
