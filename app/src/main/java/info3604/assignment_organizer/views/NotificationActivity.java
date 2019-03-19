package info3604.assignment_organizer.views;

import info3604.assignment_organizer.Main;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import info3604.assignment_organizer.R;


public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notification);
    }

    @Override   //Builds main_menu.xml from menu resource in res
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notif_menu, menu);
        return true;
    }

    @Override   //Getting which menu item is selected and creating toasts when they are
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.start_page:
                startActivity(new Intent(this, Main.class));
                break;
            case R.id.add_assignment:
                i = new Intent(this, add_assignment.class);
                startActivity(i);
                break;
            case R.id.add_course:
                i = new Intent(this, add_course.class);
                startActivity(i);
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
}