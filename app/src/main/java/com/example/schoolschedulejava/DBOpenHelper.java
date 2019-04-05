package com.example.schoolschedulejava;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

    //Strings for database information
    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;

    //Terms table constants identifying the values for the term table
    public static final String TABLE_TERMS = "terms";
    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE = "termTitle";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";

    public static final String[] TERMS_COLUMNS =
            {TERM_ID, TERM_TITLE, TERM_START, TERM_END};

    //Courses table constants identifying the values for the course table
    public static final String TABLE_COURSES = "courses";
    public static final String COURSE_ID = "_id";
    public static final String TERMID = "termId";
    public static final String MENTORID = "mentorId";
    public static final String COURSE_TITLE = "courseTitle";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END = "courseEnd";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_NOTES = "courseNotes";

    public static final String[] COURSES_COLUMNS =
            {COURSE_ID, TERMID, MENTORID, COURSE_TITLE, COURSE_START, COURSE_END, COURSE_STATUS,
                    COURSE_NOTES};

    //Mentor table constants identifying values for course mentors
    public static final String TABLE_MENTORS = "mentors";
    public static final String MENTOR_ID = "_id";
    public static final String MENTOR_NAME = "mentorName";
    public static final String MENTOR_PHONE  = "mentorPhone";
    public static final String MENTOR_EMAIL = "mentorEmail";

    public static final String[] MENTORS_COLUMNS =
            {MENTOR_ID, MENTOR_NAME, MENTOR_PHONE, MENTOR_EMAIL};

    //Assessments table constants identifying values for course assessments
    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENT_ID = "_id";
    public static final String COURSEID = "courseId";
    public static final String ASSESSMENT_NAME = "assessmentName";
    public static final String ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_DATE = "assessmentDate";


    public static final String[] ASSESSMENTS_COLUMNS =
            {ASSESSMENT_ID, COURSEID, ASSESSMENT_NAME, ASSESSMENT_TYPE, ASSESSMENT_DATE};

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
                    MENTOR_PHONE + " TEXT)";

    private static final String TABLE_CREATE_COURSES =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERMID + " INTEGER, " +
                    MENTORID + " INTEGER, " +
                    COURSE_TITLE + " TEXT, " +
                    COURSE_START + " TEXT, " +
                    COURSE_END + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_NOTES + " TEXT, " +
                    "FOREIGN KEY (" + TERMID + ") REFERENCES " + TABLE_TERMS+ "(" + TERM_ID + "), " +
                    "FOREIGN KEY (" + MENTORID + ") REFERENCES " + TABLE_MENTORS+ "(" + MENTOR_ID + ")" +
                    ")";

    private static final String TABLE_CREATE_ASSESSMENTS =
            "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSEID + " INTEGER," +
                    ASSESSMENT_NAME + " TEXT, " +
                    ASSESSMENT_TYPE + " TEXT,  " +
                    ASSESSMENT_DATE + " TEXT, " +
                    "FOREIGN KEY (" + COURSEID + ") REFERENCES " + TABLE_ASSESSMENTS + "(" +COURSE_ID +")" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_TERMS);
        db.execSQL(TABLE_CREATE_MENTORS);
        db.execSQL(TABLE_CREATE_ASSESSMENTS);
        db.execSQL(TABLE_CREATE_COURSES);

    Log.d("DBOpenHelper", "Created Database Tables");
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
