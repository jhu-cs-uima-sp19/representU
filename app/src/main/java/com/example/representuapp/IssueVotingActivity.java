package com.example.representuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IssueVotingActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Integer yeaNum = 0;
    public Integer nayNum = 0;
    public Button nayButton;
    public Button yeaButton;
    public List<Comment> commentsList;
    public List<String> votedYayList;
    public List<String> votedNayList;
    public TextView summary;
    public String title;
    public String idString;
    public boolean hasVotedYay;
    public boolean hasVotedNay;
    String name;
    String id;
    int colorPrimaryDark;
    int white;
    String JHED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_voting);
        summary = findViewById(R.id.user_issue_summary);
        Intent intent = getIntent();
        JHED = intent.getStringExtra("JHED");
        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        yeaNum = intent.getIntExtra("yea", 0);
        nayNum = intent.getIntExtra("nay", 0);
        yeaButton = findViewById(R.id.yay_user);
        nayButton = findViewById(R.id.nay_user);

        //initialize colors
        colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        white = ContextCompat.getColor(this, R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //votedYayList = (ArrayList<String>) issues.child(id).getClass().getField("usersNay");
        //votedNayList = (ArrayList<String>) snapshot.child("usersNay").getValue();
        //issues.child(id).child(name).child("votesYay").setValue(yeaNum);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: if user has voted Yea, unvote, nayButton.setVisibility(View.VISIBLE) and Toast.maketext("You unvoted")
                //hasVotedYay = checkVotedYay(JHED);

                if (hasVotedYay) {
                    //have both button visible
                    //nayButton.setVisibility(View.VISIBLE);
                    //yeaButton.setVisibility(View.VISIBLE);
                    yeaNum--;
                    issues.child(id).child(name).child("votesYay").setValue(yeaNum);
                    votedYayList.remove(JHED);
                    issues.child(id).child(name).child("usersYay").setValue(votedYayList);
                    Toast.makeText(IssueVotingActivity.this, "Vote Revoked", Toast.LENGTH_SHORT).show();
                } else {
                    //nayButton.setVisibility(View.GONE);
                    //yeaButton.setVisibility(View.VISIBLE);
                    yeaNum++;
                    issues.child(id).child(name).child("votesYay").setValue(yeaNum);
                    votedYayList.add(JHED);
                    issues.child(id).child(name).child("usersYay").setValue(votedYayList);
                    Toast.makeText(IssueVotingActivity.this, "You voted Yea!", Toast.LENGTH_SHORT).show();

                    // Starts stats activity and sends title, id, votes for/against, and how the user voted
                    Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("id", id);
                    intent.putExtra("yea", yeaNum);
                    intent.putExtra("nay", nayNum);
                    intent.putExtra("voted", "Yea");
                    startActivity(intent);
                }
            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hasVotedNay = checkVotedNay(JHED);
                if (hasVotedNay) {
                    //have both button visible
                    //nayButton.setVisibility(View.VISIBLE);
                    //yeaButton.setVisibility(View.VISIBLE);
                    nayNum--;
                    issues.child(id).child(name).child("votesNay").setValue(nayNum);
                    votedNayList.remove(JHED);
                    issues.child(id).child(name).child("usersNay").setValue(votedNayList);
                    Toast.makeText(IssueVotingActivity.this, "Vote Revoked", Toast.LENGTH_SHORT).show();
                } else {
                    //yeaButton.setVisibility(View.GONE);
                    //nayButton.setVisibility(View.VISIBLE);
                    nayNum++;
                    issues.child(id).child(name).child("votesNay").setValue(nayNum);
                    votedNayList.add(JHED);
                    issues.child(id).child(name).child("usersNay").setValue(votedNayList);
                    Toast.makeText(IssueVotingActivity.this, "You voted Nay!", Toast.LENGTH_SHORT).show();

                    // Starts stats activity and sends title, id, votes for/against, and how the user voted
                    Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("id", id);
                    intent.putExtra("yea", yeaNum);
                    intent.putExtra("nay", nayNum);
                    intent.putExtra("voted", "Nay");

                    startActivity(intent);
                }
            }
        });

        loadIssuePage();
        loadComments();

    }

    public void loadIssuePage() {

        issues.child(id).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                summary.setText(snapshot.child("summary").getValue(String.class));
                yeaNum = snapshot.child("votesYay").getValue(Integer.class);
                nayNum = snapshot.child("votesNay").getValue(Integer.class);
                GenericTypeIndicator<ArrayList<String>> gti =new GenericTypeIndicator<ArrayList<String>>(){};
                votedYayList = snapshot.child("usersYay").getValue(gti);
                votedNayList = snapshot.child("usersNay").getValue(gti);

                if (votedNayList == null) {
                    votedNayList = new ArrayList<>();
                    votedYayList.add(" ");
                    issues.child(id).child(name).child("usersNay").setValue(votedNayList);

                }
                if (votedYayList == null) {
                    votedYayList = new ArrayList<>();
                    votedYayList.add(" ");
                    issues.child(id).child(name).child("usersYay").setValue(votedYayList);
                }
                Log.d("v_yaylist_afterNULL", votedYayList.toString());
                Log.d("v_naylist_afterNULL", votedNayList.toString());
                //GenericTypeIndicator<ArrayList<Comment>> gti =new GenericTypeIndicator<ArrayList<Comment>>(){};
                //commentsList = snapshot.child("comments").getValue(gti);
                hasVotedNay = checkVotedNay(JHED);
                hasVotedYay = checkVotedYay(JHED);

                //Check that visibility makes sense.
                //TODO: REMOVE FIRST IF

                if (hasVotedYay && hasVotedNay) {
                    Log.d("v_", "TRUE / TRUE");
                } else if (hasVotedYay) {
                    Log.d("v_", "TRUE / FALSE");
                    nayButton.setVisibility(View.GONE);
                    yeaButton.setVisibility(View.VISIBLE);
                } else if(hasVotedNay) {
                    Log.d("v_", "FALSE / TRUE");
                    nayButton.setVisibility(View.VISIBLE);
                    yeaButton.setVisibility(View.GONE);
                } else {
                    Log.d("v_", "FALSE / FALSE");
                    nayButton.setVisibility(View.VISIBLE);
                    yeaButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        setTitle(name);
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        issues.child(id).child(name).child("usersNay").setValue(votedNayList);
        issues.child(id).child(name).child("usersYay").setValue(votedYayList);
        issues.child(id).child(name).child("votesNay").setValue(nayNum);
        issues.child(id).child(name).child("votesYay").setValue(yeaNum);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkVotedNay(JHED);
        checkVotedYay(JHED);
        issues.child(id).child(name).child("usersNay").setValue(votedNayList);
        issues.child(id).child(name).child("usersYay").setValue(votedYayList);
        issues.child(id).child(name).child("votesNay").setValue(nayNum);
        issues.child(id).child(name).child("votesYay").setValue(yeaNum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVotedNay(JHED);
        checkVotedYay(JHED);
        issues.child(id).child(name).child("usersNay").setValue(votedNayList);
        issues.child(id).child(name).child("usersYay").setValue(votedYayList);
        issues.child(id).child(name).child("votesNay").setValue(nayNum);
        issues.child(id).child(name).child("votesYay").setValue(yeaNum);
    }
*/
    public void loadComments() {
    }

    public boolean checkVotedYay(String userName) {
        if (votedYayList.contains(userName)) {
            nayButton.setVisibility(View.GONE);
            yeaButton.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public boolean checkVotedNay(String userName) {
        if (votedNayList.contains(userName)) {
            nayButton.setVisibility(View.VISIBLE);
            yeaButton.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

}
