package me.tiqur.tappy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    long startTime = -1L;
    int clicks;
    int b1clicks;
    int b2clicks;
    private Button button1;
    private Button button2;
    private TextView bpmText;
    private TextView clicksText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        bpmText = (TextView) findViewById(R.id.bpm);
        clicksText = (TextView) findViewById(R.id.clicks);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    // unstable rate
    // graph that shows ms between each click
    // average bpm
    // visualize average
    // ((((cilcks / (now  - startTime) * 60000) / 4) * 100) / 100)
    //		Stream Speed: " + (Math.round((((clickTimes.length) / (Date.now() - beginTime) * 60000)/4) * 100) / 100).toFixed(2) + " bpm.<br>\
    @Override
    public void onClick(View v) {
        long now = Calendar.getInstance().getTimeInMillis();
        if (startTime == -1L) startTime = now; // this will cause some issues, but will fix later
        double bpm = (Math.round((((float) clicks / (now - startTime) * 60000.0) / 4.0) * 100.0) / 100.0);
        clicks++;
        if (v.getId() == button1.getId()) {
            bpmText.setText("BPM: " + bpm);
            button1.setText(String.valueOf(++b1clicks));
        } else {
            bpmText.setText("BPM: " + bpm);
            button2.setText(String.valueOf(++b2clicks));
        }
        clicksText.setText("Clicks: " + clicks);
       // startTime = now;
    }
}