package apps.mly.gpstest;

import android.content.Intent;
import android.app.IntentService;
import android.util.Log;
import java.util.List;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;


public class GeofenceTransitionService extends IntentService {


    private String TAG = "GPSTEST";

    public GeofenceTransitionService(String name, String TAG) {
        super(name);
        this.TAG = TAG;
    }


    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //String errorMessage = GeofenceErrorMessages.getErrorString(this,
            //        geofencingEvent.getErrorCode());
            // Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            /*

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            */

            // Send notification and log the transition details.
            //sendNotification(geofenceTransitionDetails);
            //Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            //Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
            //        geofenceTransition));
        }
    }
}
