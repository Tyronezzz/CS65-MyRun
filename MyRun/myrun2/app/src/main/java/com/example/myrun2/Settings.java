package com.example.myrun2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings.lifecycle";
    private static final String IDX = "idx";
    public int REQUEST_SETTING = 0;
    public int REQUEST_PREFERENCE = 1;
    public int REQUEST_SIGNOUT = 2;
    private String[] values = {"Metric(Kilometers)", "Imperial(Miles)"};
    private Switch mswitch;
    AlertDialog alertDialog1;
    private int idx = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String[] moptions ={"Unit Preference", "Webpage"};
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

        boolean isSelect = false;
        final ListAdapter la = new ListAdapter(this, new String[]{"Privacy Setting"}, new String[]{"Posting your records anonymously"}, isSelect, REQUEST_SETTING);
        ListView mlistView = findViewById(R.id.app_setting_listview);
        mlistView.setAdapter(la);
        la.notifyDataSetChanged();

        final ListAdapter la2 = new ListAdapter(this, moptions, mresults, REQUEST_PREFERENCE);
        ListView mlistView2 = findViewById(R.id.app_setting_listview2);
        mlistView2.setAdapter(la2);
        la2.notifyDataSetChanged();

        final ListAdapter la3 = new ListAdapter(this, new String[]{"Sign Out"}, new String[]{""}, REQUEST_SIGNOUT);
        ListView mlistView3 = findViewById(R.id.app_setting_listview3);
        mlistView3.setAdapter(la3);
        la3.notifyDataSetChanged();


        @SuppressLint("InflateParams") View inflatedView = getLayoutInflater().inflate(R.layout.listview_row_clip_btn, null);
        mswitch =  inflatedView.findViewById(R.id.myswitch);
//        mswitch.setChecked(true);
        //mswitch.clearFocus();

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));

                switch (position){
                    case 0:
                        if(mswitch == null)
                        {
                            Log.d(TAG, "Sc");
                        }
                        else
                        {
                            Log.d(TAG, String.valueOf(mswitch.isChecked()));

//                            mswitch.setChecked(!mswitch.isChecked());
//                            la2.notifyDataSetChanged();

//                            mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                                }
//                            });

//                            mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    // do something, the isChecked will be
//                                    // true if the switch is in the On position
//
//                                    mswitch.setChecked(isChecked);
//                                }
//                            });
                        }

                        break;
                    default:
                        break;
                }

            }
        });

        mlistView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));
                Intent intent;
                switch (position){
                    case 0:
                        showRadioDialog();
                        break;

                    case 1:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mresults[1]));
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

            }
        });

        mlistView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(Settings.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }

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

        builder.setSingleChoiceItems(values, idx, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                idx = item;
                sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
                editor = sharedPreferences.edit();
                editor.putInt("key_unit_pre", idx);
                editor.apply();
                alertDialog1.dismiss();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog1 = builder.create();
        alertDialog1.show();

    }


}
