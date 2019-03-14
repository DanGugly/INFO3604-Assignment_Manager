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

    public boolean courseExistsInDb(String courseCode){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = { COLUMN_CODE };
        String selection = COLUMN_CODE + " =?";
        String[] selectionArgs = { courseCode };
        String limit = "1";

        Cursor cursor = db.query(TABLE_COURSES, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public String toString(){
        String dbString = "" ;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COURSES ;
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