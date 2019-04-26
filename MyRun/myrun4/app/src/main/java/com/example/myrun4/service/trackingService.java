package com.example.myrun4.service;

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

import com.example.myrun4.R;
import com.example.myrun4.activity.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

public class trackingService extends Service {

    private static final String TAG = "tracking";
    private NotificationManager mNotificationManager;
    private static boolean isRunning = false;
    public static final String CHANNEL_ID = "notification channel";
//    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;

//    private List<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track
//    LocationManager locationManager;
//    private String provider;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private double totalDis = 0;
    private ArrayList<LatLng> latlngArr;
    private double cur_speed;
    private double avg_speed;
    private double climbed;
    private double calories;
    private long startTime;
    private long lastTime;


//    private class IncomingMessageHandler extends Handler {
//
//        @Override
//        public void handleMessage(Message msg) {
//            Log.d(TAG, "S:handleMessage: " + msg.what + msg.replyTo);
////            switch (msg.what) {
////                case MSG_REGISTER_CLIENT:
////                    Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.add(msg.replyTo) ");
////                    mClients.add(msg.replyTo);//replyTo is the Messanger, that carrys the Message over.
////                    break;
////                case MSG_UNREGISTER_CLIENT:
////                    Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.remove(msg.replyTo) ");
////                    mClients.remove(msg.replyTo);// each client has a dedicated Messanger to communicae with ther server.
////                    break;
//////                case MSG_SET_INT_VALUE:
//////                    incrementBy = msg.arg1;
//////                    break;
////                default:
//                    super.handleMessage(msg);
////            }
//        }
//    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Log.d(TAG, "Bind!!!");
//        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

        if(mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication());
        }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback,null);
        return null;

//        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "start service");
//        mTimer.scheduleAtFixedRate(new MyTask(), 0, 1000L);

        isRunning = true;
        latlngArr = new ArrayList<>();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "S:onStartCommand(): Received start id " + startId + ": " + intent);


        //set up the inital location
        showNotification();
        initExerciseEntry();
        startLocationUpdate();

        return START_STICKY; // Run until explicitly stopped.
    }

    private LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }


    private void initExerciseEntry()
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
                .setInterval(100) ;     // 1 seconds, in milliseconds


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if(mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication());
        }

       mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
           @Override
           public void onSuccess(Location location)
            {
                if(location != null)
                {
                    Log.d(TAG, "not null init loc");
                    LatLng latlng = fromLocationToLatLng(location);

                    // send other fields like speed?
                    latlngArr.add(latlng);


                    Intent intent = new Intent();
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
            }
       });
    }

    private void startActivityUpdate()
    {


    }



    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                Log.d(TAG, "moving...");

                LatLng latlng = fromLocationToLatLng(location);




                // send other fields like speed?
                double lastmove = getSingleDis(latlng, latlngArr.get(latlngArr.size()-1));
                totalDis += lastmove;
                latlngArr.add(latlng);
//                Log.d(TAG, "total dis " + totalDis);       // km
                cur_speed = lastmove / (Calendar.getInstance().getTimeInMillis() - lastTime) * 1000000.0;
                avg_speed = totalDis / (Calendar.getInstance().getTimeInMillis() - startTime)* 1000000.0;
                climbed = 2;   //????
                calories = 1;

                Intent intent = new Intent();
                intent.putExtra("latlngUpdate", latlng);
                intent.putExtra("curSpeed", cur_speed);
                intent.putExtra("avgSpeed", avg_speed);
                intent.putExtra("climbed", climbed);
                intent.putExtra("calorie", calories);
                intent.putExtra("distance", totalDis * 1000.0);

                intent.setAction("myrun.UPDATE_BROADCAST");
                sendBroadcast(intent);
            }
        }


    };

    @TargetApi(Build.VERSION_CODES.O)
    private void showNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_DEFAULT);

        Intent resultIntent = new Intent(this, MapsActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Tap me to go back")
                        .setSmallIcon(R.drawable.common_full_open_on_phone)
                        .setContentIntent(pendingIntent);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(notificationChannel);

        Notification notification = mBuilder.build();
        startForeground(1, notification);
//        mNotificationManager.notify(0, notification);





    }


    public double getSingleDis(LatLng l1, LatLng l2)
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
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

//        locationManager.removeUpdates(locationListener);
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "S:onDestroy():Service Stopped");
        super.onDestroy();

        mNotificationManager.cancelAll(); // Cancel the persistent notification.
        isRunning = false;
    }

}
