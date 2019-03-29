package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.adapters.AssignmentAdapter;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Assignment;

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

import java.util.ArrayList;
import java.util.List;

public class view_assignments extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainController MC;
    private AssignmentController AC;
    private AssignmentAdapter adapter;
    private String filter = "";
    private List<Assignment> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_assignments);

        courseList = new ArrayList<>();

        //initialize the variables
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populate recyclerview
        populaterecyclerView(filter);
    }

    private void populaterecyclerView(String filter){
        MC = new MainController(this);
        AC = new AssignmentController(this);
        adapter = new AssignmentAdapter(MC.getAssignmentList(filter), this, mRecyclerView);
        Log.d("Assignment List Count:",""+adapter.getItemCount());
        mRecyclerView.setAdapter(adapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

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
            case R.id.addMenu:
                goToAddActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void goToAddActivity(){
        Intent intent = new Intent(this, add_assignment.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
