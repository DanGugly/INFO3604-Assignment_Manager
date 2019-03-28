package info3604.assignment_organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import info3604.assignment_organizer.models.Checkpoint;
import info3604.assignment_organizer.views.AssignmentList;
import info3604.assignment_organizer.views.CheckpointList;
import info3604.assignment_organizer.views.add_assignment;
import info3604.assignment_organizer.views.add_course;
import info3604.assignment_organizer.views.add_checkpoint;
import info3604.assignment_organizer.views.CourseList;

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
                i = new Intent(this, add_assignment.class);
                startActivity(i);
                break;
            case R.id.add_course:
                i = new Intent(this, add_course.class);
                startActivity(i);
                break;
            case R.id.add_checkpoint:
                i = new Intent(this, add_checkpoint.class);
                startActivity(i);
                break;
            case R.id.course_list:
                i = new Intent(this, CourseList.class);
                startActivity(i);
                break;
            case R.id.assignment_list:
                i = new Intent(this, AssignmentList.class);
                startActivity(i);
                break;
            case R.id.checkpoint_list:
                i = new Intent(this, CheckpointList.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
