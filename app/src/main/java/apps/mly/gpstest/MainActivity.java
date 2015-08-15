package apps.mly.gpstest;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;


import android.app.PendingIntent;
import android.content.Intent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;


import android.view.View;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.List;




//
// demonstrates 3 uses of GPS location.
// 1) fixes position when you first start app (getLastLocation)
// 2) polls for location change every 10 seconds
// 3) sets a geofence monitor and reacts when we trip it
//

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private PendingIntent mGeofencePendingIntent;
    private LocationRequest mLocationRequest;
    private String TAG = "GPSTEST.MainActivity";

    private List<Geofence> mGeoFences;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        Log.i(TAG, "just built a connection to the google api");



    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeoFences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout ll = (LinearLayout) findViewById(R.id.geoCircle);
        DrawView drawing = new DrawView(this);
        ll.addView(drawing);


        buildGoogleApiClient();


        /*

        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);

        */

        createLocationRequest();




        Log.i(TAG, "end of oncreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //
    // gps location change just occurred...
    //
    @Override
    public void onLocationChanged(Location location) {

        TextView latChange = (TextView) findViewById(R.id.latitudeChange);
        TextView lonChange = (TextView) findViewById(R.id.longitudeChange);

        latChange.setText(String.valueOf(location.getLatitude()));
        lonChange.setText(String.valueOf(location.getLongitude()));

    }


    //
    // got connected to google play services
    //
    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            TextView lat = (TextView) findViewById(R.id.latitude);
            TextView lon = (TextView) findViewById(R.id.longitude);

            lat.setText(String.valueOf(mLastLocation.getLatitude()));
            lon.setText(String.valueOf(mLastLocation.getLongitude()));

        }


        startLocationUpdates();


    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Conn suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "conn failed");

        //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();

    }


    private class DrawView extends View {

        public DrawView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            Paint myPaint = new Paint();
            myPaint.setColor(Color.BLACK);

            canvas.drawCircle(
                    200,
                    200,
                    200,
                    myPaint
            );

        }
    }


}
