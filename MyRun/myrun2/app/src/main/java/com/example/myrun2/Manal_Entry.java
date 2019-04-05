package com.example.myrun2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.Objects;

public class Manal_Entry extends AppCompatActivity {

    String[] nameArray = {"Activity","Date","Time","Duration","Distance","Calorie", "Heartbeat","Comment"};

    String[] infoArray = {
            "Manual",
            "2019-01-01",
            "10:10",
            "0 mins",
            "0 kms",
            "0 cals",
            "0 bpm",
            " "
    };

    private ListView mlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manal__entry);

        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar



        ListAdapter whatever = new ListAdapter(this, nameArray, infoArray);
        mlistView = (ListView) findViewById(R.id.manual_listview);
        mlistView.setAdapter(whatever);
    }











}
