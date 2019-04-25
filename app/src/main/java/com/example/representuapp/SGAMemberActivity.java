package com.example.representuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class SGAMemberActivity extends AppCompatActivity {

    String name;
    String position;
    String bio;

    TextView memName;
    TextView memPos;
    TextView memBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgamember);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        position = i.getStringExtra("pos");
        bio = i.getStringExtra("bio");

        memName = (TextView) findViewById(R.id.memberName);
        memPos = (TextView) findViewById(R.id.position);
        memBio = (TextView) findViewById(R.id.biography);

        memName.setText(name);
        memPos.setText(position);
        memBio.setText(bio);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Meet " + name);
    }
}
