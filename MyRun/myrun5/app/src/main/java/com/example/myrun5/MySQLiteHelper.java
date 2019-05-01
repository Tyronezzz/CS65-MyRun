/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-15
 */

package com.example.myrun5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myrun5.model.ExerciseEntry;

import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME_ENTRIES = "manual_entry_table";
    private static final String KEY_ROWID = "_id";
    private static final String KEY_INPUT_TYPE = "input_type";
    private static final String KEY_ACTIVITY_TYPE = "activity_type";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_AVG_PACE = "avg_page";
    private static final String KEY_AVG_SPEED = "avg_speed";
    private static final String KEY_CALORIES = "calorie";
    private static final String KEY_CLIMB = "climb";
    private static final String KEY_HEARTRATE = "heart_rate";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_PRIVACY = "privacy";
    private static final String KEY_GPS_DATA = "gps";
    private static final String DBNAME = "MyRunDB";
    private static final int DBVERSION = 3;
    private SQLiteDatabase db;


    private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "      // create the table
            + TABLE_NAME_ENTRIES
            + " ("
            + KEY_ROWID
            + " integer primary key autoincrement, "
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
    public void insertEntry(ExerciseEntry entry) {

        db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.d(TAG, "input type " + entry.getInputType());


        values.put(MySQLiteHelper.KEY_INPUT_TYPE, entry.getInputType());
        values.put(MySQLiteHelper.KEY_ACTIVITY_TYPE, entry.getActType());
        values.put(MySQLiteHelper.KEY_DATE_TIME, entry.getDateTime());
        values.put(MySQLiteHelper.KEY_DURATION, entry.getDuration());
        values.put(MySQLiteHelper.KEY_DISTANCE, entry.getDistance());
        values.put(MySQLiteHelper.KEY_AVG_PACE, entry.getAvgPace());
        values.put(MySQLiteHelper.KEY_AVG_SPEED, entry.getAvgSpeed());
        values.put(MySQLiteHelper.KEY_CALORIES, entry.getCalorie());
        values.put(MySQLiteHelper.KEY_CLIMB, entry.getClimb());
        values.put(MySQLiteHelper.KEY_HEARTRATE, entry.getHeartrate());
        values.put(MySQLiteHelper.KEY_COMMENT, entry.getComment());
        values.put(MySQLiteHelper.KEY_PRIVACY, entry.getPrivacy());
        values.put(MySQLiteHelper.KEY_GPS_DATA, entry.getGps());


        long newRowId = db.insert(TABLE_NAME_ENTRIES, null, values);
        if(newRowId == -1)
            Log.d(TAG, "Error in inserting!");

        else
            Log.d(TAG, "Successs in insert!");

        db.close();
        this.close();

    }


    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
        db = getWritableDatabase();
        db.delete(TABLE_NAME_ENTRIES, KEY_ROWID + " = " + rowIndex, null);
        Log.d(TAG, rowIndex + " have been deleted");

        db.close();
        this.close();
    }


    public void deleteAll() {
        db = getWritableDatabase();

        Log.d(TAG, "delete all = ");
        db.delete(TABLE_NAME_ENTRIES, null, null);
        db.close();
        this.close();
    }


//     Query a specific entry by its index.
//    public void fetchEntryByIndex(long rowId) {
//
//        db = getReadableDatabase();
////        String sortOrder = FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";
//        String selection = KEY_ROWID + " LIKE ?";
//        String[] selectionArgs = { String.valueOf(rowId) };
//
//        Cursor cursor = db.query(
//                TABLE_NAME_ENTRIES,   // The table to query
//                null,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                null               // The sort order
//        );
//
//    }

    // Query the entire table, return all rows
    public ArrayList<ExerciseEntry> fetchEntries() {

        ArrayList<ExerciseEntry> entries = new ArrayList<>();
        db = getReadableDatabase();
        String sortOrder = null;      //KEY_DATE_TIME + " ASC";

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
        while (!cursor.isAfterLast()) {     // move cursor to get each column

            long id = cursor.getLong(0);
            String input_type = cursor.getString(1);
            String act_name = cursor.getString(2);
            String date_time = cursor.getString(3);
            String duration = cursor.getString(4);
            String distance = cursor.getString(5);
            String avg_pace = cursor.getString(6);
            String avg_speed = cursor.getString(7);
            String cal =cursor.getString(8);
            String climb =cursor.getString(9);
            String heartrate = cursor.getString(10);
            String comment = cursor.getString(11);
            String privacy = cursor.getString(12);
            String gps = cursor.getString(13);

            Log.d(TAG, "get gps " + gps);

            ExerciseEntry entry = new ExerciseEntry(id,input_type, act_name, date_time, duration,
                    distance,avg_pace, avg_speed, cal, climb,
                   heartrate, comment, privacy, gps);

            entries.add(entry);
            cursor.moveToNext();
        }

        db.close();
        cursor.close();       //close the cursor
        return entries;
    }


}
