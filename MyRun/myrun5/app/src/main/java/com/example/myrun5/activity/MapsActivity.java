/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-21
 */


package com.example.myrun5.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myrun5.MySQLiteHelper;
import com.example.myrun5.R;
import com.example.myrun5.fragment.main_history;
import com.example.myrun5.model.ExerciseEntry;
import com.example.myrun5.service.ActivityDetectionService;
import com.example.myrun5.service.trackingService;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.myrun5.fragment.main_history.*;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ServiceConnection {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    public Marker curMarker;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Marker preMarker;

    boolean mIsBound;
    private ServiceConnection mConnection = this;  // as we implement ServiceConnection
    private LatLng prelat;
    private PolylineOptions rectOptions;
    public Polyline polyline;
    private MyBroadcastReceiver receiver;
    private MyBroadcastReceiver receiver2;
    private SharedPreferences sharedPreferences;

    private double cur_speed;
    private double avg_speed;
    private double climbed;
    private double calories;
    private long startTime;
    private double duration;
    private double totalDis;
    private String activity_type;
    private TextView act_type;
    private TextView mspeed;
    private TextView mavgSpeed;
    private TextView mclimbed;
    private TextView mcalorie;
    private TextView mdistance;
    private String gpsStr;
    private String parentName;
    private ExerciseEntry oneentry;
    private long entryID;
    private String input_type;
    private HashMap<String, Integer> act_table;
    private double factorm2mile = 0.00062;

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        myToolbar.setTitle("Map");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar


        Intent intent = getIntent();
        input_type = intent.getStringExtra("TYPE");
        activity_type = intent.getStringExtra("act_type");        // get the activity name: running...
        parentName = intent.getStringExtra("PARENTNAME");        // get the parent activity name


        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        gpsStr = "";

        act_type = findViewById(R.id.text_activity_type);
        mspeed = findViewById(R.id.text_cur_speed);
        mavgSpeed = findViewById(R.id.text_avg_speed);
        mclimbed = findViewById(R.id.text_climb);
        mcalorie = findViewById(R.id.text_calorie);
        mdistance = findViewById(R.id.text_distance);


        // Obtain the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        act_table = new HashMap<>();

        if(parentName.equals("MAINHISTORY"))         // history
        {
            oneentry = (ExerciseEntry) getIntent().getSerializableExtra("EXENTRY");
            entryID = oneentry.getId();
        }

        else          // automatic and gps
        {
            // start the service here
            try {
                Intent tracking = new Intent(getApplication(), trackingService.class);
                tracking.putExtra("activity_input_name", input_type);
                startService(tracking);
                bindService(tracking, mConnection, Context.BIND_AUTO_CREATE);
            }
            catch (Exception e)
            {
                Log.d(TAG, "error binding");
            }

            mIsBound = true;

            receiver = new MyBroadcastReceiver();     // two broadcast receiver
            receiver2 = new MyBroadcastReceiver();
            registerReceiver(
                    receiver,
                    new IntentFilter("myrun.CUSTOM_BROADCAST")
            );

            registerReceiver(
                    receiver2,
                    new IntentFilter("myrun.UPDATE_BROADCAST")
            );
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setUpMap(ExerciseEntry entry) {    // rendering the map for history
        act_type.setText("Activity: " +entry.getActType());
        mcalorie.setText("Calorie: " + entry.getCalorie());

        int km_mile_idx = sharedPreferences.getInt("key_unit_pre", 0);

        if(km_mile_idx == 1 &&  entry.getAvgSpeed().contains("m/s"))     //m to mile
        {
            String[] substr = entry.getAvgSpeed().split("\\s+");
            String[] substr2 = entry.getClimb().split("\\s+");
            String[] substr3 = entry.getDistance().split("\\s+");

            double tmpavgSpeed = Double.parseDouble(substr[0])*factorm2mile<0.01? 0.0 : Double.parseDouble(substr[0])*factorm2mile;
            double tmpclimb = Double.parseDouble(substr2[0])*factorm2mile<0.01? 0.0 : Double.parseDouble(substr2[0])*factorm2mile;
            double tmpdis = Double.parseDouble(substr3[0])*factorm2mile<0.01? 0.0 : Double.parseDouble(substr3[0])*factorm2mile;

            mavgSpeed.setText("Avg Speed: " + String.format("%.2f", tmpavgSpeed) +" miles/s");
            mclimbed.setText("Climbed: " + String.format("%.2f", tmpclimb) + " miles");
            mdistance.setText("Distance: " + String.format("%.2f", tmpdis) + " miles");
        }

        else if(km_mile_idx == 0 && entry.getAvgSpeed().contains("mile"))      // mile to m
        {

            String[] substr = entry.getAvgSpeed().split("\\s+");
            String[] substr2 = entry.getClimb().split("\\s+");
            String[] substr3 = entry.getDistance().split("\\s+");

            double tmpavgSpeed = Double.parseDouble(substr[0])*1609.34<0.01? 0.0 : Double.parseDouble(substr[0])*1609.34;
            double tmpclimb = Double.parseDouble(substr2[0])*1609.34<0.01? 0.0 : Double.parseDouble(substr2[0])*1609.34;
            double tmpdis = Double.parseDouble(substr3[0])*1609.34<0.01? 0.0 : Double.parseDouble(substr3[0])*1609.34;

            mavgSpeed.setText("Avg Speed: " + String.format("%.2f", tmpavgSpeed) + " m/s");
            mclimbed.setText("Climbed: " + String.format("%.2f", tmpclimb) + " m");
            mdistance.setText("Distance: " + String.format("%.2f", tmpdis) + " m");
        }

        else
        {
            mavgSpeed.setText("Avg Speed: " + entry.getAvgSpeed());
            mclimbed.setText("Climbed: " + entry.getClimb());
            mdistance.setText("Distance: " + entry.getDistance());
        }


        if(km_mile_idx == 0)
            mspeed.setText("Speed: 0.00 m/s");             // m
        else
            mspeed.setText("Speed: 0.00 mile/s");             // mile

        String gpsData = entry.getGps();
        String[] arrOfStr = gpsData.split(";");
        LatLng newloc = str2latlng(arrOfStr[0]);

        MarkerOptions a = new MarkerOptions().position(newloc).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED));
        preMarker = mMap.addMarker(a);     // start point
        rectOptions = new PolylineOptions().add(preMarker.getPosition());

        for(int i=1;i<arrOfStr.length;i++)      // draw lines here
        {

            LatLng loc = str2latlng(arrOfStr[i]);
            rectOptions.add(loc);
            rectOptions.color(Color.BLACK);
            polyline = mMap.addPolyline(rectOptions);
        }

        LatLng lastloc = str2latlng(arrOfStr[arrOfStr.length-1]);
        curMarker = mMap.addMarker(new MarkerOptions().position(lastloc).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN)));          //set position and icon for the marker

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newloc, 17)); //17: the desired zoom level, in the range of 2.0 to 21.0

    }

    private LatLng str2latlng(String s) {           // convert String to latlng
        String[] latlong =  s.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        return new LatLng(latitude, longitude);
    }



    @Override
    protected void onStart() {
        super.onStart();

        if(input_type != null &&input_type.equals("Automatic"))
        {
            Log.d(TAG, "onStart()");
            LocalBroadcastManager.getInstance(this).registerReceiver(mActivityBroadcastReceiver,
                    new IntentFilter("AR Activity"));

            startService(new Intent(this, ActivityDetectionService.class));      // register the RX and start up the ActivityDetectionService service
        }

    }

    BroadcastReceiver mActivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             Log.d(TAG, "onReceive()");
            if (intent.getAction().equals("AR Activity")) {
                int type = intent.getIntExtra("type", -1);
                int confidence = intent.getIntExtra("confidence", 0);
                handleUserActivity(type, confidence);
            }
        }
    };


    @SuppressLint("SetTextI18n")
    private void handleUserActivity(int type, int confidence) {
        String label = "Unknown";
        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = "In_Vehicle";
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = "On_Bicycle";
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = "On_Foot";
                break;
            }
            case DetectedActivity.RUNNING: {
                label = "Running";
                break;
            }
            case DetectedActivity.STILL: {
                label = "Still";
                break;
            }
            case DetectedActivity.TILTING: {
                label = "Tilting";
                break;
            }
            case DetectedActivity.WALKING: {
                label = "Walking";
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = "Unknown";
                break;
            }
        }

        act_type.setText("Activity: " + label);

        if(act_table.get(label) == null)        // update hashtable
            act_table.put(label, 1);
        else
            act_table.put(label, act_table.get(label)+1);

        Log.d(TAG, "broadcast:onReceive(): Activity is " + label + " and confidence level is: " + confidence);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manual_menu, menu);     // Inflate the menu

        if(parentName.equals("MAINHISTORY"))
        {
            MenuItem item = menu.findItem(R.id.action_save);
            item.setTitle("DELETE");
        }

        return true;
    }


    private void findLikelyAct()
    {

        int maxValueInMap=(Collections.max(act_table.values()));  // This will return max value in the Hashmap
        for (Map.Entry<String, Integer> entry : act_table.entrySet()) {  // Itrate hashmap
            if (entry.getValue() == maxValueInMap) {
                activity_type = entry.getKey();
                return;
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    // operations on the toolbar
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_save:
                if(parentName.equals("MAINHISTORY"))       // delete in the history
                {
                    Intent intent = new Intent();
                    intent.putExtra("DELETEIDX", entryID);
                    setResult(RESULT_OK, intent);        // important!!!

                }

                else      // save the map para
                {

                    duration = (Calendar.getInstance().getTimeInMillis() - startTime) / 60000.0; // get min
                    if(duration < 0.01)
                        duration = 0.0;

                    AsynWriteSQL writesqlhelper = new AsynWriteSQL();
                    writesqlhelper.execute();
                }
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (!checkPermission())     //  checkPermission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

        if(parentName.equals("MAINHISTORY"))
            setUpMap(oneentry);
    }


    private void upDateMap() {

        LatLng latlng = prelat;
        MarkerOptions a = new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED));
        preMarker = mMap.addMarker(a);     // start point

        rectOptions = new PolylineOptions().add(preMarker.getPosition());


        curMarker = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN)));          //set position and icon for the marker

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Zoom in
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17)); //17: the desired zoom level, in the range of 2.0 to 21.0
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission granted. Let's show the map");
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void updateWithNewLocation(LatLng curlatlng) {

        if (curlatlng != null) {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curlatlng, 17));

            if (curMarker != null)
            {
                curMarker.remove();
            }

            curMarker = mMap.addMarker(new MarkerOptions().position(curlatlng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN)));

            rectOptions.add(curMarker.getPosition());
            rectOptions.color(Color.BLACK);
            polyline = mMap.addPolyline(rectOptions);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

//        mServiceMessenger = new Messenger(service);

//        try {
//            Message msg = Message.obtain(null, trackingService.MSG_REGISTER_CLIENT);
//            msg.replyTo = mMessenger;
//            Log.d(TAG, "C: TX MSG_REGISTER_CLIENT");
//            // We use service Messenger to send the msg to the Server
//            mServiceMessenger.send(msg);
//
//        } catch (RemoteException e) {
//            Log.e(TAG, "RemoteException", e);
//        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
//        mServiceMessenger = null;
    }



    private void doUnbindService() {
        Log.d(TAG, "C:doUnBindService()");
        if (mIsBound) {

//            if (mServiceMessenger != null) {
//                try {
//                    Message msg = Message.obtain(null, trackingService.MSG_UNREGISTER_CLIENT);
//
//                    msg.replyTo = mMessenger;
//                    mServiceMessenger.send(msg);// need to use the server messenger to send the message to the server
//                } catch (RemoteException e) {
//                    // There is nothing special we need to do if the service has
//                    // crashed.
//                }
//            }

            unbindService(mConnection);      // Detach our existing connection.
            mIsBound = false;
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "get receiver " + intent.getAction());

            if(intent.getAction().equals("myrun.CUSTOM_BROADCAST"))
            {
                prelat = intent.getParcelableExtra("latlng");
                startTime = intent.getLongExtra("StartTime", 0);
                Log.d(TAG, "start time " + startTime);
                String newlatlng = String.valueOf(prelat.latitude) + "," + String.valueOf(prelat.longitude) + ";";
                gpsStr += newlatlng;

                upDateMap();
                getEntries(intent);
            }

            else if(intent.getAction().equals("myrun.UPDATE_BROADCAST"))
            {
                LatLng curlat = intent.getParcelableExtra("latlngUpdate");
                String newlatlng = String.valueOf(curlat.latitude) + "," + String.valueOf(curlat.longitude) + ";";
                gpsStr += newlatlng;

                updateWithNewLocation(curlat);
                getEntries(intent);
            }

            int km_mile_idx = sharedPreferences.getInt("key_unit_pre", 0);

            if(km_mile_idx == 1)   // to mile
            {
                cur_speed *= factorm2mile;
                avg_speed *= factorm2mile;
                climbed *= factorm2mile;
                totalDis *= factorm2mile;
            }

            if(totalDis < 0.01)
                totalDis = 0.0;

            if(climbed < 0.01)
                climbed = 0.0;

            if(cur_speed < 0.01)
                cur_speed = 0.00;

            if(avg_speed < 0.01)
                avg_speed = 0.00;

            if(calories < 0.01)
                calories = 0.0;

            if(input_type != null && !input_type.equals("Automatic"))
            {
                act_type.setText("Activity: " + activity_type);
            }

            mcalorie.setText("Calorie: " + String.format("%.2f", calories) + " cal");

            if(km_mile_idx == 1)
            {
                mspeed.setText("Speed: " + String.format("%.2f", cur_speed) + " mile/s");             // mile
                mavgSpeed.setText("Avg Speed: " + String.format("%.2f", avg_speed) + " mile/s");
                mclimbed.setText("Climbed: " + String.format("%.2f", climbed) + " mile");
                mdistance.setText("Distance: " + String.format("%.2f", totalDis) + " mile");   // m
            }

            else
            {
                mspeed.setText("Speed: " + String.format("%.2f", cur_speed) + " m/s");             // m
                mavgSpeed.setText("Avg Speed: " + String.format("%.2f", avg_speed) + " m/s");
                mclimbed.setText("Climbed: " + String.format("%.2f", climbed) + " m");
                mdistance.setText("Distance: " + String.format("%.2f", totalDis) + " m");   // m
            }
        }
    }


    private void getEntries(Intent intent) {
        cur_speed = intent.getDoubleExtra("curSpeed", 0);
        avg_speed = intent.getDoubleExtra("avgSpeed", 0);
        climbed = intent.getDoubleExtra("climbed", 0);
        calories = intent.getDoubleExtra("calorie", 0);
        totalDis = intent.getDoubleExtra("distance", 0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(parentName.equals("MAINHISTORY"))
        {
            Log.d(TAG, "leave");
        }

        else
        {
            try {
                doUnbindService();
                stopService(new Intent(MapsActivity.this, trackingService.class));
                unregisterReceiver(receiver);
                unregisterReceiver(receiver2);

            } catch (Throwable t) {
                Log.e(TAG, "Failed to unbind from the service", t);
            }
        }

        if(input_type != null && input_type.equals("Automatic"))
        {
            stopService(new Intent(this, ActivityDetectionService.class));
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mActivityBroadcastReceiver);
        }
    }



    @SuppressLint("StaticFieldLeak")
    class AsynWriteSQL extends AsyncTask<Void, Void, Void> {

        MySQLiteHelper mysqlhelper;

        AsynWriteSQL()
        {
            mysqlhelper = new MySQLiteHelper(getApplication());

        }

        @SuppressLint("DefaultLocale")
        @Override
        protected Void doInBackground(Void... voids) {

            Calendar tmpcld = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");     // set the current time
            String strTime = mdformat.format(tmpcld.getTime());
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


            String tmp_input = "GPS";
            if(input_type != null &&input_type.equals("Automatic"))
            {
                findLikelyAct();
                tmp_input = "Automatic";
            }


            ExerciseEntry entry;
            int km_mile_idx = sharedPreferences.getInt("key_unit_pre", 0);
            if(km_mile_idx == 0)     // km
            {
                entry = new ExerciseEntry(0, tmp_input, activity_type,date + " " + strTime, String.format("%.2f", duration) + " mins",
                        String.format("%.2f", totalDis) + " m", null, String.format("%.2f", avg_speed) + " m/s",
                        String.format("%.2f", calories) + " cal", String.format("%.2f", climbed) + " m", null, null, null, gpsStr,
                        "false", "false","false");

            }

            else     // mile
            {
                entry = new ExerciseEntry(0, "GPS", activity_type,date + " " + strTime, String.format("%.2f", duration) + " mins",
                        String.format("%.2f", totalDis) + " mile", null, String.format("%.2f", avg_speed) + " mile/s",
                        String.format("%.2f", calories) + " cal", String.format("%.2f", climbed) + " mile", null, null, null, gpsStr,
                        "false", "false","false");
            }


            mysqlhelper.insertEntry(entry);                    // insert an entry
            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            Log.d(TAG, "after insert");

            LoaderManager mLoader = main_history.mLoader;        // reinit a loader to update the ui
            if (mLoader != null)
            {
                mLoader.destroyLoader(1);
            }
            mLoader.initLoader(1, null, lc).forceLoad();
        }
    }

}
