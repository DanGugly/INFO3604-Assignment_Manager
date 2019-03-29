package info3604.assignment_organizer.controllers;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import androidx.annotation.Nullable;
import android.database.Cursor;
import info3604.assignment_organizer.models.Checkpoint;

public class MainController extends SQLiteOpenHelper{

    //Database name and version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder.db";

    //Course Table
    private static final String TABLE_COURSES = "courses";
    private static final String COURSE_CODE = "code";
    private static final String COURSE_NAME = "name";
    private static final String COURSE_CREDITS = "credits";
    private static final String COURSE_LEVEL = "level";

    //Assignment Table
    private static final String TABLE_ASSIGNMENTS = "assignments";
    private static final String ASSIGNMENT_ASSIGNMENTID = "assignment_id";
    private static final String ASSIGNMENT_COURSEID = "course_id";
    private static final String ASSIGNMENT_TITLE = "title";
    private static final String ASSIGNMENT_STARTDATE = "start_date";
    private static final String ASSIGNMENT_DUEDATE = "due_date";
    private static final String ASSIGNMENT_NOTES = "notes";
    private static final String ASSIGNMENT_PROGRESS = "assignment_progress";

    //Checkpoint Table
    private static final String TABLE_CHECKPOINTS = "checkpoints";
    private static final String CHECKPOINT_ASSIGNMENTID = "assignment_id";
    private static final String CHECKPOINT_CHECKPOINTID = "checkpoint_id";
    private static final String CHECKPOINT_TITLE = "title";
    private static final String CHECKPOINT_STARTDATE = "start_date";
    private static final String CHECKPOINT_DUEDATE = "due_date";
    private static final String CHECKPOINT_NOTES = "notes";
    private static final String CHECKPOINT_PROGRESS = "checkpoint_progress";

    public MainController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public MainController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON; ");
        db.enableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_COURSES + "(" +
                COURSE_CODE + " TEXT PRIMARY KEY, " +
                COURSE_NAME + " TEXT NOT NULL, " +
                COURSE_CREDITS + " INTEGER NOT NULL, " +
                COURSE_LEVEL + " INTEGER NOT NULL);";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_ASSIGNMENTS+ "(" +
                ASSIGNMENT_ASSIGNMENTID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                ASSIGNMENT_TITLE + " TEXT NOT NULL, " +
                ASSIGNMENT_STARTDATE + " TEXT NOT NULL, " +
                ASSIGNMENT_DUEDATE + " TEXT NOT NULL, " +
                ASSIGNMENT_NOTES + " TEXT, " +
                ASSIGNMENT_PROGRESS + " INTEGER NOT NULL, " +
                ASSIGNMENT_COURSEID + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + ASSIGNMENT_COURSEID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_CODE + ") ON DELETE CASCADE" +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_CHECKPOINTS+ "(" +
                CHECKPOINT_CHECKPOINTID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CHECKPOINT_TITLE + " TEXT NOT NULL, " +
                CHECKPOINT_STARTDATE + " TEXT NOT NULL, " +
                CHECKPOINT_DUEDATE + " TEXT NOT NULL, " +
                CHECKPOINT_NOTES + " TEXT, " +
                CHECKPOINT_PROGRESS + " INTEGER NOT NULL, " +
                CHECKPOINT_ASSIGNMENTID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + CHECKPOINT_ASSIGNMENTID + ") REFERENCES " + TABLE_ASSIGNMENTS + "(" + ASSIGNMENT_ASSIGNMENTID + ") ON DELETE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CHECKPOINTS);
        onCreate(db);
    }
    public Cursor getCourseList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_COURSES, null);
        return data;
    }

    public Cursor getAssignmentList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ASSIGNMENTS, null);
        return data;
    }

    public Cursor getCheckpointList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_CHECKPOINTS, null);
        return data;
    }
}