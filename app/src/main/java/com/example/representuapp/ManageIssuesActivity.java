package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.UUID;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;


public class ManageIssuesActivity extends AppCompatActivity {

    Button cancel;
    Button add;
    ListView issuesList;
    boolean connected;
    DatabaseReference connectedRef;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference issues = database.getReference().child("issues");

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> idList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_issues);

        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        issuesList = (ListView) findViewById(R.id.issuesList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        issuesList.setAdapter(adapter);
        issuesList.setClickable(true);

        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    Toast.makeText(getApplicationContext(), "Database Disconnected. Check internet connection!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        issues.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayList.clear();
                idList.clear();

                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String id = childDataSnapshot.getKey();
                    String string = childDataSnapshot.child("title").getValue(String.class);
                    arrayList.add(string);
                    idList.add(id);
                    adapter.notifyDataSetChanged();
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
                    intent.putExtra("name", title);
                    intent.putExtra("id", idNum);
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