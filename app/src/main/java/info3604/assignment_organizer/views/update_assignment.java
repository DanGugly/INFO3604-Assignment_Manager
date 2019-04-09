package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.controllers.NotifController;
import info3604.assignment_organizer.models.Assignment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class update_assignment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    Button b_pick;
    TextInputEditText tv_result; //Date and time text field
    TextInputEditText assTitle;
    TextInputEditText assNotes;

    AssignmentController AC;
    CourseController CC;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    int assignmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_assignment);

        b_pick = (Button) findViewById(R.id.b_pick);

        tv_result = (TextInputEditText) findViewById(R.id.tv_result);
        assTitle = (TextInputEditText) findViewById(R.id.assTitle);
        assNotes = (TextInputEditText) findViewById(R.id.assNotes);

        AC = new AssignmentController(this);
        CC = new CourseController(this);

        try {
            //get intent to get person id
            assignmentID = getIntent().getIntExtra("ASSIGNMENT_ID",0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        b_pick.setOnClickListener(new View.OnClickListener() {  //Button for setting date and time
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(update_assignment.this,
                        update_assignment.this, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(update_assignment.this,
                update_assignment.this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1){ //This is date format returned when clicking set date & time button in Add Assignment
        hourFinal = i;
        minuteFinal = i1;

        String minute= Integer.toString(minuteFinal), hour = Integer.toString(hourFinal);
        if (hourFinal<10){
            hour = "0"+hour;
        }
        if (minuteFinal<10){
            minute = "0"+minute;
        }
        String chosenDate = dayFinal+"/"+
                monthFinal+"/"+
                yearFinal+
                " "+hour+":"+
                minute;
        Assignment assignment = new Assignment();
        assignment.setDueDate(chosenDate);

        if(assignment.isPastDueDate()){
            Toast.makeText(this,"Please enter a valid date!",Toast.LENGTH_LONG).show();
        }
        else{
            tv_result.setText(chosenDate);
        }
    }

    private boolean checkFields(){

        boolean filled = false;

        String val = tv_result.getText().toString();
        if (!val.equals("")){ filled = true; }

        val = assTitle.getText().toString();
        if (!val.equals("")){ filled = true; }

        val = assNotes.getText().toString();
        if (!val.equals("")){ filled = true; }

        if(!filled)
            Toast.makeText(this,"Please fill in data in one of the fields",Toast.LENGTH_LONG).show();

        return filled;
    }

    public void updateDb(View view){
        boolean result = false;

        if (checkFields() && assignmentID != 0){

            Assignment assignment = new Assignment(
                    "",
                    assTitle.getText().toString().trim(),
                    tv_result.getText().toString().trim(),
                    assNotes.getText().toString().trim()
            );

            assignment.setAssID(assignmentID);
            result = AC.updateAssignment(assignment);

        }
        if (result) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent(this, NotifController.class);

            notificationIntent.putExtra("title", "Assignment: " + assTitle.getText().toString());    //Values should be pulled from DB
            notificationIntent.putExtra("content", "Notes: "+assNotes.getText().toString() + " Reminder");
            notificationIntent.putExtra("ticker", assTitle.getText().toString());

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Get current time
            String givenDateString = tv_result.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");


            try {
                Date mDate = sdf.parse(givenDateString);
                long timeInMilliseconds = mDate.getTime();
                long millis = System.currentTimeMillis();   //long currentTimeMillis ()-Returns the current time in milliseconds.
                long seconds = (timeInMilliseconds - millis) / 1000;               //Divide millis by 1000 to get the number of seconds.
                if(seconds>3600){
                    seconds = seconds - 3600;
                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, (int) seconds);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(this, view_assignments.class);
            startActivity(i);
        }
    }
}
