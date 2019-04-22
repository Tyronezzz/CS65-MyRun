/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */


package com.example.myrun4.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class ActionTabsViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private static final int START = 0;
    private static final int HISTORY = 1;
    private static final String UI_TAB_START = "START";
    private static final String UI_TAB_HISTORY = "HISTORY";

    public ActionTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    // Return the Fragment associated with a specified position.
    public Fragment getItem(int pos){
        return fragments.get(pos);
    }

    // Return the number of views available
    public int getCount(){
        return fragments.size();
    }


    public CharSequence getPageTitle(int position) {

        switch (position) {
            case START:
                return UI_TAB_START;

            case HISTORY:
                return UI_TAB_HISTORY;

            default:
                break;
        }
        return null;
    }
}
