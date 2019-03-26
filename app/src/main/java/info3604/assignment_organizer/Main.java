package info3604.assignment_organizer;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.controllers.NotifController;
import info3604.assignment_organizer.views.add_assignment;
import info3604.assignment_organizer.views.add_course;
import info3604.assignment_organizer.views.View_Assignment;
import info3604.assignment_organizer.views.View_Course;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

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
            case R.id.course_view:
                startActivity(new Intent(this, View_Course.class));
                break;
            case R.id.assignment_view:
                startActivity(new Intent(this, View_Assignment.class));
                break;
            case R.id.send_notification:
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent notificationIntent = new Intent(this, NotifController.class);

                notificationIntent.putExtra("title","Assignment 2");    //Values should be pulled from DB
                notificationIntent.putExtra("content","Checkpoint 1 reminder");
                notificationIntent.putExtra("ticker","Due rand,...");

                PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 5);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }
        return super.onOptionsItemSelected(item);
    }
}
