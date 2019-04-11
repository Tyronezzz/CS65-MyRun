package com.example.myrun3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.RowId;
import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_ENTRIES = "manual_entry_table";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_INPUT_TYPE = "input_type";
    public static final String KEY_ACTIVITY_TYPE = "activity_type";
    public static final String KEY_DATE_TIME = "date_time";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_AVG_PACE = "avg_page";
    public static final String KEY_AVG_SPEED = "avg_speed";
    public static final String KEY_CALORIES = "calorie";
    public static final String KEY_CLIMB = "climb";
    public static final String KEY_HEARTRATE = "heart_rate";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_PRIVACY = "privacy";
    public static final String KEY_GPS_DATA = "gps";
    private static final String DBNAME = "MyRunDB";
    private static final int DBVERSION = 3;
    private SQLiteDatabase db;


    public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_ENTRIES
            + " ("
            + KEY_ROWID
            + "integer primary key autoincrement, "
            + KEY_INPUT_TYPE
            + " TEXT, "
            + KEY_ACTIVITY_TYPE
            + " TEXT, "
            + KEY_DATE_TIME
            + " TEXT, "
            + KEY_DURATION
            + " TEXT, "
            + KEY_DISTANCE
            + " TEXT, "
            + KEY_AVG_PACE
            + " TEXT, "
            + KEY_AVG_SPEED
            + " TEXT, "
            + KEY_CALORIES
            + " TEXT, "
            + KEY_CLIMB
            + " TEXT, "
            + KEY_HEARTRATE
            + " TEXT, "
            + KEY_COMMENT
            + " TEXT, "
            + KEY_PRIVACY
            + " TEXT, "
            + KEY_GPS_DATA
            + " BLOB "
            + ");";
    private static final String TAG = "mysqlitehelper";


    public MySQLiteHelper(Context context)
    {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "upgrading");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ENTRIES);
        onCreate(db);
    }



    // Insert a item given each column value
    public long insertEntry(ExerciseEntry entry) {

        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_INPUT_TYPE, entry.getInputType());










        long newRowId = db.insert(TABLE_NAME_ENTRIES, null, values);
        if(newRowId == -1)
            Log.d(TAG, "Error in inserting!");



        cursor.close();
        db.close();
        this.close();

    }



    public void removeEntry(long rowIndex) {            // Remove an entry by giving its index
        db = getWritableDatabase();
        String selection = KEY_ROWID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowIndex) };

        int deletedRows = db.delete(TABLE_NAME_ENTRIES, selection, selectionArgs);
        Log.d(TAG, String.valueOf(deletedRows) + " row(s) have been deleted");

        db.close();
        this.close();
    }

    // Query a specific entry by its index.
    public ExerciseEntry fetchEntryByIndex(long rowId) {

        db = getReadableDatabase();
        String sortOrder = FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";
        String selection = KEY_ROWID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };

        Cursor cursor = db.query(
                TABLE_NAME_ENTRIES,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );




    }

    // Query the entire table, return all rows
    public ArrayList<ExerciseEntry> fetchEntries() {

        ArrayList<ExerciseEntry> entries = new ArrayList<>();
        db = getReadableDatabase();
        String sortOrder = KEY_DATE_TIME + " ASC";

        Cursor cursor = db.query(
                TABLE_NAME_ENTRIES,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ExerciseEntry entry = cursorToComment(cursor);
//            Log.d(TAG, "get entry = " + cursorToComment(cursor).toString());
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }


}
