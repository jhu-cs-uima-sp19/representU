package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;

public class AddIssueActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveIssue();
    }


    public void saveIssue() {
        final EditText issueTitle = findViewById(R.id.issue_title);
        final EditText issueSum = findViewById(R.id.issue_summary);
        Button saveButton = findViewById(R.id.addButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if((issueTitle.getText().toString()).equals(null) || (issueTitle.getText().toString()).equals("")
                        || (issueSum.getText().toString()).equals(null) || (issueSum.getText().toString()).equals("") ) {
                    Toast.makeText(getApplicationContext(), "Error: Please Fill All Fields", LENGTH_SHORT).show();
                } else {
                    Issue issue = new Issue(issueTitle.getText().toString(), issueSum.getText().toString());
                    dbRef.child("issues").child(issue.idNum.toString()).setValue(issue);
                    Toast.makeText(getApplicationContext(), "Added Issue", LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
