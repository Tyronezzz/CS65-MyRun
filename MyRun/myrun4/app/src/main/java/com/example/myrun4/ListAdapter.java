/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun4;

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

import com.example.myrun4.model.ExerciseEntry;

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
    private int request_code;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<ExerciseEntry> exetry;
    private int REQUEST_SETTING = 0;


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

    public ListAdapter(Activity context, int rc, List<ExerciseEntry> exetry) {
        super(context, R.layout.listview_history, exetry);
        this.context = context;
        this.exetry = new ArrayList<>(exetry);
        this.request_code = rc;
        Log.d(TAG, "size " + exetry.size());
    }


    public void addall(List<ExerciseEntry> items) {
        exetry = new ArrayList<>(items);
    }



    @Override
    public int getCount() {

        int REQUEST_HISTORY = 3;
        if(request_code == REQUEST_HISTORY)
        {

            return exetry.size();
        }


        else if(request_code == REQUEST_SETTING)
            return setting_option.length;

        else           //if(context.getLocalClassName().contains("Manal_Entry"))
            return Options.length;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position,View view, @NonNull ViewGroup parent) {

        Log.d(TAG, "class name" + context.getLocalClassName());

        if(context.getLocalClassName().contains("Manal_Entry") && request_code != 3)     // set the listview for Manual Entry Activity
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
            int REQUEST_SIGNOUT = 2;
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

//                Log.d(TAG, "update history");
                LayoutInflater inflater=context.getLayoutInflater();
                @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_history, null,true);

                TextView mTitleView = rowView.findViewById(R.id.manual_title);
                TextView mDateView =  rowView.findViewById(R.id.manual_datetime);
                TextView mDuration =  rowView.findViewById(R.id.manual_des);

                sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
                int km_mile_idx = sharedPreferences.getInt("key_unit_pre", 0);
                
                String tmpdis = exetry.get(position).getDistance();
                if(km_mile_idx == 1 && tmpdis.contains("kms"))
                {
                    String[] substr = exetry.get(position).getDistance().split("\\s+");
                    tmpdis = String.valueOf(Double.parseDouble(substr[0])*1.609) + " miles";
                }

                else if(km_mile_idx == 0 && tmpdis.contains("mile"))
                {
                    String[] substr = exetry.get(position).getDistance().split("\\s+");
                    tmpdis = String.valueOf(Double.parseDouble(substr[0])*0.621) + " kms";
                }
                
                mTitleView.setText( "Manual: " + exetry.get(position).getActType());     //this code sets the values of the objects to values from the arrays
                mDateView.setText(exetry.get(position).getDateTime());
                mDuration.setText(tmpdis + ", " + exetry.get(position).getDuration());

                return rowView;
            }
        }
    }
}
