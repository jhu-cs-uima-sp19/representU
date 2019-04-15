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
    public TextView summary;
    public String title;
    public String idString;
    public boolean yeaVotedAlready = false;
    public boolean nayVotedAlready = false;
    String name;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_voting);
        summary = findViewById(R.id.sga_issue_summary);
        //pref = this.getPreferences(0);
        //editor = pref.edit();
        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadIssuePage();
        //voting();
    }

    public void loadIssuePage() {
        Button yeaButton = (Button) findViewById(R.id.yay_user);
        Button nayButton = (Button) findViewById(R.id.nay_user);
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

    public void voting() {
        final Button yeaButton = (Button) findViewById(R.id.yay_user);
        final Button nayButton = (Button) findViewById(R.id.nay_user);

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
                issues.child(idString).child(title).child("votesYay").setValue(yeaNum);
                Toast.makeText(IssueVotingActivity.this,"You voted yea!", Toast.LENGTH_SHORT).show();
            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nayVotedAlready) {
                    nayNum--;
                    nayVotedAlready = !nayVotedAlready;
                } else {
                    nayNum++;
                    nayVotedAlready = !nayVotedAlready;
                }
                issues.child(idString).child(title).child("votesNay").setValue(nayNum);
                Toast.makeText(IssueVotingActivity.this,"You voted nay!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
