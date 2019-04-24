package com.example.representuapp;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.LENGTH_LONG;

/**
 * A login screen that offers login via email/password.
 * -- implements LoaderCallbacks<Cursor> --
 */
public class MainActivity extends AppCompatActivity {

    /*
    private class Map<String, String> {
        @Override
        public int size() {
            return USERS.size();
        }

        @Override
        public boolean isEmpty() {
            return (USERS.size() == 0);
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
                return "null";
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
                    return entry.getValue();
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
    }; */

    public class HashMap {

        // better re-sizing is taken as 2^4
        private final int[] SIZE = {16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};
        private Entry table[] = new Entry[SIZE[0]];
        private int fill = 0;
        private int index = 0;

        /**
         * To store the Map data in key and value pair.
         * Using linear probing by 1
         */
        class Entry {
            final String key;
            String value;
            boolean collided;

            Entry(String k, String v) {
                key = k;
                value = v;
                collided = false;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getKey() {
                return key;
            }
        }

        /**
         * Returns the entry mapped to key in the HashMap.
         */
        public Entry get(String k) {
            int hash = Math.abs(mod(k.hashCode(), SIZE[index]));
            Entry e = table[hash];

            if (e.collided && !e.key.equals(k)) {
                //split for loops for efficiency
                for (int i = hash; i < SIZE[index]; i++) {
                    if (table[i].key.equals(k)) {
                        return table[i];
                    }
                }
                for (int i = 0; i < hash; i++) {
                    if (table[i].key.equals(k)) {
                        return table[i];
                    }
                }
                return null;

            } else if (e.key.equals(k)) { return e; }

            return null;
        }

        /** to help with hash **/
        private int mod(int x, int y) {
            int result = x % y;
            if (result < 0)
                result += y;
            return result;
        }

        /**
         * If the map previously contained a mapping for the key, the old
         * value is replaced.
         * If there is a collision, original Entry marked, hash increases until
         * no collisions, places new Entry
         */
        public void put(String k, String v) {
            int hash = Math.abs(mod(k.hashCode(),SIZE[index]));
            table = resize(table);
            Entry e = table[hash];

            if(e != null) {
                if(e.key.equals(k)) {
                    e.value = v;
                } else {
                    // Collision, probe
                    e.collided = true;
                    while (table[hash] != null) {
                        if (hash + 1 < SIZE[index]) {
                            hash++;
                        } else { hash = 0; }
                    }
                    e = new Entry(k, v);
                    table[hash]= e;
                    fill++;
                }
            } else {
                table[hash]= new Entry(k, v);
                fill++;
            }
        }

        private Entry[] resize(Entry[] table) {
            //if table has filled up
            if (fill + 1 >= SIZE[index]) {
                //create a new one
                Entry new_table[] = new Entry[SIZE[index + 1]];
                //populate it
                for (int i = 0; i <= SIZE[index]; i++) {
                    new_table[i] = table[i];
                }
                //increase index
                index++;
                return new_table;
            }
            return table;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            for (int i = 0; i < SIZE[index]; i++){
                if (table[i] != null) {
                    sb.append("(k:");
                    if (table[i].key == null) {
                        sb.append("null");
                    } else {
                        sb.append(table[i].key);
                    }
                    sb.append(" v:");
                    if (table[i].value == null) {
                        sb.append("null");
                    } else {
                        sb.append(table[i].value);
                    }
                    sb.append("), ");
                } else {
                    sb.append("(NULL), ");
                }
            }
            sb.append(" }");
            return sb.toString();
        }
    }


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
        username = username.replace("@jhu.edu","");
        //SGA and Admin login - launches SGA side of app
        if (username == null || password == null) {
            Toast.makeText(getApplicationContext(), "Username and/or Password fields CANNOT be empty!", Toast.LENGTH_LONG).show();
            emailView.setTextColor(getResources().getColor(R.color.red));
            passwordView.setTextColor(getResources().getColor(R.color.red));
        } else {
            emailView.setTextColor(getResources().getColor(R.color.colorPrimary));
            passwordView.setTextColor(getResources().getColor(R.color.colorPrimary));

            //if given username is not in USERS
            if (USERS.get(username) == null) {
                emailView.setTextColor(getResources().getColor(R.color.red));
                passwordView.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(), "Username is Incorrect", LENGTH_SHORT).show();
                //if login had admin credentials
            } else if ((username.equals(adminUsername)) && (password.equals(adminPassword))) {
                Intent intent = new Intent(MainActivity.this, SGAFeedActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                //if username, password combo works
            } else if (USERS.get(username).getValue().equals(password)) {
                Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                //if username, password combo doesn't work
                emailView.setTextColor(getResources().getColor(R.color.red));
                passwordView.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(), "Username and/or Password is Incorrect", LENGTH_SHORT).show();
            }
        }
    }

}

