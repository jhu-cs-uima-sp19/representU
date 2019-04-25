package com.example.representuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.content.Intent;
import java.util.ArrayList;


public class MeetYourSGAActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_cards;
    SGAMembersAdapter SGAMembersAdapter;
    ArrayList<SGAMember> languageModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_your_sga);
        Intent i = getIntent();

        init();


    }

    private void init() {
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //setTitle("Languages");

        //initialisation of recycler view
        rv_cards = (RecyclerView) findViewById(R.id.rv_cards);

        //setting layout manager .Here in GridLayoutManager constructor you need to specify the number
        //of coloms in you want in grid .Here it is 2

        rv_cards.setLayoutManager(new GridLayoutManager(MeetYourSGAActivity.this, 2));
        rv_cards.addItemDecoration(new Divider(10));


        languageModels = new ArrayList<>();

        //adding few datas into array list

        languageModels.add(new SGAMember("1","Android"));
        languageModels.add(new SGAMember("2","C++"));
        languageModels.add(new SGAMember("3","C"));
        languageModels.add(new SGAMember("4","Python"));
        languageModels.add(new SGAMember("5","Java Script"));
        languageModels.add(new SGAMember("6","Java"));
        languageModels.add(new SGAMember("7","Sql"));
        languageModels.add(new SGAMember("8","PHP"));
        languageModels.add(new SGAMember("9","MySql"));
        languageModels.add(new SGAMember("10","Asp.Net"));

        //initialisation of recycler adapter
        SGAMembersAdapter = new SGAMembersAdapter(MeetYourSGAActivity.this, languageModels, R.layout.grid_item);

        //setting adapter to recycler
        rv_cards.setAdapter(SGAMembersAdapter);
    }

}