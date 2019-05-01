package com.example.representuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;

public class EditMemberActivity extends AppCompatActivity {

    String name;
    String id;
    String bio;
    String pos;

    EditText memName;
    EditText memPos;
    EditText memBio;
    Button deleteButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference members = database.getReference().child("SGA");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        id = i.getStringExtra("id");
        bio = i.getStringExtra("bio");
        pos = i.getStringExtra("pos");

        memName = findViewById(R.id.member_name_edit);
        memPos  = findViewById(R.id.member_pos_edit);
        memBio = findViewById(R.id.member_bio_edit);
        deleteButton = (Button) findViewById(R.id.deleteButtonEdit);

        memName.setText(name);
        memPos.setText(pos);
        memBio.setText(bio);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit " + name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_issue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();
        if (idItem == R.id.action_save) {
            name = memName.getText().toString();
            pos = memPos.getText().toString();
            bio = memBio.getText().toString();


            if(pos.equals(null) || pos.equals("") || name.equals(null) || name.equals("")
                    || bio.equals(null) || bio.equals("")) {
                Toast.makeText(getApplicationContext(), "Error: Please Fill All Fields", LENGTH_SHORT).show();
            } else {
                members.child(id).child("name").setValue(name);
                members.child(id).child("position").setValue(pos);
                members.child(id).child("bio").setValue(bio);
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
