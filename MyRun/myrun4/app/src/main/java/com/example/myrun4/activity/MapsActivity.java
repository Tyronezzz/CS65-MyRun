package com.example.myrun4.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myrun4.MySQLiteHelper;
import com.example.myrun4.R;
import com.example.myrun4.fragment.main_history;
import com.example.myrun4.model.ExerciseEntry;
import com.example.myrun4.service.trackingService;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ServiceConnection {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    public Marker whereAmI;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Marker preMarker;

    boolean mIsBound;
    private ServiceConnection mConnection = this;  // as we implement ServiceConnection
    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());
    private Messenger mServiceMessenger = null;
//    LocationManager locationManager;
    private LatLng prelat;
    private PolylineOptions rectOptions;
    private Polyline polyline;
    private MyBroadcastReceiver receiver;
    private MyBroadcastReceiver receiver2;


    private double cur_speed;
    private double avg_speed;
    private double climbed;
    private double calories;
    private long startTime;
    private double duration;
    private double totalDis;
    private String activity_type;


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
        activity_type = intent.getStringExtra("act_type");        // get the parent activity name


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // start the service here
        try {

            Intent tracking = new Intent(getApplication(), trackingService.class);
//            tracking.putExtra("locationManager", (Parcelable) locationManager);
//            tracking.putExtra("context", (Parcelable) getApplication());

            startService(tracking);
            bindService(tracking, mConnection, Context.BIND_AUTO_CREATE);

        }
        catch (Exception e)
        {
            Log.d(TAG, "error binding");
        }

        mIsBound = true;




        receiver = new MyBroadcastReceiver();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manual_menu, menu);     // Inflate the menu
        MenuItem item = menu.findItem(R.id.action_save);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    // operations on the toolbar
        switch (item.getItemId()) {

            case android.R.id.home:
//                doUnbindService();
                finish();
                return true;


            case R.id.action_save:
                // save, insert and finish


                duration = (Calendar.getInstance().getTimeInMillis() - startTime) / 6000; // get min

                AsynWriteSQL writesqlhelper = new AsynWriteSQL();
                writesqlhelper.execute();

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (!checkPermission())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
//        else
//            upDateMap();


    }


    private void upDateMap() {

        LatLng latlng = prelat;
        MarkerOptions a = new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED));
        preMarker = mMap.addMarker(a);     // start point

        rectOptions = new PolylineOptions().add(preMarker.getPosition());


        whereAmI = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN)));          //set position and icon for the marker

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Zoom in
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17)); //17: the desired zoom level, in the range of 2.0 to 21.0
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission granted. Let's show the map");
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void updateWithNewLocation(LatLng curlatlng) {

        if (curlatlng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curlatlng, 17));

            if (whereAmI != null)
            {
                whereAmI.remove();
            }

            whereAmI = mMap.addMarker(new MarkerOptions().position(curlatlng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN)).title("Here I Am."));

            rectOptions.add(whereAmI.getPosition());
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
        mServiceMessenger = new Messenger(service);

        try {
            Message msg = Message.obtain(null, trackingService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            Log.d(TAG, "C: TX MSG_REGISTER_CLIENT");
            // We use service Messenger to send the msg to the Server
            mServiceMessenger.send(msg);

        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceMessenger = null;
    }


    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "C:IncomingHandler:handleMessage " + msg.replyTo);
            switch (msg.what) {
//                case MyService.MSG_SET_INT_VALUE:
//                    Log.d(TAG, "C: RX MSG_SET_INT_VALUE");
//                    // msg.arg1 here as only arg1 was used to store data in the server class.
//                    textIntValue.setText("Int Message: " + msg.arg1);
//                    break;
//                case MyService.MSG_SET_STRING_VALUE:
//                    // getString(key) -> str1 is the key of the key value pair we used in the server side.
//                    String str1 = msg.getData().getString("str1");
//                    Log.d(TAG, "C:RX MSG_SET_STRING_VALUE");
//                    textStrValue.setText("Str Message: " + str1);
//                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }



    private void doUnbindService() {
        Log.d(TAG, "C:doUnBindService()");
        if (mIsBound) {
            // If we have received the service, and hence registered with it,
            // then now is the time to unregister.
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, trackingService.MSG_UNREGISTER_CLIENT);//  obtain (Handler h, int what) - 'what' is the tag of the message, which will be used in line 72 in MyService.java. Returns a new Message from the global message pool. More efficient than creating and allocating new instances.
                    //Log.d(TAG, "C: TX MSG_UNREGISTER_CLIENT");
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);// need to use the server messenger to send the message to the server
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has
                    // crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "get receiver " + intent.getAction());

            if(intent.getAction().equals("myrun.CUSTOM_BROADCAST"))
            {
                prelat = intent.getParcelableExtra("latlng");
                Location l = intent.getParcelableExtra("loc");
//                getLocation(l);
                upDateMap();
                getEntries(intent);

            }

            else if(intent.getAction().equals("myrun.UPDATE_BROADCAST"))
            {
                LatLng curlat = intent.getParcelableExtra("latlngUpdate");
                updateWithNewLocation(curlat);
                getEntries(intent);
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


    private void getLocation(Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder gc = new Geocoder(this, Locale.getDefault());

        if (!Geocoder.isPresent())
            Log.d(TAG, "No geocoder available");


        else {
            try {
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append("\n");

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d(TAG, "HERE " + sb.toString());
                }

            } catch (IOException e) {
                Log.d("WHEREAMI", "IO Exception", e);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
            stopService(new Intent(MapsActivity.this, trackingService.class));
            unregisterReceiver(receiver);
            unregisterReceiver(receiver2);

        } catch (Throwable t) {
            Log.e(TAG, "Failed to unbind from the service", t);
        }
    }





    @SuppressLint("StaticFieldLeak")
    class AsynWriteSQL extends AsyncTask<Void, Void, Void> {

        MySQLiteHelper mysqlhelper;

        AsynWriteSQL()
        {
            mysqlhelper = new MySQLiteHelper(getApplication());

        }

        @Override
        protected Void doInBackground(Void... voids) {

//            (long id, String input_type, String activity_type, String date_time, String duration,
//                    String distance, String avg_page, String avg_speed, String calorie, String climb,
//                    String heart_rate, String comment, String privacy, String gps){
//
//            }

            //
            Calendar tmpcld = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");     // set the current time
            String strTime = mdformat.format(tmpcld.getTime());
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            //


            ExerciseEntry entry = new ExerciseEntry(0, "GPS:", activity_type,date + " " + strTime, String.format("%.2f", duration) + " mins",
                    String.valueOf(totalDis), null, String.valueOf(avg_speed), String.valueOf(calories), String.valueOf(climbed), null, null, null, null);
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
//            main_history.adapterChoice = 1;
            mLoader.initLoader(1, null, main_history.lc).forceLoad();


        }

    }
}
