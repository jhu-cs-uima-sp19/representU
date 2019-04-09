package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddIssueActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);

        Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
}
