package com.example.myrun5.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myrun5.ListAdapter;
import com.example.myrun5.MySQLiteHelper;
import com.example.myrun5.R;
import com.example.myrun5.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class main_board extends Fragment {

    private static final String TAG = "mainboard";
    private ListAdapter mAdapter;
    private ListView mhisView;

    private MySQLiteHelper mysqlhelper;


    public class boardEntry{

        private String activity_type;
        private String input_type;
        private String date_time;
        private String duration;
        private String distance;
        private String email;

        public boardEntry(String input_type, String activity_type, String date_time, String duration, String email, String distance)
        {
            this.input_type = input_type;
            this.activity_type = activity_type;
            this.date_time = date_time;
            this.duration = duration;
            this.email = email;
            this.distance = distance;
        }

        public String getInputType() {
            return input_type;
        }

        public String getActType() {
            return activity_type;
        }

        public String getDateTime() {
            return date_time;
        }

        public String getDuration() {
            return duration;
        }

        public String getDistance() {
            return distance;
        }

        public String getEmail() {
            return email;
        }

    }

    public main_board() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_board, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ArrayList<boardEntry> entryList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, Constant.BaseUrl + "/get_exercises", null, new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response){             // Parse the JSON array and each JSON objects inside it
                        for(int i=0;i<response.length();i++) {
                            JSONObject obj = null;
                            try {
                                obj = response.getJSONObject(i);
                                String distance = obj.getString("distance");
                                String input_type = obj.getString("input_type");
                                String activity_type = obj.getString("activity_type");
                                String activity_date = obj.getString("activity_date");
                                String duration = obj.getString("duration");
                                String email = obj.getString("email");

                                boardEntry bEntry = new boardEntry(input_type, activity_type, activity_date, duration, email, distance);
                                entryList.add(bEntry);
//                                mysqlhelper = new MySQLiteHelper(getContext());
//                                ArrayList<ExerciseEntry> exetry = mysqlhelper.fetchEntries();



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mhisView = getView().findViewById(R.id.manual_hislistview);
                        mAdapter = new ListAdapter(getActivity(), entryList, 100);
                        mhisView.setAdapter(mAdapter);
                        Log.d(TAG, response.toString());
                    }

                }, error -> {
                    // TODO: Handle error
                    if(error.getMessage() != null)
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonArrayRequest);

    }






}


