package com.example.schoolschedulejava;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TermCursorAdapter extends CursorAdapter {
    TermCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.term_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String termTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_TITLE));

        String termDates = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.TERM_START)

        ) + " - " + cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.TERM_END)
        );

        TextView tvTerm = view.findViewById(R.id.tvTerm);
        TextView tvTermDates = view.findViewById(R.id.tvTermDates);

        tvTerm.setText(termTitle);
        tvTermDates.setText(termDates);
    }
}
