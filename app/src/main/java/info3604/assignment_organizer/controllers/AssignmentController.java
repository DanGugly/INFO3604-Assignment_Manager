package info3604.assignment_organizer.controllers;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Assignment;

public class AssignmentController{

    private static final String TABLE_ASSIGNMENTS = "assignments";
    
    private static final String COLUMN_ASSIGNMENTID = "assignment_id";
    private static final String COLUMN_COURSEID = "course_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_STARTDATE = "start_date";
    private static final String COLUMN_DUEDATE = "due_date";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_PROGRESS = "assignment_progress";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private MainController mController;

    public AssignmentController(@Nullable Context context) {
        mController = new MainController(context);
        this.mContext = context;

        try{
            open();
        }
        catch(SQLException e){
            Log.e("AssignmentController", "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mController.getWritableDatabase();
    }

    public void close() {
        mController.close();
    }

    /*
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
    */

    public boolean addAssignment(Assignment assignment){
        Log.d("ASSIGNMENT DATE:",""+assignment.getStartDate());
        mDatabase.beginTransactionNonExclusive();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSEID, assignment.getCourseID());
        values.put(COLUMN_NOTES, assignment.getNotes());
        values.put(COLUMN_TITLE, assignment.getTitle());
        values.put(COLUMN_STARTDATE, assignment.getStartDate());
        values.put(COLUMN_DUEDATE, assignment.getDueDate());
        values.put(COLUMN_PROGRESS, assignment.getProgress());
        Log.d("ASSIGNMENT CREATION", values.toString());
        long result = mDatabase.insert(TABLE_ASSIGNMENTS, null, values);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        Log.d("INSERT RESULT:",result+"");
        return result != -1;
    }

    public boolean updateAssignment(Assignment assignment){
        mDatabase.beginTransactionNonExclusive();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_COURSEID, assignment.getCourseID());
        values.put(COLUMN_NOTES, assignment.getNotes());
        values.put(COLUMN_TITLE, assignment.getTitle());
        values.put(COLUMN_DUEDATE, assignment.getDueDate());
        values.put(COLUMN_PROGRESS, assignment.getProgress());
        Log.d("ASSIGNMENT UPDATE", values.toString());

        long result = mDatabase.update(TABLE_ASSIGNMENTS, values, "assignment_id=?", new String[] {Integer.toString(assignment.getAssignmentID())});
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return result > 0;
    }

    public boolean deleteAssignment(int assignment_id){
        mDatabase.beginTransactionNonExclusive();
        //db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_CODE + "=\"" + courseCode + "\";");
        int result = mDatabase.delete(TABLE_ASSIGNMENTS,"assignment_id=?",new String[]{String.valueOf(assignment_id)});
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return result > 0;
    }

    public boolean assignmentExistsInDb(String assignmentID){
        mDatabase.beginTransactionNonExclusive();
        Log.d("AsgID",assignmentID);
        String selectString = "SELECT * FROM " + TABLE_ASSIGNMENTS + " WHERE " + COLUMN_ASSIGNMENTID + " =?";
        Cursor cursor = mDatabase.rawQuery(selectString, new String[] {assignmentID});
        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }
        cursor.close();
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return hasObject;
        /*
        String[] columns = { COLUMN_ASSIGNMENTID };
        String selection = COLUMN_ASSIGNMENTID + " =?";
        String[] selectionArgs = { assignmentID };
        String limit = "1";

        Cursor cursor = mDatabase.query(TABLE_COURSES, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return exists;
        */
    }

    public String toString(){
        mDatabase.beginTransactionNonExclusive();
        String dbString = "" ;
        String query = "SELECT * FROM " + TABLE_ASSIGNMENTS ;
        Cursor allRows = mDatabase.rawQuery(query,null);
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
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return dbString;
    }
}