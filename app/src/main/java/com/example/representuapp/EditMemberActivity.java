package com.example.representuapp;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    int colorPrimary;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference members = database.getReference().child("SGA");


    public AlertDialog.Builder alertDialogBuilder;


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


        deleteButton = findViewById(R.id.deleteButtonEdit);
        alertDialogBuilder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText(R.string.remove_confirm);
        //title.setTypeface(android.gra);
        //title.setBackgroundColor(colorPrimary);
        colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        title.setTextColor(colorPrimary);
        title.setTextSize(22);
        title.setPaddingRelative(0,30,0,0);
        title.setCompoundDrawablePadding(10);
        title.setGravity(Gravity.CENTER);
        alertDialogBuilder.setCustomTitle(title).setCancelable(false);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        members.child(id).removeValue();
                        Toast.makeText(getApplicationContext(), "Member Removed", LENGTH_SHORT).show();
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                // create and show alert dialog
                alertDialogBuilder.create().show();
            }
        });
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
                Toast.makeText(getApplicationContext(), "Edit Saved", LENGTH_SHORT).show();
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
