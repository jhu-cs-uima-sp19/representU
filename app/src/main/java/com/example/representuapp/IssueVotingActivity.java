package com.example.representuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class IssueVotingActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter adapter;
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
    int colorAccentDark;
    int white;
    String JHED;

    private SharedPreferences myPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_voting);
        summary = findViewById(R.id.user_issue_summary);
        Intent intent = getIntent();
        JHED = intent.getStringExtra("JHED");

        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        JHED = myPrefs.getString("JHED", "user");

        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        yeaNum = intent.getIntExtra("yea", 0);
        nayNum = intent.getIntExtra("nay", 0);
        yeaButton = findViewById(R.id.yay_user);
        nayButton = findViewById(R.id.nay_user);

        //initialize colors
        colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        colorAccentDark = ContextCompat.getColor(this, R.color.colorAccentDark);
        white = ContextCompat.getColor(this, R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasVotedYay) {
                    invalidateOptionsMenu();
                    yeaNum--;
                    issues.child(id).child("votesYay").setValue(yeaNum);
                    votedYayList.remove(JHED);
                    issues.child(id).child("usersYay").setValue(votedYayList);
                    Toast.makeText(IssueVotingActivity.this, "Vote Revoked", Toast.LENGTH_SHORT).show();
                } else {
                    invalidateOptionsMenu();
                    yeaNum++;
                    issues.child(id).child("votesYay").setValue(yeaNum);
                    votedYayList.add(JHED);
                    issues.child(id).child("usersYay").setValue(votedYayList);
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
                if (hasVotedNay) {
                    invalidateOptionsMenu();
                    nayNum--;
                    issues.child(id).child("votesNay").setValue(nayNum);
                    votedNayList.remove(JHED);
                    issues.child(id).child("usersNay").setValue(votedNayList);
                    Toast.makeText(IssueVotingActivity.this, "Vote Revoked", Toast.LENGTH_SHORT).show();
                } else {
                    invalidateOptionsMenu();
                    nayNum++;
                    issues.child(id).child("votesNay").setValue(nayNum);
                    votedNayList.add(JHED);
                    issues.child(id).child("usersNay").setValue(votedNayList);
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
        addComment();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtTitle;
        public TextView mainText;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.comment_section_user);
            txtTitle = itemView.findViewById(R.id.commentUsr);
            mainText = itemView.findViewById(R.id.comment_text);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }

        public void setMainTxt(String string) { mainText.setText(string); }

    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("comments")
                .child(id)
                .child("comments");

        FirebaseRecyclerOptions<Comment> options =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(query, new SnapshotParser<Comment>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @NonNull
                            @Override
                            public Comment parseSnapshot(@NonNull DataSnapshot snapshot) {
                                //remember to grab ID num for issue page
                                String comName = "";
                                String comText = "";
                                comName = snapshot.child("userName").getValue(String.class);
                                Log.d("comName:", comName);
                                comText = snapshot.child("mainText").getValue(String.class);
                                Log.d("comText:", comText);
                                return new Comment(comName, comText);
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Comment, ViewHolder>(options) {
            @Override
            public IssueVotingActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_textview, parent, false);

                return new IssueVotingActivity.ViewHolder(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(IssueVotingActivity.ViewHolder holder, final int position, final Comment model) {
                holder.setTxtTitle(model.userName);
                holder.setMainTxt(model.mainText);
                //editor.putString("idPass", model.idNum.toString());
                //editor.putString("titlePass", model.title);
                //editor.apply();
                //editor.commit();
//                holder.root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //Toast.makeText(SGAFeedActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(UserFeedActivity.this, IssueVotingActivity.class);
//                        intent.putExtra("title", model.title);
//                        intent.putExtra("id", model.idNum.toString());
//                        intent.putExtra("yea", model.votesYay);
//                        intent.putExtra("nay", model.votesNay);
//                        intent.putExtra("JHED", JHED);
//                        startActivity(intent);
//                    }
//                });
            }

        };
        recyclerView.setAdapter(adapter);
    }


    public void loadIssuePage() {

        issues.child(id).addValueEventListener(new ValueEventListener() {
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
                    issues.child(id).child("usersNay").setValue(votedNayList);

                }
                if (votedYayList == null) {
                    votedYayList = new ArrayList<>();
                    votedYayList.add(" ");
                    issues.child(id).child("usersYay").setValue(votedYayList);
                }
                hasVotedNay = checkVotedNay(JHED);
                hasVotedYay = checkVotedYay(JHED);

                //Check that visibility makes sense.
                if (hasVotedYay) {
                    nayButton.setVisibility(View.GONE);
                    yeaButton.setVisibility(View.VISIBLE);
                } else if(hasVotedNay) {
                    nayButton.setVisibility(View.VISIBLE);
                    yeaButton.setVisibility(View.GONE);
                } else {
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

    public void addComment() {
        Button addCom = findViewById(R.id.add_comment_button);
        addCom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(IssueVotingActivity.this, AddCommentActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("JHED", JHED);
                startActivity(intent);
            }
        });
    }


    public void loadComments() {
        recyclerView = (RecyclerView) findViewById(R.id.comments);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fetch();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (checkVotedNay(JHED) || checkVotedYay(JHED)) {
            getMenuInflater().inflate(R.menu.user_voting, menu);
            MenuItem stats = menu.findItem(R.id.action_stats);
            stats.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_stats) {
            if (checkVotedNay(JHED)) {
                Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                intent.putExtra("title", name);
                intent.putExtra("id", id);
                intent.putExtra("yea", yeaNum);
                intent.putExtra("nay", nayNum);
                intent.putExtra("voted", "Nay");
                startActivity(intent);
            } else if (checkVotedYay(JHED)) {
                Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                intent.putExtra("title", name);
                intent.putExtra("id", id);
                intent.putExtra("yea", yeaNum);
                intent.putExtra("nay", nayNum);
                intent.putExtra("voted", "Yea");
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
