package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Assignment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

//Todo: change manually entering the course code to a spinner that reads from the DB

public class add_assignment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button save;
    Button b_pick;
    TextInputEditText tv_result; //Date and time text field
    TextInputEditText courseCode;
    TextInputEditText assTitle;
    TextInputEditText assNotes;
    TextInputEditText assID;
    TextView txt;

    AssignmentController AC;
    CourseController CC;

    int day, month, year, hour, minute;
    float x1, x2, y1, y2;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);

        save = (Button) findViewById(R.id.save);
        b_pick = (Button) findViewById(R.id.b_pick);

        txt = (TextView) findViewById(R.id.placeholder);
        tv_result = (TextInputEditText) findViewById(R.id.tv_result);
        assID = (TextInputEditText) findViewById(R.id.assID);
        courseCode = (TextInputEditText) findViewById(R.id.courseCode);
        assTitle = (TextInputEditText) findViewById(R.id.assTitle);
        assNotes = (TextInputEditText) findViewById(R.id.assNotes);

        AC = new AssignmentController(this);
        CC = new CourseController(this);

        save.setOnClickListener(new View.OnClickListener() {  //What should happen when save button clicked?
            @Override
            public void onClick(View v) {                     //Maybe call controller method pass assignment info, let controller / model save to database
                // For now I display info in toast
                Toast.makeText(getApplicationContext(),
                        "Name: "+courseCode.getText()+
                                " Title: " +assTitle.getText()+
                                " Date/Time: "+tv_result.getText()+
                                " Notes: "+assNotes.getText(),
                        Toast.LENGTH_LONG).show();

                //Can create intent here to go to view assignment page?

                addToDb();
            }
        });

        b_pick.setOnClickListener(new View.OnClickListener() {  //Button for setting date and time
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(add_assignment.this,
                        add_assignment.this, year, month, day);
                datePickerDialog.show();
            }
        });

        printDB();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(add_assignment.this,
                add_assignment.this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1){ //This is date format returned when clicking set date & time button in Add Assignment
        hourFinal = i;
        minuteFinal = i1;

        tv_result.setText(dayFinal+"/"+
                monthFinal+"/"+
                yearFinal+
                " "+hourFinal+":"+
                +minuteFinal
        );
    }

    private boolean checkFields(){

        String val = tv_result.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter due date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        val = assTitle.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter assignment name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        val = courseCode.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter course code.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Todo: make it so that notes isn't a required field
        val = assNotes.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter notes.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean addToDb(){
        boolean result = false;
        if (checkFields()){
            Assignment assignment = new Assignment(
                    courseCode.getText().toString(),
                    assTitle.getText().toString(),
                    tv_result.getText().toString(),
                    assNotes.getText().toString()
            );
            if(CC.courseExistsInDb(courseCode.getText().toString()))
                result = AC.addAssignment(assignment);
            else
                Toast.makeText(this,"COURSE DOESN'T EXIST IN DB",Toast.LENGTH_LONG).show();
        }
        Log.d("RESULT:",result+" ");
        printDB();
        return result;
    }

    public boolean deleteFromDb(View view){
        boolean result = AC.deleteAssignment(Integer.parseInt(assID.getText().toString()));
        printDB();
        return result;
    }

    //Todo: make it so that the user doesn't need to fill in the fields they don't want to update
    public boolean updateDb(View view){
        boolean result = false;

        //Todo: reformat code in such a way that the user doesn't need to manually enter the id but it's detected on view
        String val = assID.getText().toString();//line only for testing purposes


        if (checkFields() && !val.equals("")){
            Assignment assignment = new Assignment(
                    courseCode.getText().toString(),
                    assTitle.getText().toString(),
                    tv_result.getText().toString(),
                    assNotes.getText().toString()
            );
            assignment.setAssID(Integer.parseInt(val));
            result = AC.updateAssignment(assignment);
        }
        Log.d("RESULT:",result+"");
        printDB();
        return result;
    }

    public void printDB(){
        if(AC != null) {
            String dbString = AC.toString();
            Toast.makeText(this, dbString,Toast.LENGTH_LONG).show();
            txt.setText(dbString);
            assTitle.setText("");
            assNotes.setText("");
            courseCode.setText("");
            tv_result.setText("");
        }


    }

    @Override   //Builds main_menu.xml from menu resourse in res
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override   //Getting which menu item is selected and creating toasts when they are
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_course:
                startActivity(new Intent(this, add_course.class));
                break;
            case R.id.start_page:
                startActivity(new Intent(this, Main.class));
                break;
            case R.id.course_view:
                startActivity(new Intent(this, View_Course.class));
                break;
            case R.id.assignment_view:
                startActivity(new Intent(this, View_Assignment.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1<x2){
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
        return false;
    }
}