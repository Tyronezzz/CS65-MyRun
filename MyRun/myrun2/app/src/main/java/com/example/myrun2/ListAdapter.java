/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


// use this class for two listview, one with switch and one without switch
public class ListAdapter extends ArrayAdapter {

    private static final String TAG = "ListAdapter";
    private Activity context;
    private String[] Options;
    private String[] Results;
    private String[] setting_option;
    private String[] setting_descrp;
    private boolean isSelect;
    private int request_code;
    //    private int REQUEST_SIGNOUT = 2;


    ListAdapter(Activity context, String[] setting_opt, String[] setting_des, boolean isSelect, int rc){
        super(context, R.layout.listview_row, setting_des);
        this.context = context;
        this.setting_option = setting_opt;
        this.setting_descrp = setting_des;
        this.isSelect = isSelect;
        this.request_code = rc;
        Log.d(TAG, "check");
    }

    ListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam, int rc){
        super(context, R.layout.listview_row_clip_btn , nameArrayParam);
        this.context = context;
        this.Options = nameArrayParam;
        this.Results = infoArrayParam;
        this.request_code = rc;
    }


//    public void setDate(String mdate)
//    {
//        this.Results[1] = mdate;
//        LayoutInflater inflater=context.getLayoutInflater();
//        View rowView = inflater.inflate(R.layout.listview_row, null,true);
//
//        TextView mResultsView =  rowView.findViewById(R.id.manual_result);
//        mResultsView.setText(Results[1]);
//        Log.d(TAG, "set??");
//
//    }

//    @Override
//    public boolean isEnabled(int position) {
//        if(position == 0)
//            return false;
//
//        return true;
//    }


    @NonNull
    @Override
    public View getView(int position,View view, @NonNull ViewGroup parent) {


        Log.d(TAG, String.valueOf(position));
        if(context.getLocalClassName().equals("com.example.myrun2.Manal_Entry"))     // set the listview for Manual Entry Activity
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
            int REQUEST_SETTING = 0;
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
                LayoutInflater inflater=context.getLayoutInflater();
                @SuppressLint("InflateParams") View rowView = inflater.inflate(R.layout.listview_row_clip_btn, null,true);

                TextView mOPtionsView = rowView.findViewById(R.id.setting_option);
                TextView mResultsView =  rowView.findViewById(R.id.setting_result);

                mOPtionsView.setTextSize(17);
                mResultsView.setTextSize(13);

                mOPtionsView.setText(setting_option[position]);
                mResultsView.setText(setting_descrp[position]);

                return rowView;
            }

            else                                              // sign out
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
        }
    }
}
