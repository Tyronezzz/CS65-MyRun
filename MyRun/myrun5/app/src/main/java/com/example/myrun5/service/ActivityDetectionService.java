/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-21
 */

package com.example.myrun5.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.Task;

import androidx.annotation.Nullable;

public class ActivityDetectionService  extends Service {

    private static final String TAG = ActivityDetectionService.class.getSimpleName();
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "onStartCommand()");
        mActivityRecognitionClient = new ActivityRecognitionClient(this);
        Intent mIntentService = new Intent(this, DetectedActivityIntentService.class);

        mPendingIntent = PendingIntent.getService(this,
                1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
        requestUpdatesHandler();

        return START_STICKY;
    }


    public void requestUpdatesHandler() {    // request updates

        if(mActivityRecognitionClient != null){
            Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(2000, mPendingIntent);

            // Adds a listener if the Task completes successfully.
            task.addOnSuccessListener(result -> Log.d(TAG, "Successfully requested activity updates"));
            // Adds a listener if the Task fails.
            task.addOnFailureListener(e -> Log.e(TAG, "Requesting activity updates failed to start"));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeUpdatesHandler();      // remove the activity requested update
    }


    public void removeUpdatesHandler() {       // remove updates
        if(mActivityRecognitionClient != null){
            Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                    mPendingIntent);
            // Adds a listener if the Task completes successfully.
            task.addOnSuccessListener(result -> Log.d(TAG, "Removed activity updates successfully!"));
            // Adds a listener if the Task fails.
            task.addOnFailureListener(e -> Log.e(TAG, "Failed to remove activity updates!"));
        }
    }


}
