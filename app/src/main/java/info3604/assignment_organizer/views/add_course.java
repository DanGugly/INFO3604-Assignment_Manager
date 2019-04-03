package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class add_course extends AppCompatActivity {

    TextInputEditText name, code, level, credits;
    CourseController CC;
    TextInputLayout name_til, code_til, level_til, credits_til;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);

        name = (TextInputEditText)findViewById(R.id.cname);
        code = (TextInputEditText)findViewById(R.id.ccode);
        level = (TextInputEditText)findViewById(R.id.clevel);
        credits = (TextInputEditText)findViewById(R.id.ccredits);

        name_til = (TextInputLayout)findViewById(R.id.cname_til);
        code_til = (TextInputLayout)findViewById(R.id.ccode_til);
        level_til = (TextInputLayout)findViewById(R.id.clevel_til);
        credits_til = (TextInputLayout)findViewById(R.id.ccredits_til);

        CC = new CourseController(this);
    }

    public void addToDb(View view){
        boolean result = false;
        if (checkFields()){
            Course course = new Course(
                    code.getText().toString().trim(),
                    name.getText().toString().trim(),
                    Integer.parseInt(credits.getText().toString().trim()),
                    Integer.parseInt(level.getText().toString().trim())
            );
            result = CC.addCourse(course);

            Log.d("RESULT:",result+"");

            Intent i = new Intent(this, view_courses.class);
            startActivity(i);

        }
    }

    private boolean checkFields(){

        String val = code.getText().toString().trim();
        if (val.equals("")){
            code_til.setError("Field cannot be empty");
            return false;
        }
        else{
            code_til.setError(null);
            code_til.setErrorEnabled(false);
        }

        val = name.getText().toString().trim();
        if (val.equals("")){
            name_til.setError("Field cannot be empty");
            return false;
        }
        else{
            name_til.setError(null);
            name_til.setErrorEnabled(false);
        }

        val = credits.getText().toString().trim();
        if (val.equals("")){
            credits_til.setError("Field cannot be empty");
            return false;
        }
        else{
            credits_til.setError(null);
            credits_til.setErrorEnabled(false);
        }

        val = level.getText().toString().trim();
        if (val.equals("")){
            level_til.setError("Field cannot be empty");
            return false;
        }
        else{
            level_til.setError(null);
            level_til.setErrorEnabled(false);
        }

        return true;
    }
}
