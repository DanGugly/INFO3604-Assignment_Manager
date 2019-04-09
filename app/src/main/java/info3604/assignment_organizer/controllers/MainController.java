package info3604.assignment_organizer.controllers;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import info3604.assignment_organizer.models.Assignment;
import android.database.Cursor;
import info3604.assignment_organizer.models.Checkpoint;
import info3604.assignment_organizer.models.Course;

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
    private static final String ASSIGNMENT_STARTDATE = "start_date";
    private static final String ASSIGNMENT_DUEDATE = "due_date";
    private static final String ASSIGNMENT_NOTES = "notes";
    private static final String ASSIGNMENT_PROGRESS = "assignment_progress";

    //Checkpoint Table
    private static final String TABLE_CHECKPOINTS = "checkpoints";
    private static final String CHECKPOINT_ASSIGNMENTID = "assignment_id";
    private static final String CHECKPOINT_CHECKPOINTID = "checkpoint_id";
    private static final String CHECKPOINT_TITLE = "title";
    private static final String CHECKPOINT_STARTDATE = "start_date";
    private static final String CHECKPOINT_DUEDATE = "due_date";
    private static final String CHECKPOINT_NOTES = "notes";
    private static final String CHECKPOINT_PROGRESS = "checkpoint_progress";

    public MainController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public MainController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON; ");
        db.enableWriteAheadLogging();
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
                ASSIGNMENT_STARTDATE + " TEXT NOT NULL, " +
                ASSIGNMENT_DUEDATE + " TEXT NOT NULL, " +
                ASSIGNMENT_NOTES + " TEXT, " +
                ASSIGNMENT_PROGRESS + " INTEGER NOT NULL, " +
                ASSIGNMENT_COURSEID + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + ASSIGNMENT_COURSEID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_CODE + ") ON DELETE CASCADE" +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_CHECKPOINTS+ "(" +
                CHECKPOINT_CHECKPOINTID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CHECKPOINT_TITLE + " TEXT NOT NULL, " +
                CHECKPOINT_STARTDATE + " TEXT NOT NULL, " +
                CHECKPOINT_DUEDATE + " TEXT NOT NULL, " +
                CHECKPOINT_NOTES + " TEXT, " +
                CHECKPOINT_PROGRESS + " INTEGER NOT NULL, " +
                CHECKPOINT_ASSIGNMENTID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + CHECKPOINT_ASSIGNMENTID + ") REFERENCES " + TABLE_ASSIGNMENTS + "(" + ASSIGNMENT_ASSIGNMENTID + ") ON DELETE CASCADE" +
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

    public Cursor getCourseList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_COURSES, null);
        return data;
    }

    public Cursor getAssignmentList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ASSIGNMENTS, null);
        return data;
    }

    public Cursor getAssignmentListHome(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT title, due_date, assignment_progress FROM " + TABLE_ASSIGNMENTS + " WHERE " + ASSIGNMENT_PROGRESS + "='" + 0 +"'" ,null);
        return data;
    }

    public Cursor getCheckpointList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_CHECKPOINTS, null);
        return data;
    }

    public List<Course> getCourseList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_COURSES;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_COURSES + " ORDER BY "+ filter;
        }

        List<Course> courseLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Course course;

        if (cursor.moveToFirst()) {
            do {
                course = new Course();

                course.setCode(cursor.getString(cursor.getColumnIndex(COURSE_CODE)));
                course.setName(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
                course.setCredits(cursor.getInt(cursor.getColumnIndex(COURSE_CREDITS)));
                course.setLevel(cursor.getInt(cursor.getColumnIndex(COURSE_LEVEL)));
                courseLinkedList.add(course);
            } while (cursor.moveToNext());
        }


        return courseLinkedList;
    }

    public ArrayList<String> getCourseCodeList() {
        String query;

        query = "SELECT  * FROM " + TABLE_COURSES;

        ArrayList<String> courseLinkedList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                courseLinkedList.add(cursor.getString(cursor.getColumnIndex(COURSE_CODE)));
            } while (cursor.moveToNext());
        }

        return courseLinkedList;
    }

    public ArrayList<String> getAssignmentStringList() {
        String query;

        query = "SELECT  * FROM " + TABLE_ASSIGNMENTS;

        ArrayList<String> assignmentLinkedList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                assignmentLinkedList.add(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_COURSEID))
                        +":"+cursor.getString(cursor.getColumnIndex(ASSIGNMENT_TITLE)));
            } while (cursor.moveToNext());
        }

        return assignmentLinkedList;
    }

    public ArrayList<Integer> getAssignmentIDList() {
        String query;

        query = "SELECT  * FROM " + TABLE_ASSIGNMENTS;

        ArrayList<Integer> assignmentLinkedList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                assignmentLinkedList.add(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_ASSIGNMENTID)));
            } while (cursor.moveToNext());
        }

        return assignmentLinkedList;
    }

    public List<Assignment> getAssignmentList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_ASSIGNMENTS;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_ASSIGNMENTS + " ORDER BY "+ filter;
        }

        List<Assignment> assignmentLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Assignment assignment;

        if (cursor.moveToFirst()) {
            do {
                assignment = new Assignment();

                assignment.setAssID(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_ASSIGNMENTID)));
                assignment.setTitle(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_TITLE)));
                assignment.setCourseID(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_COURSEID)));
                assignment.setDueDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_DUEDATE)));
                assignment.setStartDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_STARTDATE)));
                assignment.setNotes(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_NOTES)));
                assignment.setProgress(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_PROGRESS)));
                assignmentLinkedList.add(assignment);
            } while (cursor.moveToNext());
        }


        return assignmentLinkedList;
    }

    public List<Checkpoint> getCheckpointList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_CHECKPOINTS;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_CHECKPOINTS + " ORDER BY "+ filter;
        }

        List<Checkpoint> checkpointLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Checkpoint checkpoint;

        if (cursor.moveToFirst()) {
            do {
                checkpoint = new Checkpoint();

                checkpoint.setCheckID(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_CHECKPOINTID)));
                checkpoint.setTitle(cursor.getString(cursor.getColumnIndex(CHECKPOINT_TITLE)));
                checkpoint.setAssignmentID(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_ASSIGNMENTID)));
                checkpoint.setDueDate(cursor.getString(cursor.getColumnIndex(CHECKPOINT_DUEDATE)));
                checkpoint.setStartDate(cursor.getString(cursor.getColumnIndex(CHECKPOINT_STARTDATE)));
                checkpoint.setNotes(cursor.getString(cursor.getColumnIndex(CHECKPOINT_NOTES)));
                checkpoint.setProgress(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_PROGRESS)));
                checkpointLinkedList.add(checkpoint);
            } while (cursor.moveToNext());
        }


        return checkpointLinkedList;
    }

    public Course getCourse(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_COURSES + " WHERE " + COURSE_CODE + "=" + id;
        Cursor cursor = db.rawQuery(query, null);

        Course course = new Course();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            course.setCode(cursor.getString(cursor.getColumnIndex(COURSE_CODE)));
            course.setName(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
            course.setCredits(cursor.getInt(cursor.getColumnIndex(COURSE_CREDITS)));
            course.setLevel(cursor.getInt(cursor.getColumnIndex(COURSE_LEVEL)));
        }

        return course;
    }

    public Assignment getAssignment(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_ASSIGNMENTS + " WHERE " + ASSIGNMENT_ASSIGNMENTID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);

        Assignment assignment = new Assignment();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            assignment.setAssID(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_ASSIGNMENTID)));
            assignment.setTitle(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_TITLE)));
            assignment.setCourseID(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_COURSEID)));
            assignment.setDueDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_DUEDATE)));
            assignment.setStartDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_STARTDATE)));
            assignment.setNotes(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_NOTES)));
            assignment.setProgress(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_PROGRESS)));
        }

        return assignment;
    }

    public Cursor getAssignment(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT title, due_date, notes, course_id FROM " + TABLE_ASSIGNMENTS + " WHERE " + ASSIGNMENT_TITLE + "='" + id+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

//        Assignment assignment = new Assignment();
//        if(cursor.getCount() > 0) {
//            cursor.moveToFirst();
//
//            assignment.setAssID(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_ASSIGNMENTID)));
//            assignment.setTitle(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_TITLE)));
//            assignment.setCourseID(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_COURSEID)));
//            assignment.setDueDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_DUEDATE)));
//            assignment.setStartDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_STARTDATE)));
//            assignment.setNotes(cursor.getString(cursor.getColumnIndex(ASSIGNMENT_NOTES)));
//            assignment.setProgress(cursor.getInt(cursor.getColumnIndex(ASSIGNMENT_PROGRESS)));
//        }
//
//        return assignment;
    }

    public Checkpoint getCheckpoint(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_CHECKPOINTS + " WHERE " + CHECKPOINT_CHECKPOINTID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);

        Checkpoint checkpoint = new Checkpoint();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            checkpoint.setCheckID(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_CHECKPOINTID)));
            checkpoint.setTitle(cursor.getString(cursor.getColumnIndex(CHECKPOINT_TITLE)));
            checkpoint.setAssignmentID(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_ASSIGNMENTID)));
            checkpoint.setDueDate(cursor.getString(cursor.getColumnIndex(CHECKPOINT_DUEDATE)));
            checkpoint.setStartDate(cursor.getString(cursor.getColumnIndex(CHECKPOINT_STARTDATE)));
            checkpoint.setNotes(cursor.getString(cursor.getColumnIndex(CHECKPOINT_NOTES)));
            checkpoint.setProgress(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_PROGRESS)));
        }

        return checkpoint;
    }

    public Cursor getCheckpoint(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  title, due_date, notes FROM " + TABLE_CHECKPOINTS + " WHERE " + CHECKPOINT_TITLE + "='" + id+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

//        Checkpoint checkpoint = new Checkpoint();
//        if(cursor.getCount() > 0) {
//            cursor.moveToFirst();
//
//            checkpoint.setCheckID(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_CHECKPOINTID)));
//            checkpoint.setTitle(cursor.getString(cursor.getColumnIndex(CHECKPOINT_TITLE)));
//            checkpoint.setAssignmentID(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_ASSIGNMENTID)));
//            checkpoint.setDueDate(cursor.getString(cursor.getColumnIndex(CHECKPOINT_DUEDATE)));
//            checkpoint.setStartDate(cursor.getString(cursor.getColumnIndex(CHECKPOINT_STARTDATE)));
//            checkpoint.setNotes(cursor.getString(cursor.getColumnIndex(CHECKPOINT_NOTES)));
//            checkpoint.setProgress(cursor.getInt(cursor.getColumnIndex(CHECKPOINT_PROGRESS)));
//        }
//
//        return checkpoint;
    }

    public Cursor getCheckpointListHome(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT title, due_date, checkpoint_progress FROM " + TABLE_CHECKPOINTS, null);
        return data;
    }
}