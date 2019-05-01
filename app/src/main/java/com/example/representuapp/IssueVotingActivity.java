package com.example.representuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    public TextView summary;
    public String title;
    public String idString;
    //public boolean yeaVotedAlready = false;
    //public boolean nayVotedAlready = false;
    String name;
    String id;
    int colorPrimaryDark;
    //int colorAccentDark = ContextCompat.getColor(this, R.color.colorAccentDark);
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
        yeaButton = (Button) findViewById(R.id.yay_user);
        nayButton = (Button) findViewById(R.id.nay_user);
        //TODO: if (user has voted yea) { nayButton.setVisibility(View.GONE); }
        //TODO: if (user has voted nay) { yeaButton.setVisibility(View.GONE); }

        //initialize colors
        colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        white = ContextCompat.getColor(this, R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: if user has voted Yea, unvote, nayButton.setVisibility(View.VISIBLE) and Toast.maketext("You unvoted")
                nayButton.setVisibility(View.GONE);
                //TODO: Save user name in database as hasVotedYea
                yeaNum++;
                issues.child(id).child(name).child("votesYay").setValue(yeaNum);

                // Starts stats activity and sends title, id, votes for/against, and how the user voted
                Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                intent.putExtra("title", name);
                intent.putExtra("id", id);
                intent.putExtra("yea", yeaNum);
                intent.putExtra("nay", nayNum);
                intent.putExtra("voted", "yea");
                startActivity(intent);

                Toast.makeText(IssueVotingActivity.this,"You voted yea!", Toast.LENGTH_SHORT).show();

            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: if user has voted Nay, unvote, yeaButton.setVisibility(View.VISIBLE) and Toast.maketext("You unvoted")
                yeaButton.setVisibility(View.GONE);
                //TODO: Save user name in database as hasVotedNay
                nayNum++;
                issues.child(id).child("votesNay").setValue(nayNum);

                // Starts stats activity and sends title, id, votes for/against, and how the user voted
                Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                intent.putExtra("title", name);
                intent.putExtra("id", id);
                intent.putExtra("yea", yeaNum);
                intent.putExtra("nay", nayNum);
                intent.putExtra("voted", "nay");
                startActivity(intent);

                Toast.makeText(IssueVotingActivity.this,"You voted nay!", Toast.LENGTH_SHORT).show();
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
                .child("issues")
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
                                for (DataSnapshot childsDataSnapshot : snapshot.getChildren()) {
                                    comName = childsDataSnapshot.getValue(Comment.class).userName;
                                    comText = childsDataSnapshot.getValue(Comment.class).mainText;
                                }
                                return new Comment(comName, comText);
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Comment, IssueVotingActivity.ViewHolder>(options) {
            @Override
            public IssueVotingActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_textview, parent, false);

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
        //String idString = pref.getString("idPass", "");
        //String titleString = pref.getString("titlePass", "909090909090");
        //summary.setText(id);
        issues.child(id).addValueEventListener(new ValueEventListener() {
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
