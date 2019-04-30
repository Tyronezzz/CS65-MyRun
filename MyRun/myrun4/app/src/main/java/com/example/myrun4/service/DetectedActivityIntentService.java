/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-21
 */

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
        List<DetectedActivity> detectedActivities = result.getProbableActivities();  // Get the list of the probable activities

        for (DetectedActivity activity : detectedActivities) {            // choose the most likely one  >= 70%
            Log.d(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
            if(activity.getConfidence() >= 70)
            {
                broadcastActivity(activity);
                return;
            }
        }

        broadcastUnknownActivity();
    }

    private void broadcastUnknownActivity() {                      // all acti below 70%, then send unknown
        Intent intent = new Intent("AR Activity");
        intent.putExtra("type", DetectedActivity.UNKNOWN);
        intent.putExtra("confidence", 100);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastActivity(DetectedActivity activity) {        // send the activity and confidence
        Log.d(TAG,TAG+ " broadcastActivity()");
        Intent intent = new Intent("AR Activity");
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
