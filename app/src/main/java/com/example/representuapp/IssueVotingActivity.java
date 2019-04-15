package com.example.representuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IssueVotingActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Integer yeaNum = 0;
    public Integer nayNum = 0;
    public Button nayButton;
    public Button yeaButton;
    public TextView summary;
    public String title;
    public String idString;
    //public boolean yeaVotedAlready = false;
    //public boolean nayVotedAlready = false;
    String name;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_voting);
        summary = findViewById(R.id.user_issue_summary);
        //pref = this.getPreferences(0);
        //editor = pref.edit();
        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        yeaButton = (Button) findViewById(R.id.yay_user);
        nayButton = (Button) findViewById(R.id.nay_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeaNum++;
                issues.child(id).child(name).child("votesYay").setValue(yeaNum);
                Toast.makeText(IssueVotingActivity.this,"You voted yea!", Toast.LENGTH_SHORT).show();
            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nayNum++;
                issues.child(id).child(name).child("votesNay").setValue(nayNum);
                Toast.makeText(IssueVotingActivity.this,"You voted nay!", Toast.LENGTH_SHORT).show();
            }
        });

        loadIssuePage();

    }

    public void loadIssuePage() {
        //String idString = pref.getString("idPass", "");
        //String titleString = pref.getString("titlePass", "909090909090");
        //summary.setText(id);
        issues.child(id).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                summary.setText(snapshot.child("summary").getValue(String.class));
                yeaNum = snapshot.child("votesYay").getValue(Integer.class);
                nayNum = snapshot.child("votesNay").getValue(Integer.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setTitle(name);
    }
    /*
    public void voting() {

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yeaVotedAlready) {
                    yeaNum--;
                    yeaVotedAlready = !yeaVotedAlready;
                } else {
                    yeaNum++;
                    yeaVotedAlready = !yeaVotedAlready;
                }
                yeaNum++;
                issues.child(idString).child(title).child("votesYay").setValue(yeaNum);
                Toast.makeText(IssueVotingActivity.this,"You voted yea!", Toast.LENGTH_SHORT).show();
            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if voted
                if (nayVotedAlready) {
                    nayNum--;
                    nayVotedAlready = false;
                } else {
                    nayNum++;
                    nayVotedAlready = true;
                }
                nayNum++;
                issues.child(idString).child(title).child("votesNay").setValue(nayNum);
                Toast.makeText(IssueVotingActivity.this,"You voted nay!", Toast.LENGTH_SHORT).show();
            }
        });
    } */

}
