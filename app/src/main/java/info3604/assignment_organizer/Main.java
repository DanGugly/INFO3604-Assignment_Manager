package info3604.assignment_organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import info3604.assignment_organizer.views.AssignmentList;
import info3604.assignment_organizer.views.CheckpointList;
import info3604.assignment_organizer.views.assignment_methods;
import info3604.assignment_organizer.views.checkpoint_methods;
import info3604.assignment_organizer.views.CourseList;
import info3604.assignment_organizer.views.course_methods;
import info3604.assignment_organizer.views.view_checkpoints;
import info3604.assignment_organizer.views.view_courses;
import info3604.assignment_organizer.views.view_assignments;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
