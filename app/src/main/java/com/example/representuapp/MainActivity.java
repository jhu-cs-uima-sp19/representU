package com.example.representuapp;

import android.content.Intent;
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

/**
 * A login screen that offers login via email/password.
 * -- implements LoaderCallbacks<Cursor> --
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View progressView;
    private View loginFormView;
    Button signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.etEmail);
        //populateAutoComplete();

        passwordView = (EditText) findViewById(R.id.password);
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

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
    }

    private void validate(String username, String password) {
        //SGA and Admin login - launches SGA side of app
        if ((username.equals("admin@jhu.edu")) && (password.equals("helloAdmin"))) {
            Intent intent = new Intent(MainActivity.this, SGAFeedActivity.class);
            startActivity(intent);
        } else if ((username.equals("user@jhu.edu")) && (password.equals("helloUser"))) {
            Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
            startActivity(intent);
        } else if (username.endsWith("@jhu.edu")) {
            Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
            startActivity(intent);
        }
    }

}

