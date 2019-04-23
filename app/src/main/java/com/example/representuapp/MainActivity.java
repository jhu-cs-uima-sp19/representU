package com.example.representuapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A login screen that offers login via email/password.
 * -- implements LoaderCallbacks<Cursor> --
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    private Map<String, String> USERS = new Map<String, String>() {
        @Override
        public int size() {
            return USERS.size();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object username) {
            for (Map.Entry<String,String> entry : USERS.entrySet()) {
                if (entry.getKey().equals(username)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsValue(Object password) {
            for (Map.Entry<String,String> entry : USERS.entrySet()) {
                if (entry.getValue().equals(password)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String put(String username, String password) {
            if (username == null || password == null) {
                return null;
            }
            for (Map.Entry<String,String> entry : USERS.entrySet()) {
                if (entry.getKey().equals(username)) {
                    entry.setValue(password);
                    return password;
                }
            }
            USERS.put(username, password);
            return password;
        }

        @Override
        public String get(Object username) {
            if (username == null) { return null; }
            for (Map.Entry<String,String> entry : USERS.entrySet()) {
                if (entry.getKey().equals(username)) {
                    String password = entry.getValue();
                    return password;
                }
            }
            return null;
        }

        // Functions not implemented
        public boolean equals(Map<String, String> map) { return false; }
        public Set keySet() { return null; }
        public int hashCode() { return 0; }
        public String remove(Object username) { return null; }
        public Collection<String> values() { return null; }
        public Set entrySet() { return null; }
        public void putAll(Map<? extends String, ? extends String> hi) {}
        public void clear() {}
    };


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.etEmail);
        passwordView = (EditText) findViewById(R.id.password);

        //USERS.put("mboloix1@jhu.edu","mb");
        //USERS.put("pbaur1@jhu.edu","pb");
        //USERS.put("alohier1@jhu.edu","al");
        //USERS.put("user@jhu.edu","helloUser");

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
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    userUsername = childDataSnapshot.getKey();
                    userPassword = childDataSnapshot.getValue(String.class);
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
                validate(emailView.getText().toString(), passwordView.getText().toString());
            }
        });
    }

    private void validate(String username, String password) {
        //SGA and Admin login - launches SGA side of app
        if (username == null || password == null) {
            Toast.makeText(getApplicationContext(), "Username and/or Password fields CANNOT be empty!", Toast.LENGTH_LONG).show();
            emailView.setTextColor(getResources().getColor(R.color.red));
            passwordView.setTextColor(getResources().getColor(R.color.red));
        } else {
            emailView.setTextColor(getResources().getColor(R.color.colorPrimary));
            passwordView.setTextColor(getResources().getColor(R.color.colorPrimary));

            if ((username.equals(adminUsername)) && (password.equals(adminPassword))) {
                Intent intent = new Intent(MainActivity.this, SGAFeedActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else if (USERS.containsKey(username) && USERS.get(username).equals(password)) {
                Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                emailView.setTextColor(getResources().getColor(R.color.red));
                passwordView.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(), "Username and/or Password is Incorrect", LENGTH_SHORT).show();
            }
        }
    }

}

