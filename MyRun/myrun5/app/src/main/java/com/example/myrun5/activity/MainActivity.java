/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-03
 */

package com.example.myrun5.activity;

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

import com.example.myrun5.fragment.main_board;
import com.example.myrun5.utils.ActionTabsViewPagerAdapter;
import com.example.myrun5.R;
import com.example.myrun5.fragment.main_history;
import com.example.myrun5.fragment.main_start;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct.lifecycle";
    private BottomNavigationView mbottom_tab;
    private int idx = 0;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);


        ArrayList<Fragment> fragments = new ArrayList<>();     // add fragments to the mainactivity
        main_start ms = new main_start();
        fragments.add(ms);
        fragments.add(new main_history());
        fragments.add(new main_board());

        viewPager = findViewById(R.id.viewpager);
        ActionTabsViewPagerAdapter myViewPageAdapter = new ActionTabsViewPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myViewPageAdapter);     // add the PagerAdapter to the viewPager


        mbottom_tab = findViewById(R.id.bottom_navigation);
        mbottom_tab.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);   // set up the bottom tab

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "IDX "+String.valueOf(idx));
        outState.putInt("FRAG_INDEX", idx);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener      // bottom tab
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Toolbar myToolbar = findViewById(R.id.main_toolbar);
            Menu nav_Menu = myToolbar.getMenu();


            switch (item.getItemId()) {
                case R.id.navigation_start:            // click "start" and start fragment
                    fragment = new main_start();
                    idx=0;
                    viewPager.setCurrentItem(0);
                    nav_Menu.findItem(R.id.action_refresh).setVisible(false);
                    return true;

                case R.id.navigation_history:        // click "history" and start fragment
                    fragment = new main_history();
                    idx=1;
                    viewPager.setCurrentItem(1);
                    nav_Menu.findItem(R.id.action_refresh).setVisible(true);
                    return true;

                case R.id.navigation_board:           // click "board" and start fragment
                    fragment = new main_history();
                    idx=2;
                    viewPager.setCurrentItem(2);
                    nav_Menu.findItem(R.id.action_refresh).setVisible(true);
                    return true;

            }
            return false;
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {            // Inflate the menu
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);

        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        Menu nav_Menu = myToolbar.getMenu();
        nav_Menu.findItem(R.id.action_refresh).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.action_settings:      // click settings
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;

            case R.id.action_edit_profile:      // click edit profile
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("PARENTNAME", "MAIN");
                startActivity(intent);
                return true;

            case R.id.action_refresh:
                // get the data from firebase

                return false;

//            default:
//                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
