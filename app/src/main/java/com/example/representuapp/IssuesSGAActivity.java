package com.example.representuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IssuesSGAActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Integer yeaNum = 0;
    public Integer nayNum = 0;
    public TextView summary;
    public String title;
    public String idString;
    String name;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_sga);
        summary = findViewById(R.id.sga_issue_summary);
        //pref = this.getPreferences(0);
        //editor = pref.edit();
        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadIssuePage();
        //voting();
    }

    public void loadIssuePage() {
        Button yeaButton = (Button) findViewById(R.id.yay_sga);
        Button nayButton = (Button) findViewById(R.id.nay_sga);
        //String idString = pref.getString("idPass", "");
        //String titleString = pref.getString("titlePass", "909090909090");
        //summary.setText(id);
        issues.child(id).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                summary.setText(snapshot.child("summary").getValue(String.class));
                yeaNum = snapshot.child("votesYay").getValue(Integer.class);
                nayNum = snapshot.child("votesNay").getValue(Integer.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setTitle(name);
    }

    public void voting() {
        final Button yeaButton = (Button) findViewById(R.id.yay_sga);
        final Button nayButton = (Button) findViewById(R.id.nay_sga);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = yeaNum + nayNum;
                int percentage;
                if (total == 0) {
                    percentage = 0;
                } else {
                    percentage = (yeaNum * 100) / total;
                }
                yeaButton.setText("" + percentage + "/%");
            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = yeaNum + nayNum;
                int percentage;
                if (total == 0) {
                    percentage = 0;
                } else {
                    percentage = (nayNum * 100) / total;
                }
                nayButton.setText("" + percentage + "/%");
            }
        });
    }
}
