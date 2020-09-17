package me.tiqur.tappy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    long startTime = Calendar.getInstance().getTimeInMillis();
    int clicks;
    private Button button1;
    private Button button2;
    private TextView bpm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        bpm = (TextView) findViewById(R.id.bpm);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    // unstable rate
    // graph that shows ms between each click
    // average bpm
    // visualize average

    @Override
    public void onClick(View v) {
        long now = Calendar.getInstance().getTimeInMillis() - startTime;
        clicks++;

        if (v.getId() == button1.getId()) {
            bpm.setText(String.valueOf(now));
        } else {
            bpm.setText("2");
        }
       // startTime = now;
    }
}