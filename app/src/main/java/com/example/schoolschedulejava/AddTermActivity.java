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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTermActivity extends AppCompatActivity {

    private static final String SET_DEBUG_TAG = "Jank is borked";
    private String action;
    private ArrayList<String> termsList;
    private Cursor termsQuery;
    private CalendarView cal;
    private TextView termTitle;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        termsList = new ArrayList<>();

        termsQuery = getContentResolver().query(TermProvider.TERMS_URI, DBOpenHelper.TERMS_COLUMNS,
                null, null, null, null);

        try {
            while(termsQuery.moveToNext()) {
                String term = termsQuery.getString(termsQuery.getColumnIndex(DBOpenHelper.TERM_TITLE));
                Log.d("AddTermActivity", term);
                try {
                    termsList.add(term);
                }
                catch (Exception e) {
                    Log.e(SET_DEBUG_TAG, "Error " + e.toString());
                }
                finally {
                    termsQuery.close();
                }
            }
        }
        catch (Exception e) {
            Log.e(SET_DEBUG_TAG, "Error going to next " + e.toString());
        }

        Intent intent = getIntent();


        termTitle = findViewById(R.id.termName);
        termTitle.setText("Term " + (termsList.size() + 1));
        cal = findViewById(R.id.calendarView);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Log.d("AddTermActivity", "Cal date is: " + year + " / " + (month + 1) + " / " + dayOfMonth);
                startDate.set(year, month, 1);
                endDate.setTime(startDate.getTime());
                endDate.add(Calendar.MONTH, 5);
                endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.getActualMaximum(Calendar.DATE));
                //Log.d("AddTermActivity", endDate.get(Calendar.YEAR) + " / " + (endDate.get(Calendar.MONTH) + 1) +  " / " + endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        });

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
        finishEditing();
    }

    private void finishEditing() {
        String newTitle = termTitle.getText().toString().trim();
        String strStartDate =  (startDate.get(Calendar.MONTH) + 1) +  " / " + startDate.getActualMinimum(Calendar.DAY_OF_MONTH) + " / " + startDate.get(Calendar.YEAR);
        String strEndDate = (endDate.get(Calendar.MONTH) + 1) +  " / " + endDate.getActualMaximum(Calendar.DAY_OF_MONTH) + " / " + endDate.get(Calendar.YEAR);

        switch(action) {
            case Intent.ACTION_INSERT:
                if(newTitle.length() == 0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    insertTerm(newTitle, strStartDate, strEndDate);
                }
                break;
            case Intent.ACTION_EDIT:
                break;
        }
        finish();
    }

    private void insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        getContentResolver().insert(TermProvider.TERMS_URI, values);
        setResult(RESULT_OK);
    }
}
