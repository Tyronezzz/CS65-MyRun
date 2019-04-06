package com.example.myrun2;

import android.content.Intent;
import android.net.Uri;
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

public class main_start extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ACT_IDX = "act_idx";
    private static final String ISFIRST = "isfirst";
    private static final String TAG = "Main_Start.lifecyc";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Spinner mspinner_input;
    private Spinner mspinner_act;
    private FloatingActionButton mfab;
    private int act_idx = 0;
    private boolean isFirst = true;

    public main_start() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main_start.
     */
    // TODO: Rename and change types and number of parameters
    public static main_start newInstance(String param1, String param2) {
        main_start fragment = new main_start();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if(isFirst)
            isFirst = false;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mspinner_input = getView().findViewById(R.id.input_type);
        mspinner_act= getView().findViewById(R.id.activity_type);

        mfab = getView().findViewById(R.id.fab);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.input_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      // Specify the layout to use when the list of choices appears
        mspinner_input.setAdapter(adapter);     // Apply the adapter to the spinner


        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.act_type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      // Specify the layout to use when the list of choices appears
        mspinner_act.setAdapter(adapter2);     // Apply the adapter to the spinner



        if(savedInstanceState != null)
        {
            act_idx = savedInstanceState.getInt(ACT_IDX, 0);
            isFirst = savedInstanceState.getBoolean(ISFIRST, true);
        }


        mspinner_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String tmp = mspinner_input.getSelectedItem().toString();

                mspinner_act.setSelection(act_idx);
                Log.d(TAG, "now" + act_idx);


                if(tmp.equals("Automatic"))
                {
                    mspinner_act.setEnabled(false);
                    mspinner_act.setClickable(false);
                    mspinner_act.setAdapter(adapter2);
                }

                else
                {
                    mspinner_act.setEnabled(true);
                    mspinner_act.setClickable(true);
                    mspinner_act.setAdapter(adapter2);

                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });


        mspinner_act.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(!isFirst)
                {
                    act_idx = position;
                    Log.d(TAG, "idx is"+act_idx);
                }


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });




        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = mspinner_input.getSelectedItem().toString();
                Intent intent;
                if(text.equals("Manual"))
                {
                    intent = new Intent(getContext(), Manal_Entry.class);
                    startActivity(intent);
                }

                else
                {
                    intent = new Intent(getContext(), MapActivity.class);
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_start, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
