package com.example.representuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SGAMemberActivity extends AppCompatActivity {

    String name;
    String bio;
    String pos;

    TextView memName;
    TextView memBio;
    TextView memPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgamember);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        bio = i.getStringExtra("bio");
        pos = i.getStringExtra("pos");



        memName = (TextView) findViewById(R.id.member_name_user);
        memName.setText(name);
        memPos = (TextView) findViewById(R.id.member_pos_user);
        memPos.setText(pos);
        memBio = (TextView) findViewById(R.id.member_bio_user);
        memBio.setText(bio);

        setTitle("Meet " + name);



    }

}
