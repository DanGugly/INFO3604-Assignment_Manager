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
import info3604.assignment_organizer.adapters.CheckpointAdapter;
import info3604.assignment_organizer.controllers.CheckpointController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Assignment;
import info3604.assignment_organizer.models.Checkpoint;

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

public class view_checkpoints extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainController MC;
    private CheckpointController CC;
    private CheckpointAdapter adapter;
    private String filter = "";
    private List<Checkpoint> courseList;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_checkpoints);

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
                startActivity(new Intent(getApplicationContext(), add_checkpoint.class));
                Toast.makeText(getApplicationContext(),"Adding Checkpoint..", Toast.LENGTH_LONG).toString();
            }
        });
        checkOverdueCheckpoints();
    }

    private void populaterecyclerView(String filter){
        MC = new MainController(this);
        CC = new CheckpointController(this);
        adapter = new CheckpointAdapter(MC.getCheckpointList(filter), this, mRecyclerView);
        Log.d("Checkpoint List Count:",""+adapter.getItemCount());
        checkOverdueCheckpoints();
        mRecyclerView.setAdapter(adapter);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(this, Main.class));
                break;
            case R.id.courses:
                startActivity(new Intent(this, view_courses.class));
                break;
            case R.id.assignments:
                startActivity(new Intent(this, view_assignments.class));
                break;
            case R.id.checkpoints:
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem del = menu.findItem(R.id.deleteMenu);
        MenuItem com = menu.findItem(R.id.completeMenu);
        del.setVisible(true);
        com.setVisible(true);
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
            case R.id.completeMenu:
                completeCheckpoints();
                return true;
            case R.id.deleteMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Checkpoint");
                builder.setMessage("Are you sure you want to delete this Checkpoint(s)?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCheckpoints();
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

    private void completeCheckpoints(){
        CheckBoxGroup<String> checkBoxGroup = adapter.getCheckBoxGroup();
        Checkpoint checkpoint;
        for(String chkID: checkBoxGroup.getValues()){

            checkpoint = MC.getCheckpoint(Integer.parseInt(chkID));
            checkpoint.setProgress(1);
            CC.updateCheckpoint(checkpoint);

        }
        adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }

    private void deleteCheckpoints(){
        CheckBoxGroup<String> checkBoxGroup = adapter.getCheckBoxGroup();
        for(String checkpoint: checkBoxGroup.getValues()){
            CC.deleteCheckpoint(Integer.parseInt(checkpoint));
        }
        adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }

    protected void onResume() {
        super.onResume();
        checkOverdueCheckpoints();
        adapter.notifyDataSetChanged();
    }

    private void checkOverdueCheckpoints(){
        List<Checkpoint> checkpointList = MC.getCheckpointList("");
        Assignment assignment;
        Checkpoint checkpoint;
        for(Checkpoint c: checkpointList){

            checkpoint = MC.getCheckpoint(c.getCheckpointID());
            assignment = MC.getAssignment(c.getAssignmentID());
            if((checkpoint.isPastDueDate() || assignment.isPastDueDate()) &&
                    (assignment.getProgress()!=1||checkpoint.getProgress()!=1)){
                checkpoint.setProgress(-1);
            }
            Log.d("Progress",checkpoint.getTitle()+ " " + checkpoint.getProgress());
            CC.updateCheckpoint(checkpoint);

        }
        adapter.notifyDataSetChanged();
    }
}
