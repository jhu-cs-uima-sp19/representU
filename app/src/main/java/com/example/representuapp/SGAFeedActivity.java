package com.example.representuapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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

import android.content.Context;
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

public class SGAFeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    public RecyclerView.Adapter mAdapter;
    public  RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter adapter;
    public SharedPreferences pass;
    public SharedPreferences.Editor editor;
    public AlertDialog.Builder alertDialogBuilder;
    public EditText new_pw;
    public EditText new_pw_confirm;
    public FirebaseDatabase database;
    public DatabaseReference adminPassword;
    public DatabaseReference adminUsername;
    public String new_pass;
    public String new_pass_confirm;
    public String old_pw;




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
                                return new Issue(title, summary, snapshot.getKey());
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
            protected void onBindViewHolder(SGAFeedActivity.ViewHolder holder, final int position, final Issue model) {
                holder.setTxtTitle(model.title);
                //editor.putString("idPass", model.idNum.toString());
                //editor.putString("titlePass", model.title);
                //editor.apply();
                //editor.commit();
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(SGAFeedActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SGAFeedActivity.this, IssuesSGAActivity.class);
                        intent.putExtra("title", model.title);
                        intent.putExtra("id", model.idNum.toString());
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

        alertDialogBuilder = new AlertDialog.Builder(this);
        new_pw = new EditText(this);
        new_pw_confirm = new EditText(this);


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
            //set message
            alertDialogBuilder.setTitle(R.string.logout_confirm).setCancelable(false);

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

        } else if (id == R.id.changePassword) {

            alertDialogBuilder = new AlertDialog.Builder(this);

            final EditText new_pw = new EditText(this);
            final EditText new_pw_confirm = new EditText(this);
            new_pw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            new_pw.setHint("New Password");
            new_pw_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            new_pw_confirm.setHint("Confirm New Password");

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(new_pw);
            ll.addView(new_pw_confirm);

            alertDialogBuilder.setView(ll);
            alertDialogBuilder.setTitle(R.string.cp_confirm).setCancelable(false);

            database = FirebaseDatabase.getInstance();
            adminPassword = database.getReference().child("adminPassword");

            adminPassword.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    old_pw = snapshot.getValue(String.class);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


            alertDialogBuilder.setPositiveButton("CHANGE PASSWORD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing here because we override this button later to change the close behaviour.
                            //However, we still need this because on older versions of Android unless we
                            //pass a handler the button doesn't get instantiated
                        }
            });

            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
                        }
            });

            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            //Overriding the handler immediately after show
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    boolean wantToCloseDialog = false;
                    new_pass = new_pw.getText().toString();
                    new_pass_confirm = new_pw_confirm.getText().toString();

                    //if both password & conformation are equal
                    //if new password is diff than old password
                    if (new_pass.equals("") | new_pass_confirm.equals("") | new_pass == null | new_pass_confirm == null){
                        Toast.makeText(getApplicationContext(), "Fields Can't Be Empty", Toast.LENGTH_SHORT).show();
                    }else if (new_pass.equals(new_pass_confirm) && !new_pass.equals(old_pw)) {
                        adminPassword.setValue(new_pass);
                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                        //finish();
                        wantToCloseDialog = true;
                    } else if (!new_pass.equals(new_pass_confirm)){
                        Toast.makeText(getApplicationContext(), "Passwords Don't Match! Try Again.", Toast.LENGTH_SHORT).show();
                    } else if (new_pass.equals(old_pw)){
                        Toast.makeText(getApplicationContext(), "New Password Must Not Equal Old Password! Try Again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        wantToCloseDialog = true;
                    }

                    //Do stuff, possibly set wantToCloseDialog to true then...
                    if(wantToCloseDialog)
                        dialog.dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });

            /*

            //define yes button
            alertDialogBuilder.setPositiveButton("CHANGE PASSWORD", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    new_pass = new_pw.getText().toString();
                    new_pass_confirm = new_pw_confirm.getText().toString();
                    //if both password & conformation are equal
                    //if new password is diff than old password
                    if (new_pass.equals(new_pass_confirm) && !new_pass.equals(old_pw)) {
                        adminPassword.setValue(new_pass);
                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (!new_pass.equals(new_pass_confirm)){
                        Toast.makeText(getApplicationContext(), "Passwords Don't Match! Try Again.", Toast.LENGTH_SHORT).show();
                    } else if (new_pass.equals(old_pw)){
                        Toast.makeText(getApplicationContext(), "New Password Must Not Equal Old Password! Try Again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
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

            */

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
