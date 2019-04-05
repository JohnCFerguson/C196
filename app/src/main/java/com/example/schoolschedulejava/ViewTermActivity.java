package com.example.schoolschedulejava;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ViewTermActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1002;
    private CursorAdapter cA;
    private Intent intent;
    private long termId;
    private ListView list;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);

        intent = getIntent();

        Log.d("Term ID in View Term: ", intent.getLongExtra("TermId", 0) + "");

        termId = intent.getLongExtra("TermId", 0);

        Cursor termQuery = getContentResolver().query(TermProvider.TERMS_URI, DBOpenHelper.TERMS_COLUMNS,
                DBOpenHelper.TERM_ID + " = " + termId, null, null, null);

        String termName = "";
        String termDates = "";

        try {
            while(termQuery.moveToNext()) {
                termName = termQuery.getString(termQuery.getColumnIndex(DBOpenHelper.TERM_TITLE));
                termDates = termQuery.getString(termQuery.getColumnIndex(DBOpenHelper.TERM_START)) + " - " +
                        termQuery.getString(termQuery.getColumnIndex(DBOpenHelper.TERM_END));
            }
        }
        catch (Exception e) {
            Log.e("ViewTermActivity", "Error moving to next " + e);
        }

        TextView termNameView = findViewById(R.id.termTitle);
        TextView termDatesVew = findViewById(R.id.termDates);

        termNameView.setText(termName);
        termDatesVew.setText(termDates);



        cA = new CourseCursorAdapter(this, null,0);
        list = findViewById(R.id.listView);
        list.setAdapter(cA);
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("ViewTerm List set", id + "");
                openCourseViewForExistingCourse(view, (int)id);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_add_course:
                openAddCourseViewForExistingTerm(findViewById(android.R.id.content));
                break;
            case R.id.action_delete_term:
                deleteTerm();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "termId" +" = ?";
        String[] selectionArgs = {String.valueOf(intent.getLongExtra("TermId", 0))};

        return new CursorLoader(
                this,   // Parent activity context
                CourseWithExtrasProvider.COURSE_WITH_EXTRAS_URI,       // Table to query
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

    public void openAddCourseViewForExistingTerm(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        intent.putExtra("TermId", termId);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public void openCourseViewForExistingCourse(View view, int id) {
        Log.d("Opening course", id+ "");
        TextView courseDates = view.findViewById(R.id.tvCourseDates);
        String dates = courseDates.getText().toString();
        try{
            Intent intent = new Intent(this, ViewCourseActivity.class);
            intent.putExtra("CourseId", id);
            intent.putExtra("Dates", dates);
            startActivityForResult(intent, EDITOR_REQUEST_CODE);
        }
        catch (Exception e){
            e.getMessage();
        }

    }

    public void deleteTerm() {
        View view;
        Boolean courseStillInProgress = false;
        TextView tvStatus;
        String status;
        for(int i = 0; i < list.getCount(); i++) {
            view = list.getAdapter().getView(i, null, null);
            tvStatus = view.findViewById(R.id.tvStatus);
            status = tvStatus.getText().toString();
            Log.d("In Delete Loop", status);
            if(status.equals("Planning to Take") || status.equals(("In Progress"))){
                Log.d("DeleteTerm", "courseSTillInProgress is True");
                courseStillInProgress = true;
            }
        }
        if(courseStillInProgress) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You cannot delete a term that still has active courses!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //no action necessary alert closes on OK, this is all that's necessary
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            final String whereClause = "terms." + DBOpenHelper.TERM_ID + " = ?";
            final String[] selectionArgs = {String.valueOf(termId)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this Term?")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Do nothing, just cancel
                        }
                    })
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getContentResolver().delete(
                                    TermProvider.TERMS_URI, whereClause, selectionArgs
                            );
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("ViewTermActivityResult", requestCode + " " + resultCode + " " + data);
        if(requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
