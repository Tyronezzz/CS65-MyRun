/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */


package com.example.myrun5.fragment;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myrun5.ListAdapter;
import com.example.myrun5.MySQLiteHelper;
import com.example.myrun5.R;
import com.example.myrun5.activity.Manal_Entry;
import com.example.myrun5.activity.MapsActivity;
import com.example.myrun5.model.ExerciseEntry;
import com.example.myrun5.utils.Constant;
import com.example.myrun5.utils.EntryListLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class main_history extends Fragment implements LoaderManager.LoaderCallbacks<List<ExerciseEntry>> {

    private static final int ALL_EXERCISE_LOADER_ID = 1;
    private static final String TAG = "Mainhistory";
    private static final int REQUEST_CODE_DELETE = 0;
    private ListAdapter mAdapter;
    ArrayList<ExerciseEntry> exetry = new ArrayList<>();
    public static LoaderManager mLoader;
    public static LoaderManager.LoaderCallbacks lc;
    ListView mhisView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String EmailHash;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_history, container, false);     // Inflate the layout for this fragment
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        try {
            EmailHash = Constant.SHA1(user.getEmail());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        mDatabase.child("user_"+EmailHash).child("exercise_entries").addChildEventListener(new ChildEventListener() {

            // When the app starts this callback will add all items to the listview
            // or if a new item is added the "add new item" button or if an item
            // has been added to via the console

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                adapter.add((String) dataSnapshot.child("title").getValue());

//                ExerciseEntry newEnty = new ExerciseEntry((long)dataSnapshot.child("id").getValue(), (String)dataSnapshot.child("input_type").getValue(), (String)dataSnapshot.child("activity_type").getValue(), (String)dataSnapshot.child("date_time").getValue(),
//                        (String)dataSnapshot.child("duration").getValue(), (String)dataSnapshot.child("distance").getValue(), null, (String)dataSnapshot.child("avg_speed").getValue(), (String)dataSnapshot.child("calorie").getValue(),
//                        (String)dataSnapshot.child("climb").getValue(), (String)dataSnapshot.child("heart_rate").getValue(), (String)dataSnapshot.child("comment").getValue(), (String)dataSnapshot.child("privacy").getValue(),
//                        (String)dataSnapshot.child("gps").getValue(), (String)dataSnapshot.child("synced").getValue(),(String) dataSnapshot.child("deleted").getValue(), (String)dataSnapshot.child("boarded").getValue());
//
//                mAdapter.appendEntry(newEnty);
//                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "here  "+ (String)dataSnapshot.child("id").getValue());

                long itemId = (long)dataSnapshot.child("id").getValue();

                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getActivity());
                ArrayList<ExerciseEntry> oldList = mysqlhelper.fetchEntries();
                for(ExerciseEntry entry: oldList)
                {
                    if(itemId == entry.getId())
                    {
                        entry.setBoarded(true);
                        break;
                    }

                }

                mAdapter.addall(oldList);
                mAdapter.notifyDataSetChanged();


            }

            // The record has been removed from the db, now remove from the listview
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                adapter.remove((String) dataSnapshot.child("title").getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mhisView = getView().findViewById(R.id.manual_hislistview);

        exetry = new ArrayList<>();
        mAdapter = new ListAdapter(getActivity(), 3, exetry);
        mhisView.setAdapter(mAdapter);

        mhisView.setOnItemClickListener((parent, view1, position, id) -> {

            if(exetry.get(position).getInputType().contains("GPS"))      // GPS
            {
                Intent k = new Intent(getActivity(), MapsActivity.class);     // start activity
                k.putExtra("PARENTNAME", "MAINHISTORY");
                k.putExtra("EXENTRY", exetry.get(position));
                startActivityForResult(k, REQUEST_CODE_DELETE);
            }

            else if(exetry.get(position).getInputType().contains("Automatic"))     // Automatic
            {
                Intent k = new Intent(getActivity(), MapsActivity.class);
                k.putExtra("PARENTNAME", "MAINHISTORY");
                k.putExtra("EXENTRY", exetry.get(position));
                startActivityForResult(k, REQUEST_CODE_DELETE);
            }

            else         // Manual
            {
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
            Log.d(TAG, "do nothing");
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


    private void runThread(final long index) {      // delete the acti

        Thread t1 = new Thread(() -> {
            MySQLiteHelper mysqlhelper = new MySQLiteHelper(getContext());
            mysqlhelper.removeEntry(index);
        });
        t1.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.d(TAG, "click update");


                // syn between local and cloud
                // synced just false ones and flag all local synced to be true


                Log.d(TAG, user.getEmail() + " "+ user.getUid());

                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getActivity());
                ArrayList<ExerciseEntry> entries = mysqlhelper.fetchSynFalseEntries();


                for(int i=0;i<entries.size();i++)
                {

                    ExerciseEntry entry = entries.get(i);
                    mDatabase.child("user_" + EmailHash).child("exercise_entries").push().setValue(entry)
                            .addOnCompleteListener(getActivity(), task -> {
                                if(task.isSuccessful()){
                                    // Insert is done!
                                    Log.d(TAG, "Insert suc");
                                }else{
                                    // Failed
                                    if(task.getException() != null)
                                        Log.d(TAG, task.getException().getMessage());
                                }
                            });
                }

                mysqlhelper.updateSyn();

                return true;


        }
        return false;
    }
}
