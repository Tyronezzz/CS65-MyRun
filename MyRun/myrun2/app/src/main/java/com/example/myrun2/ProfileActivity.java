package com.example.myrun2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Profile");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar

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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
