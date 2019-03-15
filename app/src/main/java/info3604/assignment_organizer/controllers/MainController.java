package info3604.assignment_organizer.controllers;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import androidx.annotation.Nullable;

public class MainController extends SQLiteOpenHelper{

    //Database name and version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder.db";

    //Course Table
    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CREDITS = "credits";
    private static final String COLUMN_LEVEL = "level";

    //Assignment Table
    private static final String TABLE_ASSIGNMENTS = "assignments";
    private static final String COLUMN_ASSIGNMENTID = "assignment_id";
    private static final String COLUMN_COURSEID = "course_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DUEDATE = "due_date";
    private static final String COLUMN_NOTES = "notes";

    public MainController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public MainController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_COURSES + "(" +
                COLUMN_CODE + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_CREDITS + " INTEGER NOT NULL, " +
                COLUMN_LEVEL + " INTEGER NOT NULL);";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_ASSIGNMENTS+ "(" +
                COLUMN_ASSIGNMENTID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DUEDATE + " TEXT NOT NULL, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_COURSEID + " TEXT REFERENCES " + TABLE_COURSES + "(code) ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }
}
