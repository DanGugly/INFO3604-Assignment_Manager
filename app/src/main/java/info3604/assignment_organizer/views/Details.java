package info3604.assignment_organizer.views;

import androidx.appcompat.app.AppCompatActivity;
import info3604.assignment_organizer.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    TextView textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Type");
        String details = intent.getStringExtra("Details");

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        textView2.setText(name);
        textView3.setText(details);


    }
}
