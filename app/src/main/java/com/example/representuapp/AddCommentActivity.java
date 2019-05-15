package com.example.representuapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class AddCommentActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    final private DatabaseReference dbRef = database.getReference();

    private ArrayList<Comment> comments;
    private String JHED;
    private String idString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        JHED = intent.getStringExtra("JHED");
        idString = intent.getStringExtra("id");
        saveComment();
    }


    public void saveComment() {
        final EditText comMainText = findViewById(R.id.mainText);
        final Switch anon = findViewById(R.id.anon_switch);
        Button saveButton = findViewById(R.id.postButton);




        //DataSnapshot x;
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if((comMainText.getText().toString()).equals(null) || (comMainText.getText().toString()).equals("") ) {
                    Toast.makeText(getApplicationContext(), "Error: Please Fill All Fields", LENGTH_SHORT).show();
                } else {
                    dbRef.child("comments").child(idString).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            GenericTypeIndicator<ArrayList<Comment>> gti =new GenericTypeIndicator<ArrayList<Comment>>(){};
                            comments = snapshot.getValue(gti);
                            if (comments == null) {
                                comments = new ArrayList<>();
                                comments.add(new Comment("", ""));
                            }

                            Log.d("HELLO", comments.toString());
                            //comments = arryalist that exists currently

                            if (anon.isChecked()) {
                                Comment newCom = new Comment("Anonymous", comMainText.getText().toString());
                                comments.add(newCom);
                            } else {
                                Comment newCom = new Comment(JHED, comMainText.getText().toString());
                                comments.add(newCom);
                            }
                            dbRef.child("comments").child(idString).child("comments").setValue(comments);
                        }
//                        x.setValue(comments);
                        //dbRef.child("comments").child(idString).child("comments").setValue(comments);
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                finish();
            }
        });
    }
}
