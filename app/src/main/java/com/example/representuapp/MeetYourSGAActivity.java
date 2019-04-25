package com.example.representuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.content.Intent;
import java.util.ArrayList;


public class MeetYourSGAActivity extends AppCompatActivity {

    RecyclerView rv_cards;
    SGAMembersAdapter SGAMembersAdapter;
    ArrayList<SGAMember> SGAMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_your_sga);
        Intent i = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Meet Your SGA");

        init();
    }

    private void init() {


        rv_cards = (RecyclerView) findViewById(R.id.rv_cards);
        rv_cards.setLayoutManager(new GridLayoutManager(MeetYourSGAActivity.this, 2));
        rv_cards.addItemDecoration(new Divider(10));


        SGAMembers = new ArrayList<>();


        SGAMembers.add(new SGAMember("Suzy","Pres"));
        SGAMembers.add(new SGAMember("John","VP"));
        SGAMembers.add(new SGAMember("Adam","Secretary"));
        SGAMembers.add(new SGAMember("Kate","Treasurer"));

        SGAMembersAdapter = new SGAMembersAdapter(MeetYourSGAActivity.this, SGAMembers, R.layout.grid_item);
        rv_cards.setAdapter(SGAMembersAdapter);

    }

}