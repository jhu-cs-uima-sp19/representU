package com.example.representuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_SHORT;

public class EditIssueActivity extends AppCompatActivity {


    EditText title;
    EditText desc;


    String summary;
    String name;
    String issueName;
    String issueID;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference issues = database.getReference().child("issues");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_issue);

        Intent intent = getIntent();
        issueName = getIntent().getStringExtra("name");
        issueID = getIntent().getStringExtra("id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.editTitle);

        desc = (EditText) findViewById(R.id.editDesc);

        issues.child(issueID).child(issueName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                desc.setText(snapshot.child("summary").getValue(String.class));
                title.setText(snapshot.child("title").getValue(String.class));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setTitle(issueName);


        Button delete = findViewById(R.id.deleteEdit);

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                issues.child(issueID).removeValue();
                finish();
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
            summary = desc.getText().toString();
            issues.child(issueID).child(issueName).child("summary").setValue(summary);
            name = title.getText().toString();
            issues.child(issueID).setValue(name);
            issues.child(issueID).child(name).child("summary").setValue(summary);
            issues.child(issueID).child(name).child("title").setValue(name);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}