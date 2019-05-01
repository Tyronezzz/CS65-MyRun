/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-15
 */


package com.example.myrun5.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.myrun5.MySQLiteHelper;
import com.example.myrun5.model.ExerciseEntry;

import java.util.ArrayList;
import java.util.List;

public class EntryListLoader extends AsyncTaskLoader<List<ExerciseEntry>> {

    private MySQLiteHelper dataSource;

    public EntryListLoader(@NonNull Context context) {
        super(context);
        this.dataSource = new MySQLiteHelper(context);         // create the database
    }


    @Nullable
    @Override
    public ArrayList<ExerciseEntry> loadInBackground() {

        Log.d("loader ", "loader here");
        return dataSource.fetchEntries();       // fetch all entries

    }

}
