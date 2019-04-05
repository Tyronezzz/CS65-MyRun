package com.example.myrun2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Objects;

public class Manal_Entry extends AppCompatActivity {

    private static final String TAG = "Manual_entry";
    String[] mOptions = {"Activity","Date","Time","Duration","Distance","Calorie", "Heartbeat","Comment"};
    String[] mResults = {"Manual", "2019-01-01", "10:10", "0 mins", "0 kms", "0 cals", "0 bpm", " "};
    private ListView mlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manal__entry);

        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar



        ListAdapter whatever = new ListAdapter(this, mOptions, mResults);
        mlistView = findViewById(R.id.manual_listview);
        mlistView.setAdapter(whatever);


        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));

                switch (position){
                    case 0:
                         break;

                    case 3:
                    case 4:
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Manal_Entry.this);
                        builder.setTitle("Title");
// I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
                        View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.input_dialog, null);
// Set up the input
                        final EditText input = (EditText) viewInflated.findViewById(R.id.edit_text);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        builder.setView(viewInflated);

// Set up the buttons
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String m_Text = input.getText().toString();
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                        break;

                    case 5:
                    case 6:

                        break;

                    default:
                        break;




                }


            }
        });

    }











}
