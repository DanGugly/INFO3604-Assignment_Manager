package info3604.assignment_organizer.controllers;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Assignment;
import info3604.assignment_organizer.models.Checkpoint;

public class CheckpointController {

    private static final String TABLE_CHECKPOINTS = "checkpoints";

    private static final String COLUMN_ASSIGNMENTID = "assignment_id";
    private static final String COLUMN_CHECKPOINTID = "checkpoint_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_STARTDATE = "start_date";
    private static final String COLUMN_DUEDATE = "due_date";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_PROGRESS = "checkpoint_progress";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private MainController mController;

    public CheckpointController(@Nullable Context context) {
        mController = new MainController(context);
        this.mContext = context;

        try{
            open();
        }
        catch(SQLException e){
            Log.e("CheckpointController", "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mController.getWritableDatabase();
    }

    public void close() {
        mController.close();
    }

    public boolean addCheckpoint(Checkpoint checkpoint){
        mDatabase.beginTransactionNonExclusive();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASSIGNMENTID, checkpoint.getAssignmentID());
        values.put(COLUMN_NOTES, checkpoint.getNotes());
        values.put(COLUMN_TITLE, checkpoint.getTitle());
        values.put(COLUMN_DUEDATE, checkpoint.getDueDate());
        values.put(COLUMN_STARTDATE, checkpoint.getStartDate());
        values.put(COLUMN_PROGRESS, checkpoint.getProgress());
        Log.d("CHECKPOINT CREATION", values.toString());
        long result = mDatabase.insert(TABLE_CHECKPOINTS, null, values);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        Log.d("INSERT RESULT:",result+"");
        return result != -1;
    }

    public boolean updateCheckpoint(Checkpoint checkpoint){
        mDatabase.beginTransactionNonExclusive();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_ASSIGNMENTID, checkpoint.getAssignmentID());
        if(!checkpoint.getNotes().equals(""))
            values.put(COLUMN_NOTES, checkpoint.getNotes());
        if(!checkpoint.getTitle().equals(""))
            values.put(COLUMN_TITLE, checkpoint.getTitle());
        if(!checkpoint.getDueDate().equals(""))
            values.put(COLUMN_DUEDATE, checkpoint.getDueDate());
        if(checkpoint.getProgress() == 0 || checkpoint.getProgress() == 1)
            values.put(COLUMN_PROGRESS, checkpoint.getProgress());
        Log.d("CHECKPOINT UPDATE", values.toString());
        long result = mDatabase.update(TABLE_CHECKPOINTS, values, "checkpoint_id=?", new String[] {Integer.toString(checkpoint.getCheckpointID())});
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return result > 0;
    }

    public boolean deleteCheckpoint(int checkpoint_id){
        mDatabase.beginTransactionNonExclusive();
        //db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_CODE + "=\"" + courseCode + "\";");
        int result = mDatabase.delete(TABLE_CHECKPOINTS,"checkpoint_id=?",new String[]{String.valueOf(checkpoint_id)});
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return result > 0;
    }

    public String toString(){
        mDatabase.beginTransactionNonExclusive();
        String dbString = "" ;
        String query = "SELECT * FROM " + TABLE_CHECKPOINTS ;
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