package info3604.assignment_organizer.controllers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Course;

public class CourseController extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder.db";
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREDITS = "credits";
    public static final String COLUMN_LEVEL = "level";

    public CourseController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_COURSES + "(" +
                COLUMN_CODE + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CREDITS + " INTEGER, " +
                COLUMN_LEVEL + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }

    public boolean addCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, course.getCode());
        values.put(COLUMN_NAME, course.getName());
        values.put(COLUMN_CREDITS, course.getCredits());
        values.put(COLUMN_LEVEL, course.getLevel());
        Log.d("Course CREATION", values.toString());
        long result = db.insert(TABLE_COURSES, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        Log.d("INSERT RESULT:",result+"");
        if(result == -1) {

            return false;
        }
        return true;
        /*
        if(db.insert(TABLE_COURSES, null, values) != -1){
            Log.d("INSERTION","The course is being inserted");
            db.close();
            return true;
        }
        db.close();
        Log.d("INSERTION","The course wasn't inserted");
        return false;
        */
    }

    public void deleteCourse(String courseCode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_CODE + "=\"" + courseCode + "\";");
    }

    public String toString(){
        String dbString = "" ;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COURSES + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name")) != null){
                dbString += c.getString(c.getColumnIndex("name"));
                dbString += "\n";
            }
        }
        db.close();
        return dbString;
    }


}
