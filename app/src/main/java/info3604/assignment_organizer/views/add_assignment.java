package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Assignment;
import info3604.assignment_organizer.models.Course;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
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

import java.util.ArrayList;
import java.util.Calendar;
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
        adapter = new ArrayAdapter<String>(this, R.layout.asg_spinner_layout, R.id.text, cList);
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

        String chosenDate = dayFinal+"/"+
                monthFinal+"/"+
                yearFinal+
                " "+hourFinal+":"+
                +minuteFinal;

        Assignment assignment = new Assignment();
        assignment.setDueDate(chosenDate);

        if(assignment.isPastDueDate()){
            Toast.makeText(this,"Please enter a valid date!",Toast.LENGTH_LONG).show();
        }
        else{
            tv_result.setText(chosenDate);
        }
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

        //make it so that notes isn't a required field
        val = assNotes.getText().toString();
        if (val.equals("")){
//            Toast.makeText(this, "Enter notes.", Toast.LENGTH_SHORT).show();
//            return false;
            assNotes.setText("NULL");
        }
        return true;
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

            Intent notificationIntent = new Intent(this, NotifController.class);

            notificationIntent.putExtra("title",assTitle.getText().toString()+" Due"+" "+tv_result.getText().toString());    //Values should be pulled from DB
            notificationIntent.putExtra("content",assTitle.getText().toString()+" Reminder");
            notificationIntent.putExtra("ticker",assTitle.getText().toString());

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Get current time
            String givenDateString = tv_result.getText().toString();
            Log.d("NOAHAVESH","Date: "+givenDateString);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date mDate = sdf.parse(givenDateString);
                long timeInMilliseconds = mDate.getTime();
                Log.d("NOAHAVESH","Future time: "+timeInMilliseconds);
                long millis = System.currentTimeMillis();   //long currentTimeMillis ()-Returns the current time in milliseconds.
                Log.d("NOAHAVESH","Current time: "+millis);
                long seconds = (timeInMilliseconds-millis) / 1000 ;
                if(seconds>3600) seconds -= 3600;
                // -3600 Remind 1 hour before assignment due
                Log.d("NOAHAVESH","Difference in time: "+seconds);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, (int) seconds);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        printDB();
        return result;
    }

    public boolean deleteFromDb(View view){
        boolean result = AC.deleteAssignment(Integer.parseInt(assID.getText().toString()));
        printDB();
        return result;
    }

    // make it so that the user doesn't need to fill in the fields they don't want to update
    public boolean updateDb(View view){
        boolean result = false;

        //Todo: reformat code in such a way that the user doesn't need to manually enter the id but it's detected on view
        String val = assID.getText().toString();//line only for testing purposes

        if (!val.equals("")){
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
        Intent i;
        switch (item.getItemId()){
            case R.id.add_checkpoint:
                startActivity(new Intent(this, add_checkpoint.class));
                break;
            case R.id.add_course:
                i = new Intent(this, add_course.class);
                startActivity(i);
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