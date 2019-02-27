package info3604.assignment_organizer;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.views.add_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    float x1, x2, y1, y2;

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
        switch (item.getItemId()){
            case R.id.main_menu:
                Toast.makeText(this, "Main Menu.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_assignment:
                Toast.makeText(this, "Add Assignment selected.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, add_assignment.class);
                startActivity(intent);
                return true;
            case R.id.add_course:
                Toast.makeText(this, "Add course selected.", Toast.LENGTH_SHORT).show();
                return true;
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
                if(x1>x2){
                    Intent intent = new Intent(this, add_assignment.class);
                    startActivity(intent);
                }
                break;
        }
        return false;
    }
}
