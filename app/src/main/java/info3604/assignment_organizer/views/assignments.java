package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Assignment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

//Todo: change manually entering the course code to a spinner that reads from the DB

public class assignments extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

        AC = new AssignmentController(this,null,null,1);
        CC = new CourseController(this,null, null,1);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(assignments.this,
                        assignments.this, year, month, day);
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(assignments.this,
                assignments.this, hour, minute, true);
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
                Toast.makeText(this,"COURSE DOESN'T EXIST IN DB",Toast.LENGTH_LONG);
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


        if (checkFields() && val.equals("")){
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
        String dbString = AC.toString();
        Toast.makeText(this, dbString,Toast.LENGTH_LONG).show();
        txt.setText(dbString);
        assTitle.setText("");
        assNotes.setText("");
        courseCode.setText("");
        tv_result.setText("");
    }
}