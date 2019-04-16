package info3604.assignment_organizer.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info3604.assignment_organizer.Main;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.adapters.CourseAdapter;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.xeoh.android.checkboxgroup.CheckBoxGroup;

import java.util.ArrayList;
import java.util.List;

public class view_courses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainController MC;
    private CourseController CC;
    private CourseAdapter adapter;
    private String filter = "";
    private List<Course> courseList;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_course);

        courseList = new ArrayList<>();

        //initialize the variables
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populate recyclerview
        populaterecyclerView(filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), add_course.class));
                Toast.makeText(getApplicationContext(),"Adding Course..", Toast.LENGTH_LONG).toString();
            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(this, Main.class));
                break;
            case R.id.courses:
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

    private void populaterecyclerView(String filter){
        MC = new MainController(this);
        CC = new CourseController(this);
        adapter = new CourseAdapter(MC.getCourseList(filter), this, mRecyclerView);
        Log.d("Course List Count:",""+adapter.getItemCount());
        mRecyclerView.setAdapter(adapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem del = menu.findItem(R.id.deleteMenu);
        del.setVisible(true);
        MenuItem item = menu.findItem(R.id.filterSpinner);
        Spinner spinner = (Spinner)item.getActionView();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getSelectedItem().toString();
                populaterecyclerView(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                populaterecyclerView(filter);
            }
        });


        spinner.setAdapter(adapter);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                //Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, Help.class));
                return true;
            case R.id.about:
                //Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.deleteMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Course");
                builder.setMessage("Are you sure you want to delete this Course(s)?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCourses();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void deleteCourses(){
        CheckBoxGroup<String> checkBoxGroup = adapter.getCheckBoxGroup();
        for(String course: checkBoxGroup.getValues()){
            CC.deleteCourse(course);
        }
        adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }

    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
