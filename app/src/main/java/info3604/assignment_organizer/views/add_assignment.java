package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class add_assignment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button save;
    Button b_pick;
    TextInputEditText tv_result; //Date and time text field
    TextInputEditText assName;
    TextInputEditText assTitle;
    TextInputEditText assNotes;
    float x1, x2, y1, y2;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);

        save = (Button) findViewById(R.id.save);
        b_pick = (Button) findViewById(R.id.b_pick);

        tv_result = (TextInputEditText) findViewById(R.id.tv_result);
        assName = (TextInputEditText) findViewById(R.id.assName);
        assTitle = (TextInputEditText) findViewById(R.id.assTitle);
        assNotes = (TextInputEditText) findViewById(R.id.assNotes);

        save.setOnClickListener(new View.OnClickListener() {  //What should happen when save button clicked?
            @Override
            public void onClick(View v) {                     //Maybe call controller method pass assignment info, let controller / model save to database
                // For now I display info in toast
                Toast.makeText(getApplicationContext(),
                        "Name: "+assName.getText()+
                                " Title: " +assTitle.getText()+
                                " Date/Time: "+tv_result.getText()+
                                " Notes: "+assNotes.getText(),
                        Toast.LENGTH_LONG).show();

                //Handler runs a function after a set amount of time. Creates a delayed action.
                Handler handler = new Handler(){
                    public void handleMessage(Message message){
                        finish();
                    }
                };

                handler.sendEmptyMessageDelayed(0, 1500);//1500 is milliseconds

                //Can create intent here to go to view assignment page?
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
                }
                break;
        }
        return false;
    }

    @Override   //Getting which menu item is selected and creating toasts when they are
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu:
                Toast.makeText(this, "Main Menu.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
                return true;
            case R.id.add_assignment:
                Toast.makeText(this, "Add Assignment selected.", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.add_course:
                Toast.makeText(this, "Add course selected.", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}