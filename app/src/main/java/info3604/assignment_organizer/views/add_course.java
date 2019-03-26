package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Course;

import com.google.android.material.textfield.TextInputEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class add_course extends AppCompatActivity {

    TextInputEditText name, code, level, credits;
    float x1, x2, y1, y2;
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
        CC = new CourseController(this);
        printDB();
    }

    private boolean checkFields(){

        String val = code.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter course code.", Toast.LENGTH_SHORT).show();
            return false;
        }
        val = name.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter course name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        val = credits.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter course credits.", Toast.LENGTH_SHORT).show();
            return false;
        }
        val = level.getText().toString();
        if (val.equals("")){
            Toast.makeText(this, "Enter course level.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean addToDb(View view){
        boolean result = false;
        if (checkFields()){
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
            result = CC.addCourse(course);

        }
        Log.d("RESULT:",result+"");
        printDB();
        return result;
    }

    public void printInput(String code, String name, int credits, int level){
        Toast.makeText(this, code + " " + name + " " + credits + " " + level,Toast.LENGTH_LONG).show();
    }

    public boolean deleteFromDb(View view){
        boolean result = CC.deleteCourse(code.getText().toString());
        printDB();
        return result;
    }


    //Todo: make it so that the user doesn't need to fill in the fields they don't want to update
    public boolean updateDb(View view){
        boolean result = false;
        if (checkFields()){
            Course course = new Course(
                    code.getText().toString(),
                    name.getText().toString(),
                    Integer.parseInt(credits.getText().toString()),
                    Integer.parseInt(level.getText().toString())
            );
            result = CC.updateCourse(course);
        }
        Log.d("RESULT:",result+"");
        printDB();
        return result;
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

    @Override   //Builds main_menu.xml from menu resourse in res
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override   //Getting which menu item is selected and creating toasts when they are
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_assignment:
                startActivity(new Intent(this, add_assignment.class));
                break;
            case R.id.start_page:
                startActivity(new Intent(this, Main.class));
                break;
            case R.id.course_view:
                startActivity(new Intent(this, View_Course.class));
                break;
            case R.id.assignment_view:
                startActivity(new Intent(this, View_Assignment.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                if(x1>x2){
                    Intent i = new Intent(this, add_assignment.class);
                    startActivity(i);
                }
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