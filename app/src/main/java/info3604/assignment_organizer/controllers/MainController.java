package info3604.assignment_organizer.controllers;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import androidx.annotation.Nullable;
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
    private static final String ASSIGNMENT_DUEDATE = "due_date";
    private static final String ASSIGNMENT_NOTES = "notes";

    //Checkpoint Table
    private static final String TABLE_CHECKPOINTS = "checkpoints";
    private static final String CHECKPOINT_ASSIGNMENTID = "assignment_id";
    private static final String CHECKPOINT_CHECKPOINTID = "checkpoint_id";
    private static final String CHECKPOINT_TITLE = "title";
    private static final String CHECKPOINT_DUEDATE = "due_date";
    private static final String CHECKPOINT_NOTES = "notes";

    public MainController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public MainController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
                ASSIGNMENT_DUEDATE + " TEXT NOT NULL, " +
                ASSIGNMENT_NOTES + " TEXT, " +
                ASSIGNMENT_COURSEID + " TEXT REFERENCES " + TABLE_COURSES + "(code) ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_CHECKPOINTS+ "(" +
                CHECKPOINT_CHECKPOINTID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CHECKPOINT_TITLE + " TEXT NOT NULL, " +
                CHECKPOINT_DUEDATE + " TEXT NOT NULL, " +
                CHECKPOINT_NOTES + " TEXT, " +
                CHECKPOINT_ASSIGNMENTID + " INTEGER REFERENCES " + TABLE_ASSIGNMENTS + "(assignment_id) ON UPDATE CASCADE" +
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
}
