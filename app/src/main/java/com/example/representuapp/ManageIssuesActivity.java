package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.UUID;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;


public class ManageIssuesActivity extends AppCompatActivity {

    Button cancel;
    Button add;
    ListView issuesList;
    ListView archives;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference issues = database.getReference().child("issues");

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ArrayList<String> archiveList = new ArrayList<>();
    ArrayAdapter<String> archAdapt;

    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> archivedIDs = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_issues);

        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add button
        add = (Button) findViewById(R.id.addIssue);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageIssuesActivity.this, AddIssueActivity.class);
                startActivity(intent);
            }
        });

        issuesList = (ListView) findViewById(R.id.issuesList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        issuesList.setAdapter(adapter);
        issuesList.setClickable(true);

        //archives = (ListView) findViewById(R.id.archiveList);

        //archAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, archiveList);
        //archives.setAdapter(archAdapt);
        //archives.setClickable(true);

        issues.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayList.clear();
                idList.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String id = childDataSnapshot.getKey();
                    for (DataSnapshot childsDataSnapshot : childDataSnapshot.getChildren()) {
                        String string = childsDataSnapshot.child("title").getValue(String.class);
                        /*if (childsDataSnapshot.child("archived").getValue(boolean.class)) {
                            archiveList.add(string);
                            archivedIDs.add(id);
                            archAdapt.notifyDataSetChanged();
                        } else {*/
                            arrayList.add(string);
                            idList.add(id);
                            adapter.notifyDataSetChanged();
                        //}
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        issuesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageIssuesActivity.this, EditIssueActivity.class);
                String title = arrayList.get(position);
                String idNum = idList.get(position);
                intent.putExtra("name", title);
                intent.putExtra("id", idNum);
                startActivity(intent);
            }
        });

        /*archives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageIssuesActivity.this, EditIssueActivity.class);
                String title = archiveList.get(position);
                String idNum = archivedIDs.get(position);
                intent.putExtra("name", title);
                intent.putExtra("id", idNum);
                startActivity(intent);
            }
        });*/
    }
}