/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-07
 */


package com.example.myrun5.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_history, container, false);     // Inflate the layout for this fragment
    }


    @SuppressLint("CommitPrefEdits")
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


        sharedPreferences = getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("usersEmail", "").equals(""))
        {
            Log.d(TAG, "new user");
            editor.putString("usersEmail", user.getEmail());
            editor.apply();



            DatabaseReference rootpath = mDatabase.child("user_"+EmailHash).child("exercise_entries");

//            for()
//            {
//                rootpath.child()
//
//            }
//
//            long itemId = (long)dataSnapshot.child("id").getValue();
//                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getActivity());
//                ExerciseEntry tmpEntry = new ExerciseEntry((long)dataSnapshot.child("id").getValue(), (String)dataSnapshot.child("inputType").getValue(), (String)dataSnapshot.child("actType").getValue(), (String)dataSnapshot.child("dateTime").getValue(),
//                        (String)dataSnapshot.child("duration").getValue(), (String)dataSnapshot.child("distance").getValue(), null, (String)dataSnapshot.child("avgSpeed").getValue(), (String)dataSnapshot.child("calorie").getValue(),
//                        (String)dataSnapshot.child("climb").getValue(), (String)dataSnapshot.child("heartrate").getValue(), (String)dataSnapshot.child("comment").getValue(), (String)dataSnapshot.child("privacy").getValue(),
//                        (String)dataSnapshot.child("gps").getValue(), (String)dataSnapshot.child("synced").getValue(),(String) dataSnapshot.child("deleted").getValue(), (String)dataSnapshot.child("boarded").getValue());
//
//


            // load from FB to sql
        }






        mDatabase.child("user_"+EmailHash).child("exercise_entries").addChildEventListener(new ChildEventListener() {
            // When the app starts this callback will add all items to the listview
            // or if a new item is added the "add new item" button or if an item
            // has been added to via the console

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//                long itemId = (long)dataSnapshot.child("id").getValue();
//                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getActivity());
//                ExerciseEntry tmpEntry = new ExerciseEntry((long)dataSnapshot.child("id").getValue(), (String)dataSnapshot.child("inputType").getValue(), (String)dataSnapshot.child("actType").getValue(), (String)dataSnapshot.child("dateTime").getValue(),
//                        (String)dataSnapshot.child("duration").getValue(), (String)dataSnapshot.child("distance").getValue(), null, (String)dataSnapshot.child("avgSpeed").getValue(), (String)dataSnapshot.child("calorie").getValue(),
//                        (String)dataSnapshot.child("climb").getValue(), (String)dataSnapshot.child("heartrate").getValue(), (String)dataSnapshot.child("comment").getValue(), (String)dataSnapshot.child("privacy").getValue(),
//                        (String)dataSnapshot.child("gps").getValue(), (String)dataSnapshot.child("synced").getValue(),(String) dataSnapshot.child("deleted").getValue(), (String)dataSnapshot.child("boarded").getValue());
//
//                mysqlhelper.insertEntry(tmpEntry);
//
//                mAdapter.addall(mysqlhelper.fetchEntries());
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                long itemId = (long)dataSnapshot.child("id").getValue();

                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getActivity());
                ArrayList<ExerciseEntry> oldList = mysqlhelper.fetchEntries();
                for(int i=0;i<oldList.size();i++)
                {
                    if(itemId == oldList.get(i).getId())
                    {
                        //ExerciseEntry tmpEntry = dataSnapshot.getValue(ExerciseEntry.class);
                        ExerciseEntry tmpEntry = new ExerciseEntry((long)dataSnapshot.child("id").getValue(), (String)dataSnapshot.child("inputType").getValue(), (String)dataSnapshot.child("actType").getValue(), (String)dataSnapshot.child("dateTime").getValue(),
                        (String)dataSnapshot.child("duration").getValue(), (String)dataSnapshot.child("distance").getValue(), null, (String)dataSnapshot.child("avgSpeed").getValue(), (String)dataSnapshot.child("calorie").getValue(),
                        (String)dataSnapshot.child("climb").getValue(), (String)dataSnapshot.child("heartrate").getValue(), (String)dataSnapshot.child("comment").getValue(), (String)dataSnapshot.child("privacy").getValue(),
                        (String)dataSnapshot.child("gps").getValue(), (String)dataSnapshot.child("synced").getValue(),(String) dataSnapshot.child("deleted").getValue(), (String)dataSnapshot.child("boarded").getValue());

                        oldList.set(i, tmpEntry);
                        mAdapter.addall(oldList);
                        mAdapter.notifyDataSetChanged();

                        // update local sql
                        mysqlhelper.updateFBtoSql(tmpEntry, itemId);
                        exetry = mysqlhelper.fetchEntries();
                        break;
                    }

                }
            }

            // The record has been removed from the db, now remove from the listview
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                long itemId = (long)dataSnapshot.child("id").getValue();
                MySQLiteHelper mysqlhelper = new MySQLiteHelper(getActivity());
                mysqlhelper.removeEntry(itemId);

                mAdapter.addall(mysqlhelper.fetchEntries());
                mAdapter.notifyDataSetChanged();
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

            String arrlist = sharedPreferences.getString("deleteIndexList", "");

            arrlist = arrlist + ";" + String.valueOf(index);
            editor.putString("deleteIndexList", arrlist);
            editor.apply();

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
                    mDatabase.child("user_" + EmailHash).child("exercise_entries").child(String.valueOf(entry.getId())).setValue(entry)
                            .addOnCompleteListener(getActivity(), task -> {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "Insert suc");      // Insert is done!
                                }else{
                                    // Failed
                                    if(task.getException() != null)
                                        Log.d(TAG, task.getException().getMessage());
                                }
                            });
                }

                mysqlhelper.updateSyn();

                // update the delete ones
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
                String arrlist = sharedPreferences.getString("deleteIndexList", "");

                if(!arrlist.equals(""))
                {
                    String[] arrOfStr = arrlist.split(";");

                    for(String str: arrOfStr)            // syn the FB
                    {
                        if(str.equals(""))
                            continue;

                        mDatabase.child("user_" + EmailHash).child("exercise_entries").child(str).removeValue();
                    }


                    // delete the local var
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    arrlist =  "";
                    editor.putString("deleteIndexList", arrlist);
                    editor.apply();
                }


                return true;


        }
        return false;
    }
}
