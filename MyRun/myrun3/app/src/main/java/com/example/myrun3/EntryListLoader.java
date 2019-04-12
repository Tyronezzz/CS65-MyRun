package com.example.myrun3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.myrun3.model.ExerciseEntry;

import java.util.List;

public class EntryListLoader extends AsyncTaskLoader<List<ExerciseEntry>> {

    private MySQLiteHelper dataSource;
    EntryListLoader(@NonNull Context context) {
        super(context);
        this.dataSource = new MySQLiteHelper(context);         // create the database
    }


    @Nullable
    @Override
    public List<ExerciseEntry> loadInBackground() {

        Log.d("loader ", "loader here");
        return dataSource.fetchEntries();
    }

}
