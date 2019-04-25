package com.example.myrun4.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.myrun4.R;
import com.example.myrun4.activity.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class trackingService extends Service {

    private static final String TAG = "tracking";
    private NotificationManager mNotificationManager;
    private static boolean isRunning = false;
    public static final String CHANNEL_ID = "notification channel";
    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;

    private List<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track
    LocationManager locationManager;


    private class IncomingMessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "S:handleMessage: " + msg.what + msg.replyTo);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.add(msg.replyTo) ");
                    mClients.add(msg.replyTo);//replyTo is the Messanger, that carrys the Message over.
                    break;
//                case MSG_UNREGISTER_CLIENT:
//                    Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.remove(msg.replyTo) ");
//                    mClients.remove(msg.replyTo);// each client has a dedicated Messanger to communicae with ther server.
//                    break;
//                case MSG_SET_INT_VALUE:
//                    incrementBy = msg.arg1;
//                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "start service");
//        mTimer.scheduleAtFixedRate(new MyTask(), 0, 1000L);



        isRunning = true;


    }




    @Override
    public void onDestroy() {
        Log.d(TAG, "S:onDestroy():Service Stopped");
        super.onDestroy();

//        mNotificationManager.cancelAll(); // Cancel the persistent notification.
        isRunning = false;
    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "S:onStartCommand(): Received start id " + startId + ": " + intent);


        startLocationUpdate();


        //        showNotification();


        //set up the inital location


        return START_STICKY; // Run until explicitly stopped.
    }

    private LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }


    private void initExerciseEntry(Location l)
    {
        float speed = l.getSpeed();
        long initTime = l.getTime();


    }

    private void startLocationUpdate()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        Location l = locationManager.getLastKnownLocation(provider);
        LatLng latlng = fromLocationToLatLng(l);


        // send l, activity can convert l to latlng
        Intent intent = new Intent(); //(this, MyBroadcastReceiver.class);
        intent.putExtra("latlng", latlng);
        intent.setAction("myrun.CUSTOM_BROADCAST");
        sendBroadcast(intent);

        // update once every 2 second, min distance 0 therefore not considered
        locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);

    }

    private void startActivityUpdate()
    {


    }






    @TargetApi(Build.VERSION_CODES.O)
    private void showNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_DEFAULT);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MapsActivity.class), 0);// this is the main app page it will show by clicking the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(("Tap me to go back"))
//                .setContentText(getResources().getString(R.string.service_started))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(notificationChannel);

        mNotificationManager.notify(0, notification);

    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged");
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
        }

        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged");
        }
    };


    private void updateWithNewLocation(Location location) {


        if (location != null) {
            // Update the map location.
            LatLng latlng = fromLocationToLatLng(location);

            //send the latlng
            Intent intent = new Intent(); //(this, MyBroadcastReceiver.class);
            intent.putExtra("latlngUpdate", latlng);
            intent.setAction("myrun.UPDATE_BROADCAST");
            sendBroadcast(intent);




        }
    }
}
