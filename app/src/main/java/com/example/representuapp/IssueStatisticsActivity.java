package com.example.representuapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class IssueStatisticsActivity extends AppCompatActivity {

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
    float yeaPercent;
    float nayPercent;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_statistics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        issueTitle = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        nayNum = intent.getIntExtra("nayNum", 0);
        yeaNum = intent.getIntExtra("yeaNum", 0);

        setTitle(issueTitle + " Statistics");


        youVotedX = findViewById(R.id.statistics_title);
        youVotedX.setText(issueTitle);
        youVotedX = findViewById(R.id.votes_nay);
        youVotedX.setText("Nay: " + nayNum);
        youVotedX = findViewById(R.id.votes_yay);
        youVotedX.setText("Yay: " + yeaNum);

        int colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);

        chart = (BarChart) findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();

        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "");
        Bardataset.setColors(new int[] {Color.parseColor("#EC940F"), Color.parseColor("#002D72")});

        DecimalFormat df = new DecimalFormat("##");
        com.github.mikephil.charting.formatter.PercentFormatter pf = new PercentFormatter(df);

        BARDATA = new BarData(BarEntryLabels, Bardataset);
        BARDATA.setValueFormatter(pf);
        BARDATA.setHighlightEnabled(false);

        chart.setData(BARDATA);
        chart.setDescription("");
        chart.getLegend().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        chart.setDrawGridBackground(false);
        chart.setClickable(false);

        chart.getBarData().setValueTextColor(Color.parseColor("#ffffff"));
        chart.getBarData().setValueTextSize(18);
        chart.getBarData().setDrawValues(true);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setTextSize(24);
        chart.getXAxis().setTextColor(Color.parseColor("#002D72"));

        chart.animateY(2000);
    }

    public void AddValuesToBARENTRY(){
        yeaPercent = (float) yeaNum / (yeaNum + nayNum);
        nayPercent = (float) nayNum / (yeaNum + nayNum);
        float x = yeaPercent * 100;
        float y = nayPercent * 100;
        BARENTRY.add(new BarEntry(x, 0));
        BARENTRY.add(new BarEntry(y, 1));
    }

    public void AddValuesToBarEntryLabels(){
        BarEntryLabels.add(0,"Yea");
        BarEntryLabels.add(1,"Nay");
    }
}
