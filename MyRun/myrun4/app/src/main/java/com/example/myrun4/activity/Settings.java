/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun4.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.example.myrun4.ListAdapter;
import com.example.myrun4.R;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings.lifecycle";
    private static final String IDX = "idx";
    public int REQUEST_SETTING = 0;
    public int REQUEST_PREFERENCE = 1;
    public int REQUEST_SIGNOUT = 2;
    AlertDialog alertDialog1;
    private int idx = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String[] moptions ={"Unit Preference", "Webpage"};
    private String[] values = {"Metric(Kilometers)", "Imperial(Miles)"};
    String[] mresults = {"Select the unit", "https://www.cs.dartmouth.edu/~campbell/cs65/cs65.html"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        myToolbar.setTitle("Settings");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar

        if(savedInstanceState != null)
            idx = savedInstanceState.getInt(IDX, 0);

        // set up the Privacy Setting
        final ListAdapter la = new ListAdapter(this, new String[]{"Privacy Setting"}, new String[]{"Posting your records anonymously"}, sharedPreferences, REQUEST_SETTING);
        ListView mlistView = findViewById(R.id.app_setting_listview);
        mlistView.setAdapter(la);
        la.notifyDataSetChanged();

        // set up the Unit Preference and Webpage
        final ListAdapter la2 = new ListAdapter(this, moptions, mresults, REQUEST_PREFERENCE);
        ListView mlistView2 = findViewById(R.id.app_setting_listview2);
        mlistView2.setAdapter(la2);
        la2.notifyDataSetChanged();

        // set up sign out
        final ListAdapter la3 = new ListAdapter(this, new String[]{"Sign Out"}, new String[]{""}, REQUEST_SIGNOUT);
        ListView mlistView3 = findViewById(R.id.app_setting_listview3);
        mlistView3.setAdapter(la3);
        la3.notifyDataSetChanged();

        mlistView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, String.valueOf(position));

            switch (position){
                case 0:
                    sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
                    editor = sharedPreferences.edit();
                    boolean ischecked = sharedPreferences.getBoolean("key_privacy_set", false);
                    editor.putBoolean("key_privacy_set", !ischecked);
                    editor.apply();
                    la.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        });

        mlistView2.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, String.valueOf(position));
            Intent intent;
            switch (position){
                case 0:                 // click unit preference and show the dialog
                    showRadioDialog();
                    break;

                case 1:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mresults[1]));     // jump to the Uri
                    startActivity(intent);
                    break;
                default:
                    break;
            }

        });

        mlistView3.setOnItemClickListener((parent, view, position, id) -> {

            switch (position){
                case 0:             // click sign out and return the sign in activity
                    Intent intent = new Intent(Settings.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

                default:
                    break;
            }

        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(IDX, idx);
    }

    private void showRadioDialog() {
        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        idx = sharedPreferences.getInt("key_unit_pre", 0);

        final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setTitle("Unit Preference");

        // set the choice last time
        builder.setSingleChoiceItems(values, idx, (dialog, item) -> {         // store the choice this time
            idx = item;
            sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
            editor = sharedPreferences.edit();
            editor.putInt("key_unit_pre", idx);
            editor.apply();
            alertDialog1.dismiss();
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        alertDialog1 = builder.create();
        alertDialog1.show();

    }
}
