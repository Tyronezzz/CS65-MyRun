/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-03
 */

package com.example.myrun2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
    private ViewPager viewPager;
    private ActionTabsViewPagerAdapter myViewPageAdapter;
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);


        fragments = new ArrayList<Fragment>();
        fragments.add(new main_start());
        fragments.add(new main_history());

        viewPager = findViewById(R.id.viewpager);
        // create a fragment list in order.
        // use FragmentPagerAdapter to bind the TabLayout (tabs with different titles)
        // and ViewPager (different pages of fragment) together.
        myViewPageAdapter =new ActionTabsViewPagerAdapter(this.getSupportFragmentManager(),
                fragments);
        // add the PagerAdapter to the viewPager
        viewPager.setAdapter(myViewPageAdapter);



        mbottom_tab = findViewById(R.id.bottom_navigation);
        mbottom_tab.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState != null)
        {
            idx = savedInstanceState.getInt("FRAG_INDEX", 0);
        }
        Log.d(TAG, "Creaet IDX "+String.valueOf(idx));

//        loadFragment(fragments.get(idx));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    mbottom_tab.getMenu().getItem(0).setChecked(false);

                mbottom_tab.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mbottom_tab.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                    viewPager.setCurrentItem(0);
                    //loadFragment(fragment);
                    return true;
                case R.id.navigation_history:
                    fragment = new main_history();
                    idx=1;
                    viewPager.setCurrentItem(1);
                    //loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

//    private void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {            // Inflate the menu
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;

            case R.id.action_edit_profile:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
