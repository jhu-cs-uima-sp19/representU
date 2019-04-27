package com.example.representuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class EditMemberActivity extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
        Intent i = getIntent();
        name = i.getStringExtra("name");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit " + name);
    }
}
