/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */


package com.example.myrun3.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myrun3.EntryListLoader;
import com.example.myrun3.ListAdapter;
import com.example.myrun3.MySQLiteHelper;
import com.example.myrun3.R;
import com.example.myrun3.model.ExerciseEntry;

import java.util.ArrayList;
import java.util.List;


public class main_history extends Fragment implements LoaderManager.LoaderCallbacks<List<ExerciseEntry>> {

    private static final int ALL_EXERCISE_LOADER_ID = 1;
    private static final String TAG = "Mainhistory";
    private MySQLiteHelper dataSource;
    private ListAdapter mAdapter;
    ArrayList<ExerciseEntry> exetry = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_history, container, false);     // Inflate the layout for this fragment
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dataSource = new MySQLiteHelper(getContext());
        ListView mhisView = getView().findViewById(R.id.manual_hislistview);

//        ExerciseEntry entry_one = new ExerciseEntry(null, "1", "1", "1",
//                "1",null, null, "1", null,
//                "1", "1", null, null);
        exetry = new ArrayList<ExerciseEntry>();
//        exetry.add(entry_one);

        mAdapter = new ListAdapter(getActivity(), 3, exetry);  // new String[]{}
        mhisView.setAdapter(mAdapter);


        mhisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));

                // start activity
//                Intent k = new Intent(LoginActivity.this, RegisterActivity.class);
//                    k.putExtra("PARENTNAME", "LOGIN");
//                    startActivity(k);


            }
        });


                // start loader in the background thread.
        LoaderManager mLoader = getLoaderManager();       //  getSupportLoaderManager
        mLoader.initLoader(ALL_EXERCISE_LOADER_ID, null, this).forceLoad();

    }



    @NonNull
    @Override
    public Loader<List<ExerciseEntry>> onCreateLoader(int i, @Nullable Bundle bundle) {


        if(i == ALL_EXERCISE_LOADER_ID){
            Log.d(TAG, "start here");
            return new EntryListLoader(getContext());
        }
        return null;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<ExerciseEntry>> loader, List<ExerciseEntry> exerciseEntries) {

        Log.d(TAG, "onLoadFinsh " + loader.getId());

        if(loader.getId() == ALL_EXERCISE_LOADER_ID){
            // returns the List<Comment> from queried from the db
            // Use the UI with the adapter to show the elements in a ListView
            if(exerciseEntries.size() > 0){

                Log.d(TAG, "size of input " + exerciseEntries.size());
                mAdapter.addall(exerciseEntries);
                mAdapter.notifyDataSetChanged();           // force notification -- tell the adapter to display
            }

        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<ExerciseEntry>> loader) {

        Log.d(TAG, "restart " + loader.getId());
        if(loader.getId() == ALL_EXERCISE_LOADER_ID){
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }


    }
}
