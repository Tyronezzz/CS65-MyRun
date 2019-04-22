/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-03
 */

package com.example.myrun4.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myrun4.R;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private static final String TAG = "LoginActivity.lfcyc";
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    Button mregister_button;
    Button mEmailSignInButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(getResources().getText(R.string.action_sign_in));
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mregister_button = findViewById(R.id.register_button);

        Toolbar mytoolbar = findViewById(R.id.login_toolbar);     // set up the toolbar
        setSupportActionBar(mytoolbar);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {         // log in
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mregister_button.setOnClickListener(new OnClickListener() {           // register
            @Override
            public void onClick(View v) {
                try {

                    Intent k = new Intent(LoginActivity.this, MainActivity.class);     //jump to mainactivity
                    startActivity(k);

//                    Intent k = new Intent(LoginActivity.this, RegisterActivity.class);
//                    k.putExtra("PARENTNAME", "LOGIN");
//                    startActivity(k);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState333");
    }


    // attempt to login in
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if(!isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {         // the input field is valid and try to log in. Show the progress bar
            mshowProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void mshowProgress(final boolean show) {   // call this function when show the progress bar
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);         // hide the login form
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);       // show the progress bar
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    //An asynchronous login task used to authenticate the user.
    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // check whether email and password is match
            sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
            String old_email= sharedPreferences.getString("key_email", "");
            String old_pwd = sharedPreferences.getString("key_password", "");

            // not match
            if(old_email == null || old_pwd == null)
                return false;
            return old_email.equals(mEmail) && old_pwd.equals(mPassword);
        }

        @Override
        protected void onPostExecute(final Boolean success) {      // execute after doInBackground
            mAuthTask = null;
            mshowProgress(false);

            if (success) {
                Log.d(TAG, "SUCCESS");
                finish();
                try {
                    Intent k = new Intent(LoginActivity.this, MainActivity.class);     //jump to mainactivity
                    startActivity(k);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "FAILED");          // failed, try again
                Toast.makeText(getApplicationContext(), "Email or password is incorrect!", Toast.LENGTH_LONG).show();
                mEmailView.requestFocus();
                //mEmailView.setError("");
            }
        }
    }
}

