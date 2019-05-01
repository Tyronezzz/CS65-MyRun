/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-21
 */


package com.example.myrun5.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.myrun5.R;
import com.example.myrun5.activity.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

public class trackingService extends Service {

    private static final String TAG = "tracking";
    private NotificationManager mNotificationManager;
    public static final String CHANNEL_ID = "notification channel";
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private double totalDis = 0;
    private ArrayList<LatLng> latlngArr;
    private double cur_speed;
    private double avg_speed;
    private double last_climbed;
    private double climbed;
    private double calories;
    private long startTime;
    private long lastTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Log.d(TAG, "Bind!!!");

        if(mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication());
        }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback,null);
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "start service");
        latlngArr = new ArrayList<>();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "S:onStartCommand(): Received start id " + startId + ": " + intent);


        String input_type = intent.getStringExtra("activity_input_name");
        if(input_type.equals("GPS"))
            showNotification();     // display notification

        else
            showHeadUpNotificatoin();

        initExerciseEntry();      // init the activity entry
        startLocationUpdate();      // get location from service

        return START_STICKY;       // Run until explicitly stopped.
    }

    private LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }


    private void initExerciseEntry()        // init the entry
    {
        cur_speed = 0;
        avg_speed = 0;
        climbed = 0;
        calories = 0;
        startTime = Calendar.getInstance().getTimeInMillis();
        lastTime = startTime;
        totalDis = 0;
    }



    private void startLocationUpdate()
    {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1500) ;     //1.5 seconds, in milliseconds


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if(mFusedLocationClient == null) {            // init the fusedclient
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication());
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
           if(location != null)
           {
               LatLng latlng = fromLocationToLatLng(location);
               latlngArr.add(latlng);
               last_climbed = location.getAltitude();

               Intent intent = new Intent();                 // broadcast the para for the first time
               intent.putExtra("latlng", latlng);
               intent.putExtra("loc", location);
               intent.putExtra("curSpeed", cur_speed);
               intent.putExtra("avgSpeed", avg_speed);
               intent.putExtra("climbed", climbed);
               intent.putExtra("calorie", calories);
               intent.putExtra("distance", totalDis);
               intent.putExtra("StartTime", startTime);
               intent.setAction("myrun.CUSTOM_BROADCAST");
               sendBroadcast(intent);
           }
       });
    }


    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {      // callback of update latlng
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                Log.d(TAG, "moving...");

                LatLng latlng = fromLocationToLatLng(location);

                double lastmove = getSingleDis(latlng, latlngArr.get(latlngArr.size()-1));
                totalDis += lastmove;
                latlngArr.add(latlng);
                cur_speed = lastmove / (Calendar.getInstance().getTimeInMillis() - lastTime) * 1000000.0;
                avg_speed = totalDis / (Calendar.getInstance().getTimeInMillis() - startTime)* 1000000.0;
                lastTime = Calendar.getInstance().getTimeInMillis();

                climbed += abs(location.getAltitude() - last_climbed);
                last_climbed = location.getAltitude();
                calories = totalDis * 0.2 * 1000;

                Intent intent = new Intent();           // broadcast the paras to maps for update
                intent.putExtra("latlngUpdate", latlng);
                intent.putExtra("curSpeed", cur_speed);
                intent.putExtra("avgSpeed", avg_speed);
                intent.putExtra("climbed", climbed);       // send m
                intent.putExtra("calorie", calories);
                intent.putExtra("distance", totalDis * 1000.0);    // send m

                intent.setAction("myrun.UPDATE_BROADCAST");
                sendBroadcast(intent);
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.O)
    private void showNotification() {         // display the notification

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_DEFAULT);
        Intent resultIntent = new Intent(this, MapsActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Tap me to go back")
                        .setSmallIcon(R.drawable.common_full_open_on_phone)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(notificationChannel);

        Notification notification = mBuilder.build();

        startForeground(1, notification);
//        mNotificationManager.notify(0, notification);
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void showHeadUpNotificatoin(){
        NotificationChannel notificationChannel;
        String CHANNEL_ID2 = "headup noti";

        notificationChannel = new NotificationChannel(CHANNEL_ID2, "channel name2", NotificationManager.IMPORTANCE_HIGH);

        Intent resultIntent = new Intent(this, MapsActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID2)
                .setContentTitle("Activity Recognition")
                .setContentText("Activity tracker started.")
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(notificationChannel);

        Notification notification = mBuilder.build();

        startForeground(1, notification);
    }


    public double getSingleDis(LatLng l1, LatLng l2)      // calculate distance between l1 and l2, return kms
    {
        double lat1 = l1.latitude;
        double lng1 = l1.longitude;
        double lat2 = l2.latitude;
        double lng2 = l2.longitude;
        double p =  0.0175;    //Pi/180

        double angle = 0.5 - cos((lat2 - lat1) * p)/2 + cos(lat1 * p) * cos(lat2 * p) * (1 - cos((lng2 - lng1) * p)) / 2;
        return 12742 * asin(sqrt(angle));  // 2*R*asin...
    }



    @Override
    public boolean onUnbind(Intent intent) {

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);       // remove the callback listener
        }

        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "S:onDestroy():Service Stopped");
        super.onDestroy();

        mNotificationManager.cancelAll(); // Cancel the persistent notification.
    }
}
