package com.example.myrun2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Objects;


public class Manal_Entry extends AppCompatActivity {

    private static final String TAG = "Manual_entry";
    String[] mOptions = {"Activity","Date","Time","Duration","Distance","Calorie", "Heartbeat","Comment"};
    String[] mResults = {"Manual", "2019-01-01", "10:10", "0 mins", "0 kms", "0 cals", "0 bpm", " "};
    private ListView mlistView;
    private Calendar mDateTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manal__entry);

        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar



        ListAdapter la = new ListAdapter(this, mOptions, mResults);
        mlistView = findViewById(R.id.manual_listview);
        mlistView.setAdapter(la);


        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(TAG, String.valueOf(position));

                switch (position){
                    case 1:
                        onDateClick();
                         break;

                    case 3:
                    case 4:
                        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Manal_Entry.this);
                        builder.setTitle(mOptions[position]);
                        View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.input_dialog, null);
                        final EditText input =  viewInflated.findViewById(R.id.edit_text);
                        builder.setView(viewInflated);

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String m_Text = input.getText().toString();
                                mResults[position] = m_Text;

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

    public void onDateClick() {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                mDateTime.set(Calendar.YEAR, year);
                mDateTime.set(Calendar.MONTH, month);
                mDateTime.set(Calendar.DAY_OF_MONTH, day);
//                String tmp = DateUtils.formatDateTime(getApplicationContext(),
//                        mDateTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE
//                                | DateUtils.FORMAT_SHOW_TIME);
                Log.d(TAG,  String.valueOf(year));
            }
        };
        new DatePickerDialog(Manal_Entry.this, mDateListener,
                mDateTime.get(Calendar.YEAR),
                mDateTime.get(Calendar.MONTH),
                mDateTime.get(Calendar.DAY_OF_MONTH)).show();

    }


}
