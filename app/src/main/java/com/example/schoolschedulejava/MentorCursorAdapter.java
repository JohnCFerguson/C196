package com.example.schoolschedulejava;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MentorCursorAdapter extends CursorAdapter {
    MentorCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.mentor_list_item, parent, false
        );
    }

    //@Override
    public void bindView(View view, Context context, Cursor mentorCursor) {


        String mentorName = mentorCursor.getString(mentorCursor.getColumnIndex(DBOpenHelper.MENTOR_NAME));
        String mentorEmail = mentorCursor.getString(mentorCursor.getColumnIndex(DBOpenHelper.MENTOR_EMAIL));
        String mentorPhone = mentorCursor.getString(mentorCursor.getColumnIndex(DBOpenHelper.MENTOR_PHONE));

        TextView tvMentorName = view.findViewById(R.id.tvMentorName);
        TextView tvMentorEmail = view.findViewById(R.id.tvMentorEmail);
        TextView tvMentorPhone = view.findViewById(R.id.tvMentorPhone);

        tvMentorName.setText(mentorName);
        tvMentorEmail.setText(mentorEmail);
        tvMentorPhone.setText(mentorPhone);
    }
}
