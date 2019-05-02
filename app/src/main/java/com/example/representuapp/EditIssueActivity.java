package com.example.representuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    int colorPrimary;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference issues = database.getReference().child("issues");

    public AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_issue);

        Intent intent = getIntent();
        issueName = getIntent().getStringExtra("name");
        issueID = getIntent().getStringExtra("id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.edit_issue_title_);

        desc = (EditText) findViewById(R.id.edit_issue_summary);

        issues.child(issueID).addValueEventListener(new ValueEventListener() {
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


        Button delete = findViewById(R.id.deleteButton);
        alertDialogBuilder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText(R.string.delete_confirm);
        //title.setTypeface(android.gra);
        //title.setBackgroundColor(colorPrimary);
        colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        title.setTextColor(colorPrimary);
        title.setTextSize(22);
        title.setPaddingRelative(0,30,0,0);
        title.setCompoundDrawablePadding(10);
        title.setGravity(Gravity.CENTER);
        alertDialogBuilder.setCustomTitle(title).setCancelable(false);

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        issues.child(issueID).removeValue();
                        Toast.makeText(getApplicationContext(), "Issue Deleted", LENGTH_SHORT).show();
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
        int id = item.getItemId();
        if (id == R.id.action_save) {
            summary = desc.getText().toString();
            name = title.getText().toString();

            if(summary.equals(null) || summary.equals("") || name.equals(null) || name.equals("") ) {
                Toast.makeText(getApplicationContext(), "Error: Please Fill All Fields", LENGTH_SHORT).show();
            } else {
                issues.child(issueID).child("summary").setValue(summary);
                issues.child(issueID).child("title").setValue(name);
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}