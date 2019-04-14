package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.controllers.NotifController;
import info3604.assignment_organizer.models.Assignment;
import info3604.assignment_organizer.models.Course;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class add_assignment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {

    Button save;
    Button b_pick;

    TextInputEditText tv_result; //Date and time text field
    String courseCode = "Choose Course Code";
    TextInputEditText assTitle;
    TextInputEditText assNotes;

    TextInputLayout tv_result_til;
    TextInputLayout courseCode_til;
    TextInputLayout assTitle_til;

    AssignmentController AC;
    CourseController CC;
    MainController MC;

    Spinner spinner;
    Context context;

    ArrayAdapter<String> adapter;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);

        save = (Button) findViewById(R.id.save);
        b_pick = (Button) findViewById(R.id.b_pick);

        tv_result = (TextInputEditText) findViewById(R.id.tv_result);
        assTitle = (TextInputEditText) findViewById(R.id.assTitle);
        assNotes = (TextInputEditText) findViewById(R.id.assNotes);

        tv_result_til = (TextInputLayout)findViewById(R.id.tv_result_til);
        courseCode_til = (TextInputLayout)findViewById(R.id.courseCode_til);
        assTitle_til = (TextInputLayout)findViewById(R.id.assTitle_til);

        AC = new AssignmentController(this);
        CC = new CourseController(this);
        MC = new MainController(this);

        context = add_assignment.this;

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayList<String> cList = MC.getCourseCodeList();
        cList.add(0,"Choose Course Code");
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, cList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        save.setOnClickListener(new View.OnClickListener() {  //What should happen when save button clicked?
            @Override
            public void onClick(View v) {
                if(addToDb()){
                    adapter.notifyDataSetChanged();
                    Intent i = new Intent(context, view_assignments.class);
                    startActivity(i);
                }
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

    }

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

        String val;

        val = courseCode;
        if (val.equals("Choose Course Code")){
            courseCode_til.setError("Course required");
            return false;
        }
        else{
            courseCode_til.setError(null);
            courseCode_til.setErrorEnabled(false);
        }

        val = assTitle.getText().toString();
        if (val.equals("")){
            assTitle_til.setError("Title required");
            return false;
        }
        else{
            assTitle_til.setError(null);
            assTitle_til.setErrorEnabled(false);
        }

        val = tv_result.getText().toString();
        if (val.equals("")){
            tv_result_til.setError("Date required");
            return false;
        }
        else{
            tv_result_til.setError(null);
            tv_result_til.setErrorEnabled(false);
        }

        return true;
    }

    public boolean addToDb(){
        boolean result = false;
        if (checkFields()){
            Assignment assignment = new Assignment(
                    courseCode.trim(),
                    assTitle.getText().toString().trim(),
                    tv_result.getText().toString().trim(),
                    assNotes.getText().toString().trim()
            );
            if(CC.courseExistsInDb(courseCode.trim()))
                result = AC.addAssignment(assignment);

            Log.d("RESULT:",result+" ");
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
            SimpleDateFormat sdf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            }
            try {
                Date mDate = sdf.parse(givenDateString);
                long timeInMilliseconds = mDate.getTime();
                //long millis = System.currentTimeMillis();   //long currentTimeMillis ()-Returns the current time in milliseconds.
                //long seconds = (timeInMilliseconds - millis) / 1000;               //Divide millis by 1000 to get the number of seconds.

                if(timeInMilliseconds>3600000){
                    timeInMilliseconds = timeInMilliseconds - 3600000;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timeInMilliseconds);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).toString().equals("Choose Course Code")){
            //Do nothing
        }
        else{
            courseCode = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String init = "Choose Course Code";
        int spinnerPosition = adapter.getPosition(init);
        spinner.setSelection(spinnerPosition);
    }
}
