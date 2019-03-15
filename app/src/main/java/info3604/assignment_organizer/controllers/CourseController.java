package info3604.assignment_organizer.controllers;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Course;

public class CourseController {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder.db";
    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CREDITS = "credits";
    private static final String COLUMN_LEVEL = "level";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private MainController mController;

    public CourseController(Context context){
        mController = new MainController(context);
        this.mContext = context;
        try{
            open();
        }
        catch (SQLException e){
            Log.e("CourseController", "SQLException on opening database " + e.getMessage());
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
        String query = "CREATE TABLE " + TABLE_COURSES + "(" +
                COLUMN_CODE + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_CREDITS + " INTEGER NOT NULL, " +
                COLUMN_LEVEL + " INTEGER NOT NULL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }
    */

    public boolean addCourse(Course course){
        mDatabase.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, course.getCode());
        values.put(COLUMN_NAME, course.getName());
        values.put(COLUMN_CREDITS, course.getCredits());
        values.put(COLUMN_LEVEL, course.getLevel());
        Log.d("COURSE CREATION", values.toString());
        long result = mDatabase.insert(TABLE_COURSES, null, values);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

        Log.d("INSERT RESULT:",result+"");
        return result != -1;
    }

    public boolean updateCourse(Course course){
        mDatabase.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, course.getCode());
        values.put(COLUMN_NAME, course.getName());
        values.put(COLUMN_CREDITS, course.getCredits());
        values.put(COLUMN_LEVEL, course.getLevel());
        Log.d("COURSE UPDATE", values.toString());
        long result = mDatabase.update(TABLE_COURSES, values, "code=?", new String[] {course.getCode()});
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return result > 0;
    }

    public boolean deleteCourse(String courseCode){
        mDatabase.beginTransaction();
        //db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_CODE + "=\"" + courseCode + "\";");
        int result = mDatabase.delete(TABLE_COURSES,"code=?",new String[]{courseCode});
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

        return result > 0;
    }

    public boolean courseExistsInDb(String courseCode){
        mDatabase.beginTransaction();
        String[] columns = { COLUMN_CODE };
        String selection = COLUMN_CODE + " =?";
        String[] selectionArgs = { courseCode };
        String limit = "1";

        Cursor cursor = mDatabase.query(TABLE_COURSES, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return exists;
    }

    public String toString(){
        String dbString = "" ;
        mDatabase.beginTransaction();
        String query = "SELECT * FROM " + TABLE_COURSES ;
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
