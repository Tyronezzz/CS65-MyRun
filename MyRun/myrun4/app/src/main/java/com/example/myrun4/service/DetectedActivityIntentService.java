package com.example.myrun4.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import androidx.annotation.Nullable;

public class DetectedActivityIntentService extends IntentService {


    private static final String TAG = DetectedActivityIntentService.class.getSimpleName();

    public DetectedActivityIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.

        List<DetectedActivity> detectedActivities = result.getProbableActivities();


        DetectedActivity activity_tmp = null;

        if(detectedActivities.size()>0)
        {
            activity_tmp = detectedActivities.get(0);
        }

        for (DetectedActivity activity : detectedActivities) {            // choose the most likely one
            Log.d(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());

            if(activity.getConfidence() >activity_tmp.getConfidence())
                activity_tmp = activity;
        }
        broadcastActivity(activity_tmp);

    }


    private void broadcastActivity(DetectedActivity activity) {
        Log.d(TAG,TAG+ " broadcastActivity()");
        Intent intent = new Intent("AR Activity");
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
