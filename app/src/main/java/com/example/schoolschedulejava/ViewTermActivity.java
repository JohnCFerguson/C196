package com.example.schoolschedulejava;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewTermActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1002;
    private CursorAdapter cA;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);

        intent = getIntent();

        Log.d("Term ID in View Term: ", intent.getLongExtra("TermId", 0) + "");

        String termId = intent.getLongExtra("TermId", 0) + "";

        Cursor termQuery = getContentResolver().query(TermProvider.TERMS_URI, DBOpenHelper.TERMS_COLUMNS,
                DBOpenHelper.TERM_ID + " = " + termId, null, null, null);

        String termName = "";
        String termDates = "";

        while(termQuery.moveToNext()) {
            termName = termQuery.getString(termQuery.getColumnIndex(DBOpenHelper.TERM_TITLE));
            termDates = termQuery.getString(termQuery.getColumnIndex(DBOpenHelper.TERM_START)) + " - " +
                termQuery.getString(termQuery.getColumnIndex(DBOpenHelper.TERM_END));
        }

        TextView termNameView = findViewById(R.id.termTitle);
        TextView termDatesVew = findViewById(R.id.termDates);

        termNameView.setText(termName);
        termDatesVew.setText(termDates);

        cA = new CourseCursorAdapter(this, null,0);
        ListView list = findViewById(R.id.listView);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = {String.valueOf(intent.getLongExtra("TermId", 0))};

        return new CursorLoader(
                this,   // Parent activity context
                CourseWithMentorProvider.COURSE_WITH_MENTOR_URI,       // Table to query
                null,     // Projection to return
                null,            // No selection clause
                selectionArgs,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cA.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cA.swapCursor(null);
    }

    public void openAddCourseViewForExistingTerm(View view, long termId) {
        Intent intent = new Intent(this, ViewTermActivity.class);
        intent.putExtra("TermId", termId);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }
}
