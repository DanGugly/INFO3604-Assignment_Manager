package info3604.assignment_organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.views.add_assignment;
import info3604.assignment_organizer.views.add_course;
import info3604.assignment_organizer.views.add_checkpoint;
import info3604.assignment_organizer.views.AssignmentList;
import info3604.assignment_organizer.views.CheckpointList;
import info3604.assignment_organizer.views.assignment_methods;
import info3604.assignment_organizer.views.checkpoint_methods;
import info3604.assignment_organizer.views.CourseList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import info3604.assignment_organizer.views.course_methods;
import info3604.assignment_organizer.views.view_checkpoints;
import info3604.assignment_organizer.views.view_courses;
import info3604.assignment_organizer.views.view_assignments;

import java.util.ArrayList;


public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private MainController MC;
    private ArrayList<String> list, list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssignmentList assignmentList = new AssignmentList();

        ListView listView = (ListView)findViewById(R.id.listView);
        ListView listView2 = (ListView)findViewById(R.id.listView2);
        ListAdapter listAdapter, listAdapter2;



        MC = new MainController(this);

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        Cursor data = MC.getAssignmentListHome();
        Cursor cdata = MC.getCheckpointListHome();



        if(data.getCount() == 0){
            Toast.makeText(this,"No Assignments Registered",Toast.LENGTH_SHORT).show();
        }
        else{
            String dbString = "" ;
            while (data.moveToNext() ){
                String[] columnNames = data.getColumnNames();
                for (String name: columnNames) {
                    dbString += String.format("%s\n",
                            data.getString(data.getColumnIndex(name)));
                }
                list.add(dbString);
                dbString = "";
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
                listView.setAdapter(listAdapter);
            }
        }

        if(cdata.getCount() == 0){
//            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
        }
        else{
            String dbString = "" ;
            while (cdata.moveToNext() ){
                String[] columnNames = cdata.getColumnNames();
                for (String name: columnNames) {
                    dbString += String.format("%s\n",
                            cdata.getString(cdata.getColumnIndex(name)));
                }
                list2.add(dbString);
                dbString = "";
                listAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list2);
                listView2.setAdapter(listAdapter2);
            }
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                break;
            case R.id.courses:
                startActivity(new Intent(this, view_courses.class));
                break;
            case R.id.assignments:
                startActivity(new Intent(this, view_assignments.class));
                break;
            case R.id.checkpoints:
                startActivity(new Intent(this, view_checkpoints.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                startActivity(new Intent(this, add_checkpoint.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
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

     */
}