package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddIssueActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);

        Intent intent = getIntent();
        //DatabaseReference myRef = database.getReference("message");
        //myRef.setValue("Hello, World!");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveIssue();


    }

    public void saveIssue() {

    }
}
