package com.example.representuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SGAStatisticsActivity extends AppCompatActivity {

    ListView issuesList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference issues = database.getReference().child("issues");
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> idList = new ArrayList<>();
    ArrayList<Integer> yeaList = new ArrayList<>();
    ArrayList<Integer> nayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgastatistics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Statistics");

        issuesList = (ListView) findViewById(R.id.issuesList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        issuesList.setAdapter(adapter);
        issuesList.setClickable(true);

        issues.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayList.clear();
                idList.clear();
                yeaList.clear();
                nayList.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String id = childDataSnapshot.getKey();
                    String string = childDataSnapshot.child("title").getValue(String.class);
                    int yeaNum = childDataSnapshot.child("votesYay").getValue(Integer.class);
                    int nayNum = childDataSnapshot.child("votesNay").getValue(Integer.class);
                    arrayList.add(string);
                    idList.add(id);
                    yeaList.add(yeaNum);
                    nayList.add(nayNum);
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
                Intent intent = new Intent(SGAStatisticsActivity.this, IssueStatisticsActivity.class);
                String title = arrayList.get(position);
                String idNum = idList.get(position);
                Integer yeaNum = yeaList.get(position);
                Integer nayNum = nayList.get(position);
                intent.putExtra("name", title);
                intent.putExtra("id", idNum);
                intent.putExtra("yeaNum", yeaNum);
                intent.putExtra("nayNum", nayNum);
                startActivity(intent);
            }
        });

    }

}
