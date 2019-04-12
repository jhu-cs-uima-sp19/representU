package com.example.representuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditIssueActivity extends AppCompatActivity {


    EditText title;
    EditText desc;


    String summary;
    String issueName = "";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_issue);

        Intent intent = getIntent();
        issueName = getIntent().getStringExtra("name");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.editTitle);
        title.setText(issueName);

        desc = (EditText) findViewById(R.id.editDesc);

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsDataSnapshot : childDataSnapshot.getChildren()) {
                        String string = childsDataSnapshot.child("title").getValue(String.class);
                        if (string.equals(issueName)) {
                            summary = childsDataSnapshot.child("summary").getValue(String.class);
                            desc.setText(summary);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsDataSnapshot : childDataSnapshot.getChildren()) {
                        String string = childsDataSnapshot.child("title").getValue(String.class);
                        if (string.equals(issueName)) {
                            summary = childsDataSnapshot.child("summary").getValue(String.class);
                            desc.setText(summary);
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsDataSnapshot : childDataSnapshot.getChildren()) {
                        String string = childsDataSnapshot.child("title").getValue(String.class);
                        if (string.equals(issueName)) {
                            summary = childsDataSnapshot.child("summary").getValue(String.class);
                            desc.setText(summary);
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsDataSnapshot : childDataSnapshot.getChildren()) {
                        String string = childsDataSnapshot.child("title").getValue(String.class);
                        if (string.equals(issueName)) {
                            summary = childsDataSnapshot.child("summary").getValue(String.class);
                            desc.setText(summary);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError dataError) {
            }
        });
    }
}