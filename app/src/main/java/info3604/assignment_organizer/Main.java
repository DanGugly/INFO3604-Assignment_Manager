package info3604.assignment_organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import info3604.assignment_organizer.views.AssignmentList;
import info3604.assignment_organizer.views.CheckpointList;
import info3604.assignment_organizer.views.assignment_methods;
import info3604.assignment_organizer.views.checkpoint_methods;
import info3604.assignment_organizer.views.CourseList;
import info3604.assignment_organizer.views.course_methods;
import info3604.assignment_organizer.views.view_checkpoints;
import info3604.assignment_organizer.views.view_courses;
import info3604.assignment_organizer.views.view_assignments;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.add_assignment:
                i = new Intent(this, assignment_methods.class);
                startActivity(i);
                break;
            case R.id.add_course:
                i = new Intent(this, course_methods.class);
                startActivity(i);
                break;
            case R.id.add_checkpoint:
                i = new Intent(this, checkpoint_methods.class);
                startActivity(i);
                break;
            case R.id.course_list:
                i = new Intent(this, CourseList.class);
                startActivity(i);
                break;
            case R.id.course_list_new:
                i = new Intent(this, view_courses.class);
                startActivity(i);
                break;
            case R.id.assignment_list:
                i = new Intent(this, AssignmentList.class);
                startActivity(i);
                break;
            case R.id.assignment_list_new:
                i = new Intent(this, view_assignments.class);
                startActivity(i);
                break;
            case R.id.checkpoint_list:
                i = new Intent(this, CheckpointList.class);
                startActivity(i);
                break;
            case R.id.checkpoint_list_new:
                i = new Intent(this, view_checkpoints.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
