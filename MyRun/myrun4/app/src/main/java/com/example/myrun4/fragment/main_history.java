/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */


package com.example.myrun4.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.example.myrun4.utils.EntryListLoader;
import com.example.myrun4.ListAdapter;
import com.example.myrun4.MySQLiteHelper;
import com.example.myrun4.R;
import com.example.myrun4.activity.Manal_Entry;
import com.example.myrun4.model.ExerciseEntry;
import java.util.ArrayList;
import java.util.List;


public class main_history extends Fragment implements LoaderManager.LoaderCallbacks<List<ExerciseEntry>> {

    private static final int ALL_EXERCISE_LOADER_ID = 1;
    private static final String TAG = "Mainhistory";
    private static final int REQUEST_CODE_DELETE = 0;
//    private MySQLiteHelper dataSource;
    private ListAdapter mAdapter;
    ArrayList<ExerciseEntry> exetry = new ArrayList<>();
    public static LoaderManager mLoader;
    public static LoaderManager.LoaderCallbacks lc;
    public  static int adapterChoice = 0;
    ListView mhisView;

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
        mhisView = getView().findViewById(R.id.manual_hislistview);

        exetry = new ArrayList<>();
        mAdapter = new ListAdapter(getActivity(), 3, exetry);
        mhisView.setAdapter(mAdapter);

        mhisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // start activity
                Intent k = new Intent(getActivity(), Manal_Entry.class);
                k.putExtra("PARENTNAME", "MAINHISTORY");
                k.putExtra("EXENTRY", exetry.get(position));
                k.putExtra("INDEX", exetry.get(position).getId());
                startActivityForResult(k, REQUEST_CODE_DELETE);
            }
        });

        // start loader in the background thread.
        mLoader = getLoaderManager();       //  getSupportLoaderManager
        mLoader.initLoader(ALL_EXERCISE_LOADER_ID, null, this).forceLoad();
        lc = this;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
        {
            Log.d(TAG, "GG");
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_DELETE:    //
                long idx = data.getLongExtra("DELETEIDX", 0);
                runThread(idx);

                if (mLoader != null)
                {
                    mLoader.destroyLoader(1);
                }
                mLoader.initLoader(ALL_EXERCISE_LOADER_ID, null, this).forceLoad();
                lc = this;
                break;

        }
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

//            if(exerciseEntries.size() > 0)

            if(adapterChoice == 1)
            {
                mAdapter.clear();
                mAdapter = new ListAdapter(getActivity(), 10, exetry);

                exetry = new ArrayList<>(exerciseEntries);
                mhisView.setAdapter(mAdapter);
                mAdapter.addall(exerciseEntries);
                mAdapter.notifyDataSetChanged();           // force notification -- tell the adapter to displa
            }


            else
            {
                exetry = new ArrayList<>(exerciseEntries);
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


    private void runThread(final long index) {

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getContext());
                mysqlhelper.removeEntry(index);
            }
        });
        t1.start();

    }
}
