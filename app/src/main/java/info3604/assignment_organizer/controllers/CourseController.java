package info3604.assignment_organizer.controllers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Course;

public class CourseController extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder.db";
    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CREDITS = "credits";
    private static final String COLUMN_LEVEL = "level";

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
        Log.d("COURSE CREATION", values.toString());
        long result = db.insert(TABLE_COURSES, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        Log.d("INSERT RESULT:",result+"");
        return result != -1;
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

    public boolean updateCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, course.getCode());
        values.put(COLUMN_NAME, course.getName());
        values.put(COLUMN_CREDITS, course.getCredits());
        values.put(COLUMN_LEVEL, course.getLevel());
        Log.d("COURSE UPDATE", values.toString());
        long result = db.update(TABLE_COURSES, values, "code=?", new String[] {course.getCode()});
        return result > 0;
    }

    public boolean deleteCourse(String courseCode){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_CODE + "=\"" + courseCode + "\";");
        int result = db.delete(TABLE_COURSES,"code=?",new String[]{courseCode});
        return result > 0;
    }

    public String toString(){
        String dbString = "" ;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COURSES ;
        Cursor allRows = db.rawQuery(query,null);
        /*
        while(c.moveToNext()){
            dbString += c.toString() + "\n";
        }
        */
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
        /*
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name")) != null){
                dbString += c.getString(c.getColumnIndex("name"));
                dbString += "\n";
            }
        }
        */
        allRows.close();
        db.close();
        return dbString;
    }


}
