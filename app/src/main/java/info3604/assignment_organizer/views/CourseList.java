package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Course;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CourseList extends AppCompatActivity {

    private MainController MC;

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
}
