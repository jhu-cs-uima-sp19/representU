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

public class AddMemberActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveIssue();
    }


    public void saveIssue() {
        final EditText memName = findViewById(R.id.member_name);
        final EditText memPos = findViewById(R.id.member_pos);
        final EditText memBio = findViewById(R.id.member_bio);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if((memName.getText().toString()).equals(null) || (memName.getText().toString()).equals("")
                        || (memPos.getText().toString()).equals(null) || (memPos.getText().toString()).equals("")
                        || (memBio.getText().toString()).equals(null) || (memBio.getText().toString()).equals("") ) {
                    Toast.makeText(getApplicationContext(), "Error: Please Fill All Fields", LENGTH_SHORT).show();
                } else {
                    SGAMember person = new SGAMember(memName.getText().toString(), memPos.getText().toString(), memBio.getText().toString());
                    dbRef.child("SGA").child(person.idNum.toString()).setValue(person);
                    Toast.makeText(getApplicationContext(), "Added " + person.name, LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
