package com.example.representuapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    AutoCompleteTextView emailView;
    EditText passwordView;
    Button signInButton;
    ImageView image;
    public String adminUsername;
    public String adminPassword;
    public String userUsername;
    public String userPassword;
    final HashMap USERS = new HashMap();
    public String JHED;
    DatabaseReference connectedRef;
    boolean connected;

    private SharedPreferences myPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        setTitle(R.string.login);

        emailView = findViewById(R.id.etEmail);
        emailView.setFitsSystemWindows(true);
        emailView.setCursorVisible(false);
        passwordView = findViewById(R.id.etPassword);
        passwordView.setFitsSystemWindows(true);
        signInButton = findViewById(R.id.btnLogin);
        image = findViewById(R.id.imageView2);
        image.setVisibility(View.VISIBLE);
        showSoftKeyboard(signInButton);
        emailView.setCursorVisible(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a_pass = database.getReference().child("adminPassword");
        DatabaseReference a_usnm = database.getReference().child("adminUsername");
        DatabaseReference user_login = database.getReference().child("users");

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
                    userUsername = users.child("username").getValue(String.class);
                    userPassword = users.child("password").getValue(String.class);
                    USERS.put(userUsername, userPassword);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL);
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connected) {
                    Toast.makeText(getApplicationContext(), "Database Disconnected. Check internet connection!", Toast.LENGTH_LONG).show();
                } else {
                    validate(emailView.getText().toString(), passwordView.getText().toString());
                }
            }
        });

        //TODO: Write an if statement to adjust size of picture when the orientation is landscape
    }

    private void validate(String username, String password) {

        if (username == null || password == null) {
            //username and password cant be null
            Toast.makeText(getApplicationContext(), "Username and/or Password fields CANNOT be empty!", Toast.LENGTH_LONG).show();
            emailView.setTextColor(getResources().getColor(R.color.red));
            passwordView.setTextColor(getResources().getColor(R.color.red));
        } else if ((username.equals(adminUsername)) && (password.equals(adminPassword))) {
            //SGA and Admin - launches SGA side of app
            emailView.setTextColor(getResources().getColor(R.color.colorPrimary));
            passwordView.setTextColor(getResources().getColor(R.color.colorPrimary));

            SharedPreferences.Editor peditor = myPrefs.edit();
            peditor.putString("JHED", JHED);
            peditor.commit();

            Intent intent = new Intent(MainActivity.this, SGAFeedActivity.class);
            JHED = username.replace("@jhu.edu","");
            intent.putExtra("JHED", JHED);
            Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
            emailView.setTextColor(getResources().getColor(R.color.colorPrimary));
            passwordView.setTextColor(getResources().getColor(R.color.colorPrimary));
            JHED = username.replace("@jhu.edu","");
            if (USERS.get(JHED) == null) {
                emailView.setTextColor(getResources().getColor(R.color.red));
                passwordView.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(), "Username is Incorrect", Toast.LENGTH_LONG).show();
                //if login had admin credentials
            } else if (USERS.get(JHED).getValue().equals(password)) {
                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.putString("JHED", JHED);
                peditor.commit();

                Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
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

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}

