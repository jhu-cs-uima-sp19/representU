package com.example.representuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_issue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}