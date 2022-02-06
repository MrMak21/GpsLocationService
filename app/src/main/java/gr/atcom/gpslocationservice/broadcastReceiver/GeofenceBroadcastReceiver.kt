package gr.atcom.gpslocationservice.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Timber.d(errorMessage)
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Timber.d("Enter in geofence")
            Toast.makeText(context, "Enter geofencing", Toast.LENGTH_SHORT).show()
        }

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            Timber.d("Location Found!!!!!!! ")
            Toast.makeText(context, "Dwellingggg", Toast.LENGTH_SHORT).show()
        }
    }
}