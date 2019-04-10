package info3604.assignment_organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import info3604.assignment_organizer.about.About;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.help.Help;
import info3604.assignment_organizer.views.Details;
import info3604.assignment_organizer.views.add_assignment;
import info3604.assignment_organizer.views.add_checkpoint;
import info3604.assignment_organizer.views.add_course;
import info3604.assignment_organizer.views.assignment_methods;
import info3604.assignment_organizer.views.checkpoint_methods;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
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
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import java.util.ArrayList;


public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private MainController MC;
    private ArrayList<String> list, list2;

    private FabSpeedDial fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.listView);
        ListView listView2 = (ListView)findViewById(R.id.listView2);
        ListAdapter listAdapter, listAdapter2;

        fab = (FabSpeedDial) findViewById(R.id.speedDial);
        final View obscure = findViewById(R.id.obscure);

        MC = new MainController(this);

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        Cursor data = MC.getAssignmentListHome();
        Cursor cdata = MC.getCheckpointListHome();
        fab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.opt1:
                        intent = new Intent(Main.this, add_course.class);
                        startActivity(intent);
                        break;
                    case R.id.op2:
                        intent = new Intent(Main.this, add_assignment.class);
                        startActivity(intent);
                        break;
                    case R.id.opt3:
                        intent = new Intent(Main.this, add_checkpoint.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fab.isMenuOpen() && obscure.getVisibility() == View.GONE){
                    obscure.setVisibility(View.VISIBLE);
                }else if(!fab.isMenuOpen() && obscure.getVisibility() == View.VISIBLE){
                    obscure.setVisibility(View.GONE);
                }
                handler.postDelayed(this, 100);
            }
        }, 100);

        if(data.getCount() == 0){
            //Toast.makeText(this,"No Assignments Registered",Toast.LENGTH_SHORT).show();
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

            }
            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
            listView.setAdapter(listAdapter);
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

        if(list.size() != 0) {
            final String[] str = list.toArray(new String[list.size()]);
            //List of assignments
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] words = str[position].split("\r?\n");

                    Cursor cd = MC.getAssignment(words[0]);
//                Toast.makeText(getApplicationContext(),words[0], Toast.LENGTH_LONG).show();

                    String word = "";
                    while (cd.moveToNext()) {
                        String[] columnNames = cd.getColumnNames();
                        for (String name : columnNames) {
                            String title = getName(name, 2);
                            word += String.format(title + ": %s\n",
                                    cd.getString(cd.getColumnIndex(name)));
                        }
                    }
                    word = removeLine(word);
                    Intent intent = new Intent(getApplicationContext(), Details.class);
                    intent.putExtra("Type", "Assignment Details");
                    intent.putExtra("Details", word);
                    startActivity(intent);

                    //Toast.makeText(getApplicationContext(), word, Toast.LENGTH_LONG).show();
                }
            });
        }

        //checks if the checkpoint listview is empty
        if(list2.size() != 0) {
            final String[] str2 = list2.toArray(new String[list2.size()]);

            //List of Checkpoints
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] words = str2[position].split("\r?\n");

                    Cursor cd = MC.getCheckpoint(words[0]);

                    String word = "";
                    while (cd.moveToNext()) {
                        String[] columnNames = cd.getColumnNames();
                        for (String name : columnNames) {
                            String title = getName(name, 1);
                            word += String.format(title + ": %s\n",
                                    cd.getString(cd.getColumnIndex(name)));
                        }
                    }

                    word = removeLine(word);
                    Intent intent = new Intent(getApplicationContext(), Details.class);
                    intent.putExtra("Type", "Checkpoint Details");
                    intent.putExtra("Details", word);
                    startActivity(intent);

                    //Toast.makeText(getApplicationContext(), word, Toast.LENGTH_LONG).show();
                }
            });
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

    public String getName(String name, int check){
        if(name.equals("title") && check == 1)
            return "Checkpoint Title";
        if(name.equals("title") && check == 2)
            return "Assignment Title";
        if(name.equals("due_date"))
            return "Due Date";
        if(name.equals("notes"))
            return "Notes";
        if(name.equals("course_id"))
            return "Course ID";
        if(name.equals("start_date"))
            return "Start Date";
        if(name.equals("assignment_progress"))
            return "Assignment Progress";
        if(name.equals("checkpoint_progress"))
            return "Checkpoint Progress";
        return name;
    }

    public String removeLine(String name){
        if(name.charAt(name.length() - 1) == '\n')
            name = name.substring(0, name.length()-1);
        return name;
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
        switch (item.getItemId()) {
            case R.id.help:
                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, Help.class));
                return true;
            case R.id.about:
                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, About.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}