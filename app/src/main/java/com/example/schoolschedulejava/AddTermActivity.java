package com.example.schoolschedulejava;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;

import java.time.LocalDate;
import java.util.Calendar;

public class AddTermActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private CalendarView cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        editor = findViewById(R.id.editText);

        Intent intent = getIntent();

        cal = findViewById(R.id.calendarView);

        Calendar startDate = Calendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 4);
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.getActualMaximum(Calendar.DATE));
        Log.d("AddTermActivity", endDate.get(Calendar.YEAR) + " / " + endDate.get(Calendar.MONTH) +  " / " + endDate.get(Calendar.DATE));

        cal.setDate(startDate.getTimeInMillis());

        Uri uri = intent.getParcelableExtra(TermProvider.TERM_ITEM_TYPE);

        if(uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_term);
        }
    }

    private void finishEditing() {
        String termTitle = editor.getText().toString().trim();
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(cal.getDate());
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 5);
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.getActualMaximum(endDate.get(Calendar.MONTH)));

        switch(action) {
            case Intent.ACTION_INSERT:
                if(termTitle.length() == 0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    //insertTerm(termTitle,);
                }
                break;
            case Intent.ACTION_EDIT:

                break;
        }
    }

    private void insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        Uri termUri = getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + termUri.getLastPathSegment());
    }
}
