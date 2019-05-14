package com.example.representuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MeetYourSGAActivity extends AppCompatActivity {

    RecyclerView rv_cards;
    SGAMembersAdapter SGAMembersAdapter;
    ArrayList<SGAMember> SGAMembers;
    DatabaseReference connectedRef;
    boolean connected;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference members = database.getReference().child("SGA");

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
        rv_cards.addItemDecoration(new Divider(40));


        ArrayList<String> SGANames = new ArrayList<>();
        SGAMembers = new ArrayList<>();

        members.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SGAMembers.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    SGAMember curr = new SGAMember();
                    curr.idNum = childDataSnapshot.child("idNum").getValue(String.class);
                    curr.bio = childDataSnapshot.child("bio").getValue(String.class);
                    curr.name = childDataSnapshot.child("name").getValue(String.class);
                    curr.position = childDataSnapshot.child("position").getValue(String.class);
                    SGAMembers.add(curr);
                    SGAMembersAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        SGAMembersAdapter = new SGAMembersAdapter(MeetYourSGAActivity.this, SGAMembers, R.layout.grid_item);
        rv_cards.setAdapter(SGAMembersAdapter);
    }


}