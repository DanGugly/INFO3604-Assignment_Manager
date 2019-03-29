package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Course;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CourseList extends AppCompatActivity {

    private MainController MC;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        ListView listView = (ListView)findViewById(R.id.courseListView);
        MC = new MainController(this);

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = MC.getCourseList();

        String dbString = "" ;

        if(data.getCount() == 0){
            Toast.makeText(this,"No Courses Registered",Toast.LENGTH_SHORT).show();
        }
        else{
            while (data.moveToNext() ){
                String[] columnNames = data.getColumnNames();
                for (String name: columnNames) {
                    dbString += String.format("%s: %s\n", name,
                            data.getString(data.getColumnIndex(name)));
                }
                theList.add(dbString);
                dbString = "";
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }
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
            case R.id.add_checkpoint:
                startActivity(new Intent(this, add_checkpoint.class));
                break;
            case R.id.add_course:
                startActivity(new Intent(this, add_course.class));
                break;
            case R.id.start_page:
                startActivity(new Intent(this, Main.class));
                break;
            case R.id.add_assignment:
                startActivity(new Intent(this, add_assignment.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
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
