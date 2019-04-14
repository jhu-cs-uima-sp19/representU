package com.example.representuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SGAFeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter adapter;
    public SharedPreferences pass;
    public SharedPreferences.Editor editor;

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
                                for (DataSnapshot childsDataSnapshot : snapshot.getChildren()) {
                                    title = childsDataSnapshot.child("title").getValue(String.class);
                                }
                                return new Issue(title, summary);
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Issue, SGAFeedActivity.ViewHolder>(options) {
            @Override
            public SGAFeedActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_textview, parent, false);

                return new SGAFeedActivity.ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(SGAFeedActivity.ViewHolder holder, final int position, Issue model) {
                holder.setTxtTitle(model.title);
                editor.putString("idPass", model.idNum.toString());
                editor.putString("titlePass", model.title.toString());
                editor.apply();
                editor.commit();
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SGAFeedActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SGAFeedActivity.this, IssuesSGAActivity.class);
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
        setContentView(R.layout.activity_sga_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        pass = this.getPreferences(0);
        editor = pass.edit();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadIssues();
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
        getMenuInflater().inflate(R.menu.sga, menu);
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

        if (id == R.id.manageIssues) {
            Intent intent = new Intent(SGAFeedActivity.this, ManageIssuesActivity.class);
            startActivity(intent);
        } else if (id == R.id.editSGA) {
            Intent intent = new Intent(SGAFeedActivity.this, EditSGAActivity.class);
            startActivity(intent);
        } else if (id == R.id.settingsSGA) {
            Intent intent = new Intent(SGAFeedActivity.this, SGASettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            finish();

        } else if (id == R.id.changePassword) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
