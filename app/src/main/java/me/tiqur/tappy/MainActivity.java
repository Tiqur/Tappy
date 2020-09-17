package me.tiqur.tappy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

class Click {
    long time;
    boolean key; // true = left, false = right

    public Click(long now, boolean b) {
        time = now;
        key = b;
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    long startTime = -1L;
    int bpmAverageRange = 2000; // ( Milliseconds ) this will be dynamic later on  ( example: if 2000ms, then get every click from last 2sec and average )
    HashSet<Click> clicks = new HashSet<>();
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
    // graph that shows ms between each click   ( highlight clicks in range )
    // average bpm
    // visualize average
    // ((((cilcks / (now  - startTime) * 60000) / 4) * 100) / 100)
    // total average bpm

    // for stamina practice, have progress bar

    // settings page?
    @Override
    public void onClick(View v) {
        long now = Calendar.getInstance().getTimeInMillis();
        if (startTime == -1L) startTime = now;


        // array of clicks that are IN THE SPECIFIED RANGE at this current time
        HashSet<Click> inRange = new HashSet<>();
        // add the clicks that are in range to the inRange set
        for (Click click : clicks) if (click.time > now - bpmAverageRange) inRange.add(click);

        // adds current click to both inRange, and normal set
        Click currentClick = new Click(now, v.getId() == button1.getId());
        clicks.add(currentClick);
        inRange.add(currentClick);

        // Calculates BPM
        double bpm = Math.round((((float) inRange.size() / bpmAverageRange * 60000.0) / 4.0) * 100.0) / 100.0;;


        if (v.getId() == button1.getId()) {
            bpmText.setText("BPM: " + bpm);
            button1.setText(String.valueOf(++b1clicks));
        } else {
            bpmText.setText("BPM: " + bpm);
            button2.setText(String.valueOf(++b2clicks));
        }
        clicksText.setText("Clicks: " + clicks.size());
    }


}