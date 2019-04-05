package com.example.schoolschedulejava;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TermsDialogFragment extends DialogFragment {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private final String SET_DEBUG_TAG = "TermsDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_term, null);

        final EditText termNameView = view.findViewById(R.id.termName);

        final Calendar termStart = Calendar.getInstance();
        CalendarView termStartView = view.findViewById(R.id.termStart);
        termStartView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                termStart.set(year,month,dayOfMonth);
            }
        });

        builder.setView(view)
                .setPositiveButton(R.string.add_term, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String termName = termNameView.getText().toString().trim();
                        if(!termName.equals("")){
                            addTerm(termName, termStart);
                            ((MainActivity)getActivity()).restartLoader();
                        }
                        else {

                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Don't really need to do anything here, this should close the dialog
                    }
                });

        return builder.create();
    }

    public void addTerm(String termName, Calendar termStart) {
        Calendar endDate = Calendar.getInstance();

        termStart.set(termStart.get(Calendar.YEAR), termStart.get(Calendar.MONTH), 1);
        endDate.setTime(termStart.getTime());

        endDate.add(Calendar.MONTH, 6);
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.getActualMaximum(Calendar.DATE));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String strStartDate =  sdf.format(termStart.getTime());
        String strEndDate = sdf.format(endDate.getTime());

        insertTerm(termName, strStartDate, strEndDate);
    }

    private void insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        Uri termUri = getActivity().getContentResolver().insert(TermProvider.TERMS_URI, values);
        Log.d("MainActivity", "Inserted Term " + termUri.getLastPathSegment());
    }

}
