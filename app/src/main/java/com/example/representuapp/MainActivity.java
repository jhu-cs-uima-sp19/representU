package com.example.representuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static android.widget.Toast.LENGTH_SHORT;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView emailView;
    private EditText passwordView;
    Button signInButton;
    public String adminUsername;
    public String adminPassword;
    private FirebaseDatabase database;
    private DatabaseReference a_pass;
    private DatabaseReference a_usnm;
    private DatabaseReference user_login;
    String userUsername;
    String userPassword;
    final HashMap USERS = new HashMap();
    String JHED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        emailView = findViewById(R.id.etEmail);
        passwordView = findViewById(R.id.etPassword);


        // Update Admin login
        database = FirebaseDatabase.getInstance();
        a_pass = database.getReference().child("adminPassword");
        a_usnm = database.getReference().child("adminUsername");
        user_login = database.getReference().child("users");

        a_pass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                adminPassword = snapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        a_usnm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                adminUsername = snapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        user_login.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot users : snapshot.getChildren()) {
                    Log.d("children", snapshot.getChildren().toString());
                    userUsername = users.child("username").getValue(String.class);
                    userPassword = users.child("password").getValue(String.class);
                    USERS.put(userUsername, userPassword);
                    Log.d("userUsername", userUsername);
                    Log.d("userPassword", userPassword);
                }
                Log.d("hashmap yay", USERS.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton = (Button) findViewById(R.id.btnLogin);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                Log.d("get_key", emailView.getText().toString());
                Log.d("get_val", passwordView.getText().toString());
                validate(emailView.getText().toString(), passwordView.getText().toString());
            }
        });
    }

    private void validate(String username, String password) {
        if (username == null || password == null) {
            //username and password cant be null
            Toast.makeText(getApplicationContext(), "Username and/or Password fields CANNOT be empty!", Toast.LENGTH_LONG).show();
            emailView.setTextColor(getResources().getColor(R.color.red));
            passwordView.setTextColor(getResources().getColor(R.color.red));
        } else if ((username.equals(adminUsername)) && (password.equals(adminPassword))) {
            emailView.setTextColor(getResources().getColor(R.color.colorPrimary));
            passwordView.setTextColor(getResources().getColor(R.color.colorPrimary));
            //SGA and Admin login - launches SGA side of app
            Intent intent = new Intent(MainActivity.this, SGAFeedActivity.class);
            JHED = username.replace("@jhu.edu","");
            intent.putExtra("JHED", JHED);
            Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
            emailView.setTextColor(getResources().getColor(R.color.colorPrimary));
            passwordView.setTextColor(getResources().getColor(R.color.colorPrimary));
            username = username.replace("@jhu.edu","");
            if (USERS.get(username) == null) {
                emailView.setTextColor(getResources().getColor(R.color.red));
                passwordView.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(), "Username is Incorrect", Toast.LENGTH_LONG).show();
                //if login had admin credentials
            } else if (USERS.get(username).getValue().equals(password)) {
                Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
                JHED = username;
                intent.putExtra("JHED", JHED);
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            } else {
                //if username, password combo doesn't work
                emailView.setTextColor(getResources().getColor(R.color.red));
                passwordView.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(), "Username and/or Password is Incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }

}

