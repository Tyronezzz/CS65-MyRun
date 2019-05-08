/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun5.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myrun5.ListAdapter;
import com.example.myrun5.MySQLiteHelper;
import com.example.myrun5.R;
import com.example.myrun5.fragment.main_history;
import com.example.myrun5.model.ExerciseEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Manal_Entry extends AppCompatActivity{

    private static final String TAG = "Manual_entry";
    private static final String RESULT_LIST = "result_list";
    String[] mOptions = {"Activity","Date","Time","Duration","Distance","Calorie", "Heartbeat","Comment"};
    String[] mResults = {"", "2019-01-01", "10:10", "0 mins", "0 kms", "0 cals", "0 bpm", " "};
    private Calendar mDateTime = Calendar.getInstance();
    private String parentName;
    private long index;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
//    private ListAdapter mAdapter;

    ListView mhisView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manal__entry);

        Toolbar myToolbar = findViewById(R.id.manual_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar


        Intent intent = getIntent();
        parentName = intent.getStringExtra("PARENTNAME");        // get the parent activity name
        String act_name = intent.getStringExtra("ACT");        // get the activity type name
//        String act_type = intent.getStringExtra("TYPE");
        index = intent.getLongExtra("INDEX", 0);


        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase = FirebaseDatabase.getInstance().getReference();




        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());  // set the current date
        mResults[1] = date;

        Calendar tmpcld = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");     // set the current time
        String strDate = mdformat.format(tmpcld.getTime());
        mResults[2] = strDate;


        if(parentName.equals("MAINHISTORY"))      // from mainhistory, to delete
        {

            ExerciseEntry entry = (ExerciseEntry) getIntent().getSerializableExtra("EXENTRY");
            String[] arrOfStr = entry.getDateTime().split("\\s+");

            mResults[0] = entry.getActType();
            mResults[1] = arrOfStr[0];
            mResults[2] = arrOfStr[1];
            mResults[3] = entry.getDuration();
            mResults[4] = entry.getDistance();
            mResults[5] = entry.getCalorie();
            mResults[6] = entry.getHeartrate();
            mResults[7] = entry.getComment();

            sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
            int km_mile_idx = sharedPreferences.getInt("key_unit_pre", 0);
            String[] substr = mResults[4].split("\\s+");
            String tmpdis = mResults[4];
            if(km_mile_idx == 1 && tmpdis.contains("kms"))
            {
                tmpdis = String.valueOf(Double.parseDouble(substr[0])*0.621) + " miles";
            }

            else if(km_mile_idx == 0 && tmpdis.contains("mile"))
            {
                tmpdis = String.valueOf(Double.parseDouble(substr[0])*1.609) + " kms";
            }

            mResults[4] = tmpdis;

            final ListAdapter la_history = new ListAdapter(this, mOptions, mResults, 9);        // set up the listadapter
            ListView mlistView = findViewById(R.id.manual_listview);
            mlistView.setAdapter(la_history);
            mlistView.setEnabled(false);
        }

        else
        {
            if(savedInstanceState != null)
            {
                mResults = savedInstanceState.getStringArrayList(RESULT_LIST).toArray(new String[0]);
            }

            mResults[0] = act_name;


            sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
            int km_mile_idx = sharedPreferences.getInt("key_unit_pre", 0);

            String[] substr = mResults[4].split("\\s+");
            if(km_mile_idx == 1)
                mResults[4] = substr[0] + " miles";

            else
                mResults[4] = substr[0] + " kms";


            final ListAdapter la = new ListAdapter(this, mOptions, mResults, 9);        // set up the listadapter
            ListView mlistView = findViewById(R.id.manual_listview);
            mlistView.setAdapter(la);


            final LayoutInflater factory = getLayoutInflater();
            @SuppressLint("InflateParams") final View textEntryView = factory.inflate(R.layout.fragment_main_history, null);
            mhisView = textEntryView.findViewById(R.id.manual_hislistview);


            mlistView.setOnItemClickListener((parent, view, position, id) -> {
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

            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(RESULT_LIST, new ArrayList<>(Arrays.asList(mResults)));

    }

    public void onDateClick(final ListAdapter la) {             // set the date
        DatePickerDialog.OnDateSetListener mDateListener = (view, year, month, day) -> {
            mDateTime.set(Calendar.YEAR, year);
            mDateTime.set(Calendar.MONTH, month);
            mDateTime.set(Calendar.DAY_OF_MONTH, day);

            mResults[1] = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day);
            la.notifyDataSetChanged();
        };
        new DatePickerDialog(Manal_Entry.this, mDateListener,
                mDateTime.get(Calendar.YEAR),
                mDateTime.get(Calendar.MONTH),
                mDateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onTimeClick(final ListAdapter la) {       // set the time
        TimePickerDialog.OnTimeSetListener mTimeListener = (view, hour, minute) -> {
            mDateTime.set(Calendar.HOUR_OF_DAY, hour);
            mDateTime.set(Calendar.MINUTE, minute);
            mResults[2] = String.valueOf(hour) + ":" + String.valueOf(minute);
            la.notifyDataSetChanged();
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

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {

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
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manual_menu, menu);     // Inflate the menu

        if(parentName.equals("MAINHISTORY"))
        {
            MenuItem item = menu.findItem(R.id.action_save);
            item.setTitle("DELETE");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    // operations on the toolbar
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;


            case R.id.action_save:
                if(parentName.equals("MAINHISTORY"))
                {
                    // delete the entry
                    Intent intent = new Intent();
                    intent.putExtra("DELETEIDX", index);
                    setResult(RESULT_OK, intent);        // important!!!
                    finish();
                }

                else
                {
                    AsynWriteSQL writesqlhelper = new AsynWriteSQL(mhisView);
                    writesqlhelper.execute();

                    finish();
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("StaticFieldLeak")
    class AsynWriteSQL extends AsyncTask<Void, Void, Void> {

        MySQLiteHelper mysqlhelper;
        ListView mhistoryView;

        AsynWriteSQL(ListView mhisView)
        {
            mysqlhelper = new MySQLiteHelper(getApplication());
            mhistoryView = mhisView;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ExerciseEntry entry = new ExerciseEntry(0, "Manual", mResults[0], mResults[1]+" "+mResults[2],
                    mResults[3], mResults[4],null, null, mResults[5], null, mResults[6], mResults[7], null, null,
                    "false", "false","false");
            long rowId = mysqlhelper.insertEntry(entry);                    // insert an entry

            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            Log.d(TAG, "after insert");

            LoaderManager mLoader = main_history.mLoader;        // reinit a loader to update the ui
            if (mLoader != null)
            {
                mLoader.destroyLoader(1);
            }
            mLoader.initLoader(1, null, main_history.lc).forceLoad();


        }

    }







}
