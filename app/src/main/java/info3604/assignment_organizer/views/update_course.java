package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class update_course extends AppCompatActivity {

    TextInputEditText name, level, credits;
    TextView txt;
    CourseController CC;
    String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_course);
        name = (TextInputEditText)findViewById(R.id.cname);
        level = (TextInputEditText)findViewById(R.id.clevel);
        credits = (TextInputEditText)findViewById(R.id.ccredits);
        txt = (TextView)findViewById(R.id.placeholder);
        CC = new CourseController(this);

        try {
            //get intent to get person id
            courseID = getIntent().getStringExtra("COURSE_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkFields(){
        boolean filled = false;

        String val;

        val = name.getText().toString();
        if (!val.equals("")){ filled = true; }

        val = credits.getText().toString();
        if (!val.equals("")){ filled = true; }

        val = level.getText().toString();
        if (!val.equals("")){ filled = true; }

        if(!filled)
            Toast.makeText(this,"Please fill in data in one of the fields",Toast.LENGTH_LONG).show();

        return filled;
    }


    //Todo: make it so that the user doesn't need to fill in the fields they don't want to update
    public void updateDb(View view){
        boolean result = false;
        if (checkFields()){
            String n;
            int lvl, cred;

            n = name.getText().toString();

            if(credits.getText().toString().equals("")){
                cred = 0;
            }
            else{
                cred = Integer.parseInt(credits.getText().toString());
            }

            if(level.getText().toString().equals("")){
                lvl = 0;
            }
            else{
                lvl = Integer.parseInt(level.getText().toString());
            }

            Course course = new Course(
                    courseID,
                    n,
                    cred,
                    lvl
            );
            result = CC.updateCourse(course);
        }
        Log.d("RESULT:",result+"");
        Intent i = new Intent(this, view_courses.class);
        startActivity(i);
    }

}
