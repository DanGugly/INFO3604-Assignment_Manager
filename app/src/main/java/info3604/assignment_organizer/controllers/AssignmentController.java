package info3604.assignment_organizer.controllers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Assignment;

public class AssignmentController extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder.db";
    private static final String TABLE_ASSIGNMENTS = "assignments";
    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_ASSIGNMENTID = "assignment_id";
    private static final String COLUMN_COURSEID = "course_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DUEDATE = "due_date";
    private static final String COLUMN_NOTES = "notes";

    public AssignmentController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ASSIGNMENTS+ "(" +
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
        onCreate(db);
    }

    public boolean addAssignment(Assignment assignment){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSEID, assignment.getCourseID());
        values.put(COLUMN_NOTES, assignment.getNotes());
        values.put(COLUMN_TITLE, assignment.getTitle());
        values.put(COLUMN_DUEDATE, assignment.getDueDate());
        Log.d("ASSIGNMENT CREATION", values.toString());
        long result = db.insert(TABLE_ASSIGNMENTS, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        Log.d("INSERT RESULT:",result+"");
        return result != -1;
    }

    public boolean updateAssignment(Assignment assignment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSEID, assignment.getCourseID());
        values.put(COLUMN_NOTES, assignment.getNotes());
        values.put(COLUMN_TITLE, assignment.getTitle());
        values.put(COLUMN_DUEDATE, assignment.getDueDate());
        Log.d("ASSIGNMENT UPDATE", values.toString());

        //Todo: might need to be changed
        long result = db.update(TABLE_ASSIGNMENTS, values, "assignment_id=?", new String[] {Integer.toString(assignment.getAssignmentID())});

        return result > 0;
    }

    public boolean deleteAssignment(int assignment_id){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_CODE + "=\"" + courseCode + "\";");
        int result = db.delete(TABLE_ASSIGNMENTS,"assignment_id=?",new String[]{String.valueOf(assignment_id)});
        return result > 0;
    }

    public String toString(){
        String dbString = "" ;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ASSIGNMENTS ;
        Cursor allRows = db.rawQuery(query,null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    dbString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                dbString += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        db.close();
        return dbString;
    }
}