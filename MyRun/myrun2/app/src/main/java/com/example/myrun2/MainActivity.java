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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct.lifecycle";

    private BottomNavigationView mbottom_tab;
    private int idx = 0;
    private ArrayList<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);


        fragments = new ArrayList<Fragment>();
        fragments.add(new main_start());
        fragments.add(new main_history());

        mbottom_tab = findViewById(R.id.bottom_navigation);
        mbottom_tab.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        if(savedInstanceState != null)
        {
            idx = savedInstanceState.getInt("FRAG_INDEX", 0);
        }
        Log.d(TAG, "Creaet IDX "+String.valueOf(idx));

        loadFragment(fragments.get(idx));

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "IDX "+String.valueOf(idx));
        outState.putInt("FRAG_INDEX", idx);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_start:
                    fragment = new main_start();
                    idx=0;
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_history:
                    fragment = new main_history();
                    idx=1;
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
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
