package com.example.schoolschedulejava;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ViewAssessmentsAdapter extends CursorAdapter {

    public ViewAssessmentsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.edit_assessment_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String assessmentName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NAME));
        String assessmentType = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TYPE));
        String assessmentDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATE));

        TextView eaAssessmentName = view.findViewById(R.id.eaAssessmentName);
        TextView eaAssessmentType = view.findViewById(R.id.eaAssessmentType);
        TextView eaAssessmentDate = view.findViewById(R.id.eaAssessmentDate);

        eaAssessmentName.setText(assessmentName);
        eaAssessmentType.setText(assessmentType);
        eaAssessmentDate.setText(assessmentDate);
    }
}
