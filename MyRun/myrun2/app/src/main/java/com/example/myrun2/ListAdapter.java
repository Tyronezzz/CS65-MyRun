package com.example.myrun2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ListAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] Options;
    private final String[] Results;


    ListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam){

        super(context, R.layout.listview_row , nameArrayParam);
        this.context = context;
        this.Options = nameArrayParam;
        this.Results = infoArrayParam;

    }

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

        return rowView;

    }
}
