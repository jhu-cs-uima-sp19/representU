package com.example.representuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


public class ManageIssuesActivity extends AppCompatActivity {

    Button cancel;
    Button add;
    ListView issuesList;
    ListView archives;
    boolean connected;
    DatabaseReference connectedRef;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference issues = database.getReference().child("issues");
    private DatabaseReference archiveDB = database.getReference().child("archived");

    private SharedPreferences myPrefs;



    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;


    ArrayList<String> archiveList = new ArrayList<>();
    ArrayAdapter<String> archAdapt;

    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> archivedIDs = new ArrayList<>();


    ArrayList<Issue> active = new ArrayList<>();
    ArrayList<Issue> archiveAL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_issues);

        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);


        issuesList = (ListView) findViewById(R.id.issuesList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);


        archives = (ListView) findViewById(R.id.archiveList);

        archAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, archiveList);



        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        issues.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                active.clear();
                idList.clear();
                arrayList.clear();
                issuesList.setAdapter(adapter);
                issuesList.setClickable(true);
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String string = childDataSnapshot.child("title").getValue(String.class);
                    String sum = childDataSnapshot.child("summary").getValue(String.class);
                    Issue current = new Issue(string, sum);
                    String id = childDataSnapshot.getKey();

                    current.idNum = id;
                    current.archived = childDataSnapshot.child("archived").getValue(boolean.class);
                    GenericTypeIndicator<ArrayList<String>> gti =new GenericTypeIndicator<ArrayList<String>>(){};
                    current.usersNay = childDataSnapshot.child("usersNay").getValue(gti);
                    current.usersYay = childDataSnapshot.child("usersYay").getValue(gti);
                    current.votesNay = childDataSnapshot.child("votesNay").getValue(Integer.class);
                    current.votesYay = childDataSnapshot.child("votesYay").getValue(Integer.class);

                    arrayList.add(string);
                    idList.add(id);
                    active.add(current);


                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        archiveDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                archiveList.clear();
                archivedIDs.clear();
                archiveAL.clear();
                archives.setAdapter(archAdapt);
                archives.setClickable(true);
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String string = childDataSnapshot.child("title").getValue(String.class);
                    String sum = childDataSnapshot.child("summary").getValue(String.class);
                    Issue current = new Issue(string, sum);
                    String id = childDataSnapshot.getKey();

                    current.idNum = id;
                    current.archived = childDataSnapshot.child("archived").getValue(boolean.class);
                    GenericTypeIndicator<ArrayList<String>> gti =new GenericTypeIndicator<ArrayList<String>>(){};
                    current.usersNay = childDataSnapshot.child("usersNay").getValue(gti);
                    current.usersYay = childDataSnapshot.child("usersYay").getValue(gti);
                    current.votesNay = childDataSnapshot.child("votesNay").getValue(Integer.class);
                    current.votesYay = childDataSnapshot.child("votesYay").getValue(Integer.class);

                    archiveList.add(string);
                    archivedIDs.add(id);
                    archiveAL.add(current);
                    archAdapt.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        issuesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!connected) {
                    Toast.makeText(getApplicationContext(), "Database Disconnected. Check internet connection!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(ManageIssuesActivity.this, EditIssueActivity.class);
                    String title = arrayList.get(position);
                    String idNum = idList.get(position);
                    Issue i = active.get(position);
                    intent.putExtra("name", title);
                    intent.putExtra("id", idNum);
                    intent.putExtra("sum", i.summary);
                    intent.putStringArrayListExtra("usersNay", (ArrayList<String>) i.usersNay);
                    intent.putStringArrayListExtra("usersYay", (ArrayList<String>) i.usersYay);
                    intent.putExtra("votesNay", i.votesNay);
                    intent.putExtra("votesYay", i.votesYay);
                    startActivity(intent);
                }
            }
        });


        archives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!connected) {
                    Toast.makeText(getApplicationContext(), "Database Disconnected. Check internet connection!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(ManageIssuesActivity.this, EditArchivedIssueActivity.class);
                    String title = archiveList.get(position);
                    String idNum = archivedIDs.get(position);
                    Issue i = archiveAL.get(position);
                    intent.putExtra("name", title);
                    intent.putExtra("id", idNum);
                    intent.putExtra("sum", i.summary);
                    intent.putStringArrayListExtra("usersNay", (ArrayList<String>) i.usersNay);
                    intent.putStringArrayListExtra("usersYay", (ArrayList<String>) i.usersYay);
                    intent.putExtra("votesNay", i.votesNay);
                    intent.putExtra("votesYay", i.votesYay);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_sga, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (!connected) {
            Toast.makeText(getApplicationContext(), "Database Disconnected. Check internet connection!", Toast.LENGTH_LONG).show();
        } else {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_add) {
                Intent intent = new Intent(ManageIssuesActivity.this, AddIssueActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}