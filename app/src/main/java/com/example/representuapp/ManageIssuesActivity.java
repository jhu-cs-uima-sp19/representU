package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseError;







public class ManageIssuesActivity extends AppCompatActivity {

    Button cancel;
    Button add;
    ListView issuesList;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_issues);

        Intent intent = getIntent();

        // Cancel button
        cancel = (Button) findViewById(R.id.exitIssues);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Cancel button
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

        /*dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot childDataSnapshot = dataSnapshot.child("issues");
                for (DataSnapshot childsDataSnapshot : dataSnapshot.getChildren()) {

                    Issue i = childsDataSnapshot.getValue(Issue.class);

                    //String string = childsDataSnapshot.child("title").getValue(String.class);
                    arrayList.add(i.title);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DataSnapshot childDataSnapshot = dataSnapshot.child("issues");
                    for (DataSnapshot childsDataSnapshot : dataSnapshot.getChildren()) {

                        Issue i = childsDataSnapshot.getValue(Issue.class);

                        //String string = childsDataSnapshot.child("title").getValue(String.class);
                        arrayList.add(i.title);
                        adapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataSnapshot childDataSnapshot = dataSnapshot.child("issues");
                for (DataSnapshot childsDataSnapshot : dataSnapshot.getChildren()) {

                    Issue i = childsDataSnapshot.getValue(Issue.class);

                    //String string = childsDataSnapshot.child("title").getValue(String.class);
                    arrayList.add(i.title);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                DataSnapshot childDataSnapshot = dataSnapshot.child("issues");
                for (DataSnapshot childsDataSnapshot : dataSnapshot.getChildren()) {

                    Issue i = childsDataSnapshot.getValue(Issue.class);

                    //String string = childsDataSnapshot.child("title").getValue(String.class);
                    arrayList.add(i.title);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError dataError) {
            }
        });*/

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot childDS = dataSnapshot.child("issues");
                for (DataSnapshot issueSnapshot: childDS.getChildren()) {
                    Issue i = issueSnapshot.getValue(Issue.class);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }


        });



        issuesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageIssuesActivity.this, EditIssueActivity.class);
                String title = arrayList.get(position);
                intent.putExtra("name", title);
                startActivity(intent);
            }
        });
    }
}
