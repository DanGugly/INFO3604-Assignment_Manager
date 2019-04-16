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

        if(!assignment.getNotes().equals(""))
            values.put(COLUMN_NOTES, assignment.getNotes());
        if(!assignment.getTitle().equals(""))
            values.put(COLUMN_TITLE, assignment.getTitle());
        if(!assignment.getDueDate().equals(""))
            values.put(COLUMN_DUEDATE, assignment.getDueDate());
        if(assignment.getProgress() == 0 || assignment.getProgress() == 1 || assignment.getProgress() == -1)
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