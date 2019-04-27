package com.example.representuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private List<Issue> issueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter adapter;
    public SharedPreferences pass;
    public SharedPreferences.Editor editor;
    public AlertDialog.Builder alertDialogBuilder;
    int white;
    int colorPrimary;
    int colorAccent;
    String JHED;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.issueTitle);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }

    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("issues");

        FirebaseRecyclerOptions<Issue> options =
                new FirebaseRecyclerOptions.Builder<Issue>()
                        .setQuery(query, new SnapshotParser<Issue>() {
                            @NonNull
                            @Override
                            public Issue parseSnapshot(@NonNull DataSnapshot snapshot) {
                                //remember to grab ID num for issue page
                                String title = "";
                                String summary = "";
                                //for (DataSnapshot childsDataSnapshot : snapshot.getChildren()) {
                                    title = snapshot.child("title").getValue(String.class);
                                //}
                                return new Issue(title, summary, snapshot.getKey());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Issue, UserFeedActivity.ViewHolder>(options) {
            @Override
            public UserFeedActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_textview, parent, false);

                return new UserFeedActivity.ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(UserFeedActivity.ViewHolder holder, final int position, final Issue model) {
                holder.setTxtTitle(model.title);
                //editor.putString("idPass", model.idNum.toString());
                //editor.putString("titlePass", model.title);
                //editor.apply();
                //editor.commit();
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(SGAFeedActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserFeedActivity.this, IssueVotingActivity.class);
                        intent.putExtra("title", model.title);
                        intent.putExtra("id", model.idNum.toString());
                        intent.putExtra("yea", model.votesYay);
                        intent.putExtra("nay", model.votesNay);
                        intent.putExtra("JHED", JHED);
                        startActivity(intent);
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        JHED = intent.getStringExtra("JHED");
        pass = this.getPreferences(0);
        editor = pass.edit();
        setSupportActionBar(toolbar);

        white = ContextCompat.getColor(this, R.color.white);
        colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        colorAccent = ContextCompat.getColor(this, R.color.colorAccent);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadIssues();

        alertDialogBuilder = new AlertDialog.Builder(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.meet_your_sga) {
            Intent intent = new Intent(UserFeedActivity.this, MeetYourSGAActivity.class);
            startActivity(intent);
        } else if (id == R.id.settingsUser) {

        } else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        //set message
        alertDialogBuilder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText(R.string.logout_confirm);
        //title.setTypeface(android.gra);
        //title.setBackgroundColor(colorPrimary);
        title.setTextColor(colorPrimary);
        title.setTextSize(22);
        title.setPaddingRelative(0,30,0,0);
        title.setCompoundDrawablePadding(10);
        title.setGravity(Gravity.CENTER);

        alertDialogBuilder.setCustomTitle(title).setCancelable(false);

        //define yes button
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //define cancel button
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        // create and show alert dialog
        alertDialogBuilder.create().show();
    }

    public void loadIssues() {
        recyclerView = (RecyclerView) findViewById(R.id.issueFeed);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fetch();

        // specify an adapter (see also next example)
        //mAdapter = new FirebaseRecyclerAdapter(issueList);
        //recyclerView.setAdapter(mAdapter);
    }
}
