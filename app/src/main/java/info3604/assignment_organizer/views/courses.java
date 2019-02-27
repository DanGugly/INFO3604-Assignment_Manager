package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Course;

import com.google.android.material.textfield.TextInputEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class courses extends AppCompatActivity {

    TextInputEditText name, code, level, credits;
    TextView txt;
    CourseController CC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);
        name = (TextInputEditText)findViewById(R.id.cname);
        code = (TextInputEditText)findViewById(R.id.ccode);
        level = (TextInputEditText)findViewById(R.id.clevel);
        credits = (TextInputEditText)findViewById(R.id.ccredits);
        txt = (TextView)findViewById(R.id.placeholder);
        CC = new CourseController(this, null, null, 1);
        printDB();
    }

    public void addToDb(View view){
        Course course = new Course(
                code.getText().toString(),
                name.getText().toString(),
                Integer.parseInt(credits.getText().toString()),
                Integer.parseInt(level.getText().toString())
        );
        /*printInput(code.getText().toString(),
                name.getText().toString(),
                Integer.parseInt(credits.getText().toString()),
                Integer.parseInt(level.getText().toString()));*/
        boolean result = CC.addCourse(course);
        Log.d("RESULT:",result+"");
        if(result)
            Toast.makeText(this,"Insertion successful",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Insertion unsuccessful",Toast.LENGTH_LONG).show();
        printDB();
    }

    public void printInput(String code, String name, int credits, int level){
        Toast.makeText(this, code + " " + name + " " + credits + " " + level,Toast.LENGTH_LONG).show();
    }

    public void deleteFromDb(View view){
        CC.deleteCourse(code.getText().toString());
        printDB();
    }

    public void printDB(){
        String dbString = CC.toString();
        Toast.makeText(this, dbString,Toast.LENGTH_LONG).show();
        txt.setText(dbString);
        name.setText("");
        code.setText("");
        level.setText("");
        credits.setText("");
    }

}
