package com.example.myrun2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of countries
    private final String[] nameArray;

    //to store the list of countries
    private final String[] infoArray;


    public ListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);
        this.context = context;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = rowView.findViewById(R.id.manual_option);
        TextView infoTextField =  rowView.findViewById(R.id.manual_result);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);

        return rowView;

    };


}
