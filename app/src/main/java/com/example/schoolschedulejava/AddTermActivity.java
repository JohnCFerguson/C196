package com.example.schoolschedulejava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTermActivity extends AppCompatActivity {

    private static final String SET_DEBUG_TAG = "Jank is borked";
    private String action;
    private ArrayList<String> termsList;
    private Cursor termsQuery;
    private CalendarView cal;
    String termTitle;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        termsList = new ArrayList<>();

        termsQuery = getContentResolver().query(TermProvider.TERMS_URI, DBOpenHelper.TERMS_COLUMNS,
                null, null, null, null);

        try {
            do {
                termsQuery.moveToNext();
                termTitle = termsQuery.getString(termsQuery.getColumnIndex(DBOpenHelper.TERM_TITLE));
                Log.d("AddTermActivity", termTitle);
                try {
                    termsList.add(termTitle);
                }
                catch (Exception e) {
                    Log.e(SET_DEBUG_TAG, "Error " + e.toString());
                }
            } while(termsQuery.moveToNext());
        }
        catch (Exception e) {
            Log.e(SET_DEBUG_TAG, "Error going to next " + e.toString());
        }

        /*for (String s : termsList) {
            Log.d("AddTermActivity", s);
        }*/

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

    @Override
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
        //finishEditing();
    }

    private void finishEditing() {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(cal.getDate());
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 4);
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
