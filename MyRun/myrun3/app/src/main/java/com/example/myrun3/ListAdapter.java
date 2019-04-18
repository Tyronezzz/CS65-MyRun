/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myrun3.model.ExerciseEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


// use this class for two listview, one with switch and one without switch
public class ListAdapter extends ArrayAdapter {

    private static final String TAG = "ListAdapter";
    private Activity context;
    private String[] Options;
    private String[] Results;
    private String[] setting_option;
    private String[] setting_descrp;
    private String[] act_datetime;
    private int request_code;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<ExerciseEntry> exetry;
    int REQUEST_SETTING = 0;
    private int REQUEST_SIGNOUT = 2;
    private int REQUEST_HISTORY = 3;


    public ListAdapter(Activity context, String[] setting_opt, String[] setting_des, SharedPreferences sharedPreferences, int rc){

        super(context, R.layout.listview_row, setting_des);

        this.context = context;
        this.setting_option = setting_opt;
        this.setting_descrp = setting_des;
        this.sharedPreferences = sharedPreferences;
        this.request_code = rc;
    }

    public ListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam, int rc){

        super(context, R.layout.listview_row_clip_btn, nameArrayParam);

        this.context = context;
        this.Options = nameArrayParam;
        this.Results = infoArrayParam;
        this.request_code = rc;
    }

//    public ListAdapter(Activity context, String[] act_title, String[] act_des, String[] act_datetime, int rc) {
//        super(context, R.layout.listview_history, act_title);
//
//        this.context = context;
//        this.setting_option = act_title;
//        this.setting_descrp = act_des;
//        this.act_datetime = act_datetime;
//        this.request_code = rc;
//
//    }



    public ListAdapter(Activity context, int rc, List<ExerciseEntry> exetry) {   //
        super(context, R.layout.listview_history, exetry);
        this.context = context;
        this.exetry = exetry;
        this.request_code = rc;
        Log.d(TAG, "ini  ");


    }


    public void addall(List<ExerciseEntry> items) {

        exetry = new ArrayList<>(items);

//        for(int i=0;i<items.size();i++)
//l            exetry.set(i, items.get(i));
//        this.exetry = items;
        Log.d(TAG, "Size of array ");
    }



    @Override
    public int getCount() {



        if(request_code == REQUEST_HISTORY)
            return exetry.size();



        else if(request_code == REQUEST_SETTING)
            return setting_option.length;

        else //if(context.getLocalClassName().contains("Manal_Entry"))
            return Options.length;


    }



    @NonNull
    @Override
    public View getView(int position,View view, @NonNull ViewGroup parent) {


        Log.d(TAG, "get here");
//        Log.d(TAG, "class name" + context.getLocalClassName());

        if(context.getLocalClassName().contains("Manal_Entry"))     // set the listview for Manual Entry Activity
        {
            LayoutInflater inflater=context.getLayoutInflater();
            @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_row, null,true);

            //this code gets references to objects in the listview_row.xml file
            TextView mOPtionsView = rowView.findViewById(R.id.manual_option);
            TextView mResultsView =  rowView.findViewById(R.id.manual_result);

            //this code sets the values of the objects to values from the arrays
            mOPtionsView.setText(Options[position]);
            mResultsView.setText(Results[position]);

            return rowView;
        }

        else              // set up the listview for Setting Activity
        {



            int REQUEST_PREFERENCE = 1;
            if(request_code == REQUEST_PREFERENCE)              // listview without switch
            {
                LayoutInflater inflater=context.getLayoutInflater();
                @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_row, null,true);

                TextView mOPtionsView = rowView.findViewById(R.id.manual_option);
                TextView mResultsView =  rowView.findViewById(R.id.manual_result);

                mOPtionsView.setTextSize(17);
                mResultsView.setTextSize(13);

                mOPtionsView.setText(Options[position]);
                mResultsView.setText(Results[position]);

                return rowView;
            }

            else if(request_code == REQUEST_SETTING)          // listview with switch
            {

                Log.d(TAG, "update");
                LayoutInflater inflater=context.getLayoutInflater();
                @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_row_clip_btn, null,true);

                TextView mOPtionsView = rowView.findViewById(R.id.setting_option);
                TextView mResultsView =  rowView.findViewById(R.id.setting_result);
                final Switch myswitch = rowView.findViewById(R.id.myswitch);


                sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
                boolean ischecked = sharedPreferences.getBoolean("key_privacy_set", false);
                myswitch.setChecked(ischecked);

                myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
                        editor = sharedPreferences.edit();
                        editor.putBoolean("key_privacy_set", myswitch.isChecked());
                        editor.apply();

                        Log.d(TAG, "save " + myswitch.isChecked());
                    }
                });

                mOPtionsView.setTextSize(17);
                mResultsView.setTextSize(13);

                mOPtionsView.setText(setting_option[position]);
                mResultsView.setText(setting_descrp[position]);

                return rowView;
            }

            else if(request_code == REQUEST_SIGNOUT)                                              // sign out
            {
                LayoutInflater inflater=context.getLayoutInflater();
                @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_row, null,true);

                TextView mOPtionsView = rowView.findViewById(R.id.manual_option);
                TextView mResultsView =  rowView.findViewById(R.id.manual_result);

                mOPtionsView.setTextSize(17);
                mResultsView.setTextSize(0);

                mOPtionsView.setText(Options[position]);
                mResultsView.setText(Results[position]);

                return rowView;
            }

            else
            {
                Log.d(TAG, "set exes");
                LayoutInflater inflater=context.getLayoutInflater();
                @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_history, null,true);


                TextView mTitleView = rowView.findViewById(R.id.manual_title);
                TextView mDateView =  rowView.findViewById(R.id.manual_datetime);
                TextView mDuration =  rowView.findViewById(R.id.manual_des);

                //this code sets the values of the objects to values from the arrays
                mTitleView.setText(exetry.get(position).getActType());
                mDateView.setText(exetry.get(position).getDateTime());
                mDuration.setText(exetry.get(position).getDuration());

                Log.d(TAG, "arrr ziess " + exetry.size());
                return rowView;
            }
        }
    }
}
