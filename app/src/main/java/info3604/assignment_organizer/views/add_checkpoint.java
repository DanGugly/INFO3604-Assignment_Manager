package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.CheckpointController;
import info3604.assignment_organizer.models.Checkpoint;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

import com.google.android.material.textfield.TextInputEditText;

public class add_checkpoint extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button save;
    Button b_pick;
    TextInputEditText tv_result; //Date and time text field
    TextInputEditText assignmentCode;
    TextInputEditText checkpointTitle;
    TextInputEditText chkNotes;
    TextInputEditText chkID;
    TextView txt;

    AssignmentController AC;
    CheckpointController CC;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_checkpoint);

        save = (Button) findViewById(R.id.save);
        b_pick = (Button) findViewById(R.id.b_pick);

        txt = (TextView) findViewById(R.id.placeholder);
        tv_result = (TextInputEditText) findViewById(R.id.tv_result);
        chkID = (TextInputEditText) findViewById(R.id.chkID);
        assignmentCode = (TextInputEditText) findViewById(R.id.assignmentCode);
        checkpointTitle = (TextInputEditText) findViewById(R.id.checkpointTitle);
        chkNotes = (TextInputEditText) findViewById(R.id.chkNotes);

        AC = new AssignmentController(this);
        CC = new CheckpointController(this);

        save.setOnClickListener(new View.OnClickListener() {  //What should happen when save button clicked?
            @Override
            public void onClick(View v) {                     //Maybe call controller method pass assignment info, let controller / model save to database
                // For now I display info in toast
                Toast.makeText(getApplicationContext(),
                        "Name: "+assignmentCode.getText()+
                                " Title: " +checkpointTitle.getText()+
                                " Date/Time: "+tv_result.getText()+
                                " Notes: "+chkNotes.getText(),
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(add_checkpoint.this,
                        add_checkpoint.this, year, month, day);
                datePickerDialog.show();
            }
        });

        printDB();
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
        val = checkpointTitle.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter checkpoint name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        val = assignmentCode.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter assignment code.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Todo: make it so that notes isn't a required field
        val = chkNotes.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter notes.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean addToDb(){
        boolean result = false;
        if (checkFields()){
            Checkpoint checkpoint = new Checkpoint(
                    Integer.parseInt(assignmentCode.getText().toString()),
                    checkpointTitle.getText().toString(),
                    tv_result.getText().toString(),
                    chkNotes.getText().toString()
            );
            if(AC.assignmentExistsInDb(assignmentCode.getText().toString()))
                result = CC.addCheckpoint(checkpoint);
            else
                Toast.makeText(this,"COURSE DOESN'T EXIST IN DB",Toast.LENGTH_LONG).show();
        }
        Log.d("RESULT:",result+" ");
        printDB();
        return result;
    }

    public boolean deleteFromDb(View view){
        boolean result = CC.deleteCheckpoint(Integer.parseInt(chkID.getText().toString()));
        printDB();
        return result;
    }

    public boolean updateDb(View view){
        boolean result = false;

        //Todo: reformat code in such a way that the user doesn't need to manually enter the id but it's detected on view
        String val = chkID.getText().toString();//line only for testing purposes


        if (checkFields() && !val.equals("")){
            Checkpoint checkpoint = new Checkpoint(
                    Integer.parseInt(assignmentCode.getText().toString()),
                    checkpointTitle.getText().toString(),
                    tv_result.getText().toString(),
                    chkNotes.getText().toString()
            );
            checkpoint.setCheckID(Integer.parseInt(val));
            result = CC.updateCheckpoint(checkpoint);
        }
        Log.d("RESULT:",result+"");
        printDB();
        return result;
    }

    public void printDB(){
        String dbString = CC.toString();
        Toast.makeText(this, dbString,Toast.LENGTH_LONG).show();
        txt.setText(dbString);
        checkpointTitle.setText("");
        chkNotes.setText("");
        assignmentCode.setText("");
        tv_result.setText("");
    }
}
