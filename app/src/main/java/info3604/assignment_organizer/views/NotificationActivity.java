package info3604.assignment_organizer.views;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.NotifController;


public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);
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