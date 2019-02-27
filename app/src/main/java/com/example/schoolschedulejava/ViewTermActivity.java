package com.example.schoolschedulejava;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class ViewTermActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{


    private CursorAdapter cA;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);

        intent = getIntent();

        Log.d("Term ID in View Term: ", intent.getLongExtra("TermId", 0) + "");

        cA = new CourseCursorAdapter(this, null,0);
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cA);

        getLoaderManager().initLoader(0, null, this);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = DBOpenHelper.TERMID + " = ?";
        String[] selectionArgs = {String.valueOf(intent.getLongExtra("TermId", 0))};

        return new CursorLoader(
                this,   // Parent activity context
                CourseProvider.COURSES_URI,       // Table to query
                null,     // Projection to return
                selection,            // No selection clause
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
}
