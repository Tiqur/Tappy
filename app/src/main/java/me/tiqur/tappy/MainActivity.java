package me.tiqur.tappy;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashSet;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    long startTime = -1L;
    int bpmAverageRangeInterval = 2000; // ( Milliseconds ) this will be dynamic later on  ( example: if 2000ms, then get every click from last 2sec and average )
    HashSet<Long> clicks = new HashSet<>();
    boolean showEntireBpmAverageGraph = false;
    private LineGraphSeries<DataPoint> averageBpm;
    private LineGraphSeries<DataPoint> unstableRate;
    private GraphView graphView;
    private Button resetButton;
    private Button button1;
    private Button button2;
    private SeekBar bpmViz1;
    private TextView clicksText;
    private TextView unstableRateText;
    private TextView target;
    private TextView bpmText;
    private Double UR;
    int bpmTarget = 180;
    int b1clicks;
    int b2clicks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        bpmViz1 = (SeekBar) findViewById(R.id.bpmViz1);
        resetButton = (Button) findViewById(R.id.resetBtn);
        bpmText = (TextView) findViewById(R.id.bpm);
        clicksText = (TextView) findViewById(R.id.clicks);
        unstableRateText = (TextView) findViewById(R.id.unstableRateText);
        target = (TextView) findViewById(R.id.targetText);
        graphView = (GraphView) findViewById(R.id.bpmGraph);
        averageBpm = new LineGraphSeries<DataPoint>();
        unstableRate = new LineGraphSeries<DataPoint>();
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        bpmViz1.setMax(bpmTarget*2);
        target.setText("Target: " + bpmTarget);

        graphInit();

        // I should probably fix this at some point
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = -1;
                b1clicks = 0;
                b2clicks = 0;
                clicks.clear();
                bpmText.setText("BPM: 0");
                clicksText.setText("Clicks: 0");
                button1.setText("");
                button2.setText("");
            }
        });



    }

    private void graphInit() {
        averageBpm.setTitle("BPM");
        unstableRate.setTitle("Unstable Rate");
        graphView.addSeries(unstableRate);
        graphView.addSeries(averageBpm);
        unstableRate.setColor(Color.RED);
        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(bpmTarget*2);
        viewport.setMaxX(20);
        viewport.setScalable(true);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphView.onDataChanged(false, false);
    }

    // unstable rate
    // graph that shows ms between each click   ( highlight clicks in range )
    // average bpm
    // visualize average
    // ((((cilcks / (now  - startTime) * 60000) / 4) * 100) / 100)
    // total average bpm

    // for stamina practice, have progress bar

    // settings page?
    // OD setting that changes range

    // interpolate between colors depending on how close to target

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        long now = Calendar.getInstance().getTimeInMillis();
        if (startTime == -1L) startTime = now;


        // array of clicks that are IN THE SPECIFIED RANGE at this current time
        HashSet<Long> inRange = new HashSet<>();
        // add the clicks that are in range to the inRange set
        for (Long clickTime : clicks) if (clickTime > now - bpmAverageRangeInterval) inRange.add(clickTime);

        // adds current click to both inRange, and normal set
        clicks.add(now);
        inRange.add(now);

        // Calculates BPM
        double bpm = Math.round((((float) inRange.size() / bpmAverageRangeInterval * 60000.0) / 4.0) * 100.0) / 100.0;;

        // moves according to bpm
        bpmViz1.setProgress((int)bpm, true);

        // get UR
        UR = calcStandardDeviation(inRange);


        // append data to graph
        averageBpm.appendData(new DataPoint(clicks.size(), bpm), !showEntireBpmAverageGraph, Integer.MAX_VALUE);
        unstableRate.appendData(new DataPoint(clicks.size(), UR), !showEntireBpmAverageGraph, Integer.MAX_VALUE);


        // increase max graph size
        if (showEntireBpmAverageGraph) graphView.getViewport().setMaxX(clicks.size()+10);

        // update each of the label's text on each click
        updateText(v, bpm);



    }

    // calculate standard deviation / Unstable rate
    private Double calcStandardDeviation(HashSet<Long> inRange) {
        double mean = 0;
        double mean2 = 0;
        for (Long clickTime : inRange) {
            mean += clickTime;
        }
        mean = mean / inRange.size();
        for (Long clickTime : inRange) {
            mean2 += (clickTime - mean) * (clickTime - mean);
        }
        mean2 = mean2 / inRange.size();
        mean2 = Math.sqrt(mean2);
//        for (Long clickTime : inRange) mean += clickTime;
//        mean /= inRange.size();
//        for (Long clickTime : inRange) mean2 += Math.pow(clickTime - mean, 2);
//        mean2 /= inRange.size();
       // return Math.sqrt(mean2);
        return mean2 / 10;
    }

    private void updateText(View v, double bpm) {
        bpmText.setText("BPM: " + bpm);
        if (v.getId() == button1.getId()) {
            button1.setText(String.valueOf(++b1clicks));
        } else {
            button2.setText(String.valueOf(++b2clicks));
        }
        clicksText.setText("Clicks: " + clicks.size());
        unstableRateText.setText("Unstable Rate: " + new DecimalFormat("0.#").format(UR));
    }




}