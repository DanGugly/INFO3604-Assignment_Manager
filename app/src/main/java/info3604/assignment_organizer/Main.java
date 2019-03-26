package info3604.assignment_organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import info3604.assignment_organizer.views.View_Assignment;
import info3604.assignment_organizer.views.View_Course;
import info3604.assignment_organizer.views.add_assignment;
import info3604.assignment_organizer.views.add_course;

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
//            case R.id.start_page:
//                i = new Intent(this, Main.class);
//                startActivity(i);
//                break;
            case R.id.add_assignment:
                i = new Intent(this, add_assignment.class);
                startActivity(i);
                break;
            case R.id.add_course:
                i = new Intent(this, add_course.class);
                startActivity(i);
                break;
            case R.id.assignment_view:
                i = new Intent(this, View_Assignment.class);
                startActivity(i);
                break;
            case R.id.course_view:
                i = new Intent(this, View_Course.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
