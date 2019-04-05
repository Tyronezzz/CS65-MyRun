package com.example.myrun2;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ListAdapter extends ArrayAdapter {

    private static final String TAG = "ListAdapter";
    private final Activity context;
    private final String[] Options;
    private final String[] Results;


    ListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam){

        super(context, R.layout.listview_row , nameArrayParam);
        this.context = context;
        this.Options = nameArrayParam;
        this.Results = infoArrayParam;

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
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView mOPtionsView = rowView.findViewById(R.id.manual_option);
        TextView mResultsView =  rowView.findViewById(R.id.manual_result);

        //this code sets the values of the objects to values from the arrays
        mOPtionsView.setText(Options[position]);
        mResultsView.setText(Results[position]);

        Log.d(TAG, "here??");
        return rowView;

    }
}
