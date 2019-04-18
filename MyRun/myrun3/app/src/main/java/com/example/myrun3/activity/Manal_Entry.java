/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun3.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.myrun3.ListAdapter;
import com.example.myrun3.MySQLiteHelper;
import com.example.myrun3.R;
import com.example.myrun3.model.ExerciseEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;


public class Manal_Entry extends AppCompatActivity{

    private static final String TAG = "Manual_entry";
    private static final String RESULT_LIST = "result_list";
    String[] mOptions = {"Activity","Date","Time","Duration","Distance","Calorie", "Heartbeat","Comment"};
    String[] mResults = {"Manual", "2019-01-01", "10:10", "0 mins", "0 kms", "0 cals", "0 bpm", " "};
    private Calendar mDateTime = Calendar.getInstance();
    private AsynWriteSQL writesqlhelper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manal__entry);

        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar


        if(savedInstanceState != null)
        {
            mResults = savedInstanceState.getStringArrayList(RESULT_LIST).toArray(new String[0]);
        }


        Intent intent = getIntent();
        String act_name = intent.getStringExtra("ACT");        // get the activity type name
        mResults[0] = act_name;

        final ListAdapter la = new ListAdapter(this, mOptions, mResults, 9);        // set up the listadapter
        ListView mlistView = findViewById(R.id.manual_listview);
        mlistView.setAdapter(la);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));

                switch (position){
                    case 1:
                        onDateClick(la);          // set the date
                        break;

                    case 2:
                        onTimeClick(la);        // set the time
                        break;

                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        showDiaglog(position);      // show the dialog
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
        outState.putStringArrayList(RESULT_LIST, new ArrayList<String>(Arrays.asList(mResults)));

    }

    public void onDateClick(final ListAdapter la) {             // set the date
        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                mDateTime.set(Calendar.YEAR, year);
                mDateTime.set(Calendar.MONTH, month);
                mDateTime.set(Calendar.DAY_OF_MONTH, day);

                mResults[1] = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day);
                la.notifyDataSetChanged();
            }
        };
        new DatePickerDialog(Manal_Entry.this, mDateListener,
                mDateTime.get(Calendar.YEAR),
                mDateTime.get(Calendar.MONTH),
                mDateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onTimeClick(final ListAdapter la) {       // set the time
        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {
                mDateTime.set(Calendar.HOUR_OF_DAY, hour);
                mDateTime.set(Calendar.MINUTE, minute);
                mResults[2] = String.valueOf(hour) + ":" + String.valueOf(minute);
                la.notifyDataSetChanged();
            }
        };

        new TimePickerDialog(Manal_Entry.this, mTimeListener,
                mDateTime.get(Calendar.HOUR_OF_DAY),
                mDateTime.get(Calendar.MINUTE), true).show();
    }


    private void showDiaglog(final int position)
    {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Manal_Entry.this);
        builder.setTitle(mOptions[position]);
        @SuppressLint("InflateParams") View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.input_dialog, null);
        final EditText input =  viewInflated.findViewById(R.id.edit_text);

        if(position==5 || position == 6)                 // strict the input type as number
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        if(position == 7)                                  // strict the input type as text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tmp_txt = input.getText().toString();
                if(position != 7)          //
                {
                    String[] arrOfStr = mResults[position].split("\\s+");
                    mResults[position] = tmp_txt + " " + arrOfStr[1];
                }

                else
                {
                    mResults[position] = tmp_txt;
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manual_menu, menu);     // Inflate the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    // operations on the toolbar
        switch (item.getItemId()) {

            case R.id.action_save:
                Log.d(TAG, "save it");

//                ExerciseEntry entryInsert = new ExerciseEntry();
                writesqlhelper = new AsynWriteSQL();       // execute the asyn task
                writesqlhelper.execute();




                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("StaticFieldLeak")
    class AsynWriteSQL extends AsyncTask<Void, Void, Void> {

        private String []entrylist;
        MySQLiteHelper mysqlhelper;

        AsynWriteSQL()
        {
            mysqlhelper = new MySQLiteHelper(getApplication());
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // insert
            ExerciseEntry entry = new ExerciseEntry(null, mResults[0], mResults[1]+" "+mResults[2],
                    mResults[3], mResults[4],null, null, mResults[5], null, mResults[6], mResults[7], null, null);

            mysqlhelper.insertEntry(entry);
            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {

            // update ui
            // fetch all activities
            // show it???

            ArrayList<ExerciseEntry> act_entries = mysqlhelper.fetchEntries();
            Log.d(TAG, "here " + act_entries.size());


            String[] act_title = new String[act_entries.size()];
            String[] act_des = new String[act_entries.size()];
            String[] act_datetime = new String[act_entries.size()];
            for(int i=0;i<act_entries.size();i++)
            {
                act_title[i] = act_entries.get(i).getActType();
                act_des[i] = act_entries.get(i).getDistance() + ", " + act_entries.get(i).getDuration();
                act_datetime[i] = act_entries.get(i).getDateTime();
            }


//            LayoutInflater inflater = Manal_Entry.this.getLayoutInflater();
//            @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_history, null,true);
//
//            main_history mh =
//
//
//
//            ListAdapter la = new ListAdapter(Manal_Entry.this, act_title, act_des, act_datetime, 99);        // set up the listadapter
//            ListView mlistView = rowView.findViewById(R.id.manual_hislistview);
//            if(mlistView == null)
//                Log.d(TAG, "sdcsdvsd");
//
//            mlistView.setAdapter(la);

        }
    }


}
