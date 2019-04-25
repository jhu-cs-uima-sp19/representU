package com.example.representuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class EditMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
        Intent i = getIntent();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle("Edit This Person");
    }
}
