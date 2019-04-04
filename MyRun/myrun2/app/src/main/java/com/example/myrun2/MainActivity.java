/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-03
 */

package com.example.myrun2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Spinner mspinner_input;
    private Spinner mspinner_act;
    private BottomNavigationView mbottom_tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);


        mspinner_input = findViewById(R.id.input_type);
        mspinner_act= findViewById(R.id.activity_type);
        mbottom_tab = findViewById(R.id.bottom_navigation);

        mbottom_tab.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.input_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      // Specify the layout to use when the list of choices appears
        mspinner_input.setAdapter(adapter);     // Apply the adapter to the spinner


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.act_type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      // Specify the layout to use when the list of choices appears
        mspinner_act.setAdapter(adapter2);     // Apply the adapter to the spinner


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_start:
                    fragment = new main_start();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_history:
                    fragment = new main_history();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navigation_start, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {            // Inflate the menu
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_settings:


                return true;

            case R.id.action_edit_profile:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
