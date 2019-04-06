package com.example.myrun2;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends RegisterActivity {

    private static final String TAG = "Profile.lifecyc";
//    private AutoCompleteTextView mNameView;
//    private RadioGroup mGender;
//    private AutoCompleteTextView mEmailView;
//    private EditText mPasswordView;
//    private AutoCompleteTextView mPhone;
//    private AutoCompleteTextView mMajor;
//    private AutoCompleteTextView mClass;
//    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Profile");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar




//        mImageView = findViewById(R.id.imageView);
//        mNameView = findViewById(R.id.register_name);
//        mEmailView = findViewById(R.id.register_email);
//        mPasswordView = findViewById(R.id.register_password);
//        mPhone = findViewById(R.id.register_phone);
//        mMajor = findViewById(R.id.register_major);
//        mClass = findViewById(R.id.register_class);
//        mGender = findViewById(R.id.register_gender);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.register_menu, menu);     // Inflate the menu
        MenuItem item = menu.findItem(R.id.action_register);
        item.setTitle("SAVE");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    // operations on the toolbar
        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;

            case R.id.action_register:
                Log.d(TAG, "save...");
                checkValid();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void checkValid()
    {
        mNameView.setError(getString(R.string.error_field_required));
        Log.d(TAG,"fk");
        //super.checkValid();
    }

    @Override
    protected void attemptRegister(boolean cancel, View focusView, ArrayList<String> putstr, ArrayList<Integer> putint) {

        Log.d(TAG, putstr.get(1));
        if (cancel) {
            focusView.requestFocus();
            Log.d(TAG, "GG");

        }
        else {

            Log.d(TAG, "SUC");
        }

    }




}
