/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */

package com.example.myrun4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.myrun4.R;
import com.example.myrun4.activity.Manal_Entry;
import com.example.myrun4.activity.MapsActivity;

public class main_start extends Fragment {

    private static final String ACT_IDX = "act_idx";
    private static final String ISFIRST = "isfirst";
    private static final String TAG = "Main_Start.lifecyc";

    private Spinner mspinner_input;
    private Spinner mspinner_act;
    private int act_idx = 0;
    private boolean isFirst = true;

    public main_start() {
        // Required empty public constructor
    }


//    public static main_start newInstance(String param1, String param2) {
//        main_start fragment = new main_start();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isFirst)
            isFirst = false;

        if(savedInstanceState != null)
        {
            act_idx = savedInstanceState.getInt(ACT_IDX);
            isFirst = savedInstanceState.getBoolean(ISFIRST, true);
            Log.d(TAG, "notnull ");
        }
        Log.d(TAG, "now "+act_idx);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mspinner_input = getView().findViewById(R.id.input_type);
        mspinner_act= getView().findViewById(R.id.activity_type);
        FloatingActionButton mfab = getView().findViewById(R.id.fab);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.input_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      // specify the layout for spinner
        mspinner_input.setAdapter(adapter);     // apply the adapter to the spinner

        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.act_type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      // specify the layout for spinner
        mspinner_act.setAdapter(adapter2);     // apply the adapter to the spinner


        mspinner_act.setSelection(act_idx);

        mspinner_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String tmp = mspinner_input.getSelectedItem().toString();

                if(tmp.equals("Automatic"))                // disable the second spinner when the first select Automatic
                {
                    mspinner_act.setEnabled(false);
                    mspinner_act.setClickable(false);
                }

                else                                      // enable
                {
                    mspinner_act.setEnabled(true);
                    mspinner_act.setClickable(true);
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });


        mspinner_act.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {         // click the activity type spinner
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!isFirst)
                {
                    act_idx = position;
                    Log.d(TAG, "idx is"+act_idx);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });


        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = mspinner_input.getSelectedItem().toString();
                Intent intent;
                if(text.equals("Manual"))                            // go to Manual Entry Activity
                {
                    intent = new Intent(getContext(), Manal_Entry.class);
                    String text2 = mspinner_act.getSelectedItem().toString();
                    String text3 = mspinner_input.getSelectedItem().toString();
                    intent.putExtra("ACT", text2);
                    intent.putExtra("TYPE", text3);
                    intent.putExtra("PARENTNAME", "MAINSTART");
                    startActivity(intent);
                }

                else                                     // go to Map Activity
                {
                    String text2 = mspinner_act.getSelectedItem().toString();
                    intent = new Intent(getContext(), MapsActivity.class);
                    intent.putExtra("act_type", text2);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        act_idx = mspinner_act.getSelectedItemPosition();
        outState.putInt(ACT_IDX, act_idx);
        outState.putBoolean(ISFIRST, isFirst);
//        Log.d(TAG, "called"+act_idx);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "req" + requestCode);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_start, container, false);      // Inflate the layout for this fragment
    }

//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
}
