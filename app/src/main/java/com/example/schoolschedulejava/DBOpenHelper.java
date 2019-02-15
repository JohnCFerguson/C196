package com.example.schoolschedulejava;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    //Strings for database information
    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABSE_VERSION = 1;

    //Terms table constants identifying the values for the term table
    private static final String TABLE_TERMS = "terms";
    private static final String TERM_ID = "_id";
    private static final String TERM_TITLE = "termTitle";
    private static final String TERM_START = "termStart";
    private static final String TERM_END = "termEnd";

    //Courses table constants identifying the values for the course table
    private static final String TABLE_COURSES = "courses";
    private static final String COURSE_ID = "_id";
    private static final String TERMID = "termId";
    private static final String MENTORID = "mentorId";
    private static final String COURSE_TITLE = "courseTitle";
    private static final String COURSE_START = "courseStart";
    private static final String COURSE_END = "courseEnd";
    private static final String COURSE_STATUS = "courseStatus";
    private static final String COURSE_ASSESSMENTS = "courseAssessments";
    private static final String COURSE_NOTES = "courseNotes";

    //Mentor table constants identifying values for course mentors
    private static final String TABLE_MENTORS = "mentors";
    private static final String MENTOR_ID = "_id";
    private static final String MENTOR_NAME = "mentorName";
    private static final String MENTOR_PHONE  = "mentorPhone";
    private static final String MENTOR_EMAIL = "mentorEmail";

    //Assessments table constants identifying values for course assessments
    private static final String TABLE_ASSESSMENTS = "assessments";
    private static final String ASSESSMENT_ID = "_id";
    private static final String ASSESSMENT_NAME = "assessmentName";
    private static final String ASSESSMENT_TYPE = "assessmentType";

    private static final String TABLE_CREATE_TERMS =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_TITLE + " TEXT, " +
                    TERM_START + " TEXT, " +
                    TERM_END + " TEXT)";

    private static final String TABLE_CREATE_MENTORS =
            "CREATE TABLE " + TABLE_MENTORS + " (" +
                    MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MENTOR_NAME + " TEXT, " +
                    MENTOR_EMAIL + " TEXT, " +
                    MENTOR_PHONE + "T"CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_NAME + " TEXT, " +
                    ASSESSMENT_TYPE + " TEXT)";

    private static final String TABLE_CREATE_COURSES =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERMID + " TEXT, " +
                    MENTORID + " TEXT, " +
                    COURSE_TITLE + " TEXT, " +
                    COURSE_START + " TEXT, " +
                    COURSE_END + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_ASSESSMENTS + " TEXT," +
                    COURSE_NOTES + " TEXT, " +
                    "FOREIGN KEY (" + TERMID + ") REFERENCES " + TABLE_TERMS+ "(" + TERM_ID + "), " +
                    "FOREIGN KEY (" + MENTORID + ") REFERENCES " + TABLE_MENTORS+ "(" + MENTOR_ID + ")" +
                    ")";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_TERMS);
        db.execSQL(TABLE_CREATE_MENTORS);
        db.execSQL(TABLE_CREATE_ASSESSMENTS);
        db.execSQL(TABLE_CREATE_COURSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }
}
