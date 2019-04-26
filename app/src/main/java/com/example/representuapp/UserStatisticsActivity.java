package com.example.representuapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class UserStatisticsActivity extends AppCompatActivity {

    String issueTitle;
    String id;
    int yeaNum;
    int nayNum;
    String votedFor;
    BarChart chart;
    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;
    TextView youVotedX;
    FloatingActionButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_statistics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        issueTitle = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        yeaNum = intent.getIntExtra("yea", 0);
        nayNum = intent.getIntExtra("nay", 0);
        // Allows us to know which way user voted in previous activity; string = "yea" or "nay"
        votedFor = intent.getStringExtra("voted");

        youVotedX = findViewById(R.id.statistics_text);
        youVotedX.setText("You voted " + votedFor);
        exitButton = findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        //int colorAccentDark = ContextCompat.getColor(this, R.color.colorAccentDark);
        //white = ContextCompat.getColor(this, R.color.white);
        //bar chart
        chart = (BarChart) findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(new int[] {Color.parseColor("#EC940F"), Color.parseColor("#002D72")});
        chart.setDescription("");
        chart.getLegend().setEnabled(false);
        chart.setBorderColor(colorPrimaryDark);
        chart.setBorderWidth(10);
        chart.setDrawMarkerViews(false);
        chart.setDrawValueAboveBar(false);
        chart.setClickable(false);
        chart.setData(BARDATA);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setTextSize(18);
        chart.getXAxis().setTextColor(colorPrimaryDark);
        chart.getRendererLeftYAxis().computeAxis(0,100);
        //assert chart.getClipToPadding();
        chart.getRendererRightYAxis().computeAxis(0,100);
        chart.animateY(3000);

    }

    public void AddValuesToBARENTRY(){
        float yeaPercent = (float)yeaNum / (yeaNum + nayNum);
        float nayPercent = (float)nayNum / (yeaNum + nayNum);
        BARENTRY.add(new BarEntry(yeaPercent, 0));
        BARENTRY.add(new BarEntry(nayPercent, 1));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("Yea");
        BarEntryLabels.add("Nay");

    }

}
