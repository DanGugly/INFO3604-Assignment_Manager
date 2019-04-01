package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CheckpointController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Assignment;
import info3604.assignment_organizer.models.Checkpoint;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

public class add_checkpoint extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener  {

    Button save;
    Button b_pick;
    TextInputEditText tv_result; //Date and time text field
    TextInputEditText assignmentCode;
    TextInputEditText checkpointTitle;
    TextInputEditText chkNotes;

    TextInputLayout assignment_til;
    TextInputLayout title_til;
    TextInputLayout date_til;


    AssignmentController AC;
    CheckpointController CC;
    MainController MC;

    Spinner spinner;
    Context context;

    ArrayAdapter<String> adapter;
    ArrayList<String> aList;
    ArrayList<Integer> idList;

    int assignmentID = -1;
    String spinnerContent = "Choose Assignment";

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_checkpoint);

        save = (Button) findViewById(R.id.save);
        b_pick = (Button) findViewById(R.id.b_pick);

        tv_result = (TextInputEditText) findViewById(R.id.tv_result);
        assignmentCode = (TextInputEditText) findViewById(R.id.assignmentCode);
        checkpointTitle = (TextInputEditText) findViewById(R.id.checkpointTitle);
        chkNotes = (TextInputEditText) findViewById(R.id.chkNotes);

        assignment_til = (TextInputLayout)findViewById(R.id.assignmentCode_til);
        title_til = (TextInputLayout)findViewById(R.id.checkpointTitle_til);
        date_til = (TextInputLayout)findViewById(R.id.tv_result_til);


        AC = new AssignmentController(this);
        CC = new CheckpointController(this);
        MC = new MainController(this);

        context = add_checkpoint.this;

        spinner = (Spinner)findViewById(R.id.spinner);
        aList = MC.getAssignmentStringList();
        idList = MC.getAssignmentIDList();
        aList.add(0,"Choose Assignment");
        idList.add(0,-1);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, aList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        save.setOnClickListener(new View.OnClickListener() {  //What should happen when save button clicked?
            @Override
            public void onClick(View v) {
                if(addToDb()){
                    adapter.notifyDataSetChanged();
                    Intent i = new Intent(context, view_checkpoints.class);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(add_checkpoint.this,
                        add_checkpoint.this, year, month, day);
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(add_checkpoint.this,
                add_checkpoint.this, hour, minute, true);
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

        if(assignmentID != -1){
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setDueDate(chosenDate);

            //Checkpoint c = MC.getCheckpoint(checkpointID);

            Assignment assignment = MC.getAssignment(assignmentID);
            assignment.setDueDate(chosenDate);

            if(checkpoint.isPastDueDate() && assignment.isPastDueDate()){
                Toast.makeText(this,"Please enter a valid date!",Toast.LENGTH_LONG).show();
            }
            else{
                tv_result.setText(chosenDate);
            }
        }
        else{
            Toast.makeText(this, "Please select an Assignment!",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkFields(){

        String val;

        val = spinnerContent;
        if (val.equals("Choose Assignment")){
            assignment_til.setError("Assignment required");
            return false;
        }
        else{
            assignment_til.setError(null);
            assignment_til.setErrorEnabled(false);
        }

        val = checkpointTitle.getText().toString();
        if (val.equals("")){
            title_til.setError("Title required");
            return false;
        }
        else{
            title_til.setError(null);
            title_til.setErrorEnabled(false);
        }

        val = tv_result.getText().toString();
        if (val.equals("")){
            date_til.setError("Date required");
            return false;
        }
        else{
            date_til.setError(null);
            date_til.setErrorEnabled(false);
        }

        return true;
    }

    public boolean addToDb(){
        boolean result = false;
        if (checkFields()){
            Checkpoint checkpoint = new Checkpoint(
                    assignmentID,
                    checkpointTitle.getText().toString().trim(),
                    tv_result.getText().toString().trim(),
                    chkNotes.getText().toString().trim()
            );
            if(AC.assignmentExistsInDb(""+assignmentID))
                result = CC.addCheckpoint(checkpoint);
        }
        Log.d("RESULT:",result+" ");
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).toString().equals("Choose Assignment")){
            //Do nothing
        }
        else{
            spinnerContent = parent.getItemAtPosition(position).toString();
            assignmentID = idList.get(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
