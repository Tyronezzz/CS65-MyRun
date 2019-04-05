package com.example.myrun2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings.lifecycle";
    private ListView mlistView;

    String[] moptions ={"Privacy Setting", "Unit Preference", "Webpage"};
    String[] mresults = {"Posting your records anonymously", "Select the unit", "https://www.cs.dartmouth.edu/~campbell/cs65/cs65.html"};
    private boolean isSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        myToolbar.setTitle("Settings");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar


        final ListAdapter la = new ListAdapter(this, moptions, mresults, isSelect);
        mlistView = findViewById(R.id.app_setting_listview);
        mlistView.setAdapter(la);
        la.notifyDataSetChanged();

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));

                switch (position){
                    case 0:
                    case 1:

                        break;
                    case 2:
//                    case 3:
//                    case 4:
                        break;

                    default:
                        break;
                }

            }
        });


    }


}
