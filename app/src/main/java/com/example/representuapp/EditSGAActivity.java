package com.example.representuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EditSGAActivity extends AppCompatActivity {

    RecyclerView rv_cards;
    EditSGAAdapter SGAMembersAdapter;
    ArrayList<SGAMember> SGAMembers;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference members = database.getReference().child("SGA");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sga);
        Intent i = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Meet Your SGA");

        init();
    }

    private void init() {


        rv_cards = (RecyclerView) findViewById(R.id.rv_cardsEDIT);
        rv_cards.setLayoutManager(new GridLayoutManager(EditSGAActivity.this, 2));
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

        SGAMembersAdapter = new EditSGAAdapter(EditSGAActivity.this, SGAMembers, R.layout.grid_item);
        rv_cards.setAdapter(SGAMembersAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_sga, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(EditSGAActivity.this, AddMemberActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}