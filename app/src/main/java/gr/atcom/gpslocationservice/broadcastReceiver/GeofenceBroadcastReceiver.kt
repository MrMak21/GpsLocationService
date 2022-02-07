package gr.atcom.gpslocationservice.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import gr.atcom.gpslocationservice.definitions.Definitions
import gr.atcom.gpslocationservice.listener.GeofenceListenerObject
import gr.atcom.gpslocationservice.ui.MapsActivity
import timber.log.Timber

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private lateinit var context: Context

    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Timber.d(errorMessage)
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        val geofenceId = geofencingEvent.triggeringGeofences[0].requestId

        when (geofenceId) {
            Definitions.GEOFENCE_BIG_RADIUS_ID -> { bigGeofenceTrigger(geofenceTransition) }
            Definitions.GEOFENCE_ID -> { smallGeofenceTrigger(geofenceTransition) }
        }

        val observableObject = GeofenceListenerObject(MapsActivity.instance)
        observableObject.geofenceTriggered(geofenceId, geofenceTransition)
    }

    private fun bigGeofenceTrigger(geofenceTransition: Int) {
        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Timber.d("Enter in BIG geofence")
                Toast.makeText(context, "Enter BIG geofencing", Toast.LENGTH_SHORT).show()
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Timber.d("EXIT from BIG geofence")
                Toast.makeText(context, "EXIT BIG geofencing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun smallGeofenceTrigger(geofenceTransition: Int) {
        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Timber.d("Enter in SMALL geofence")
                Toast.makeText(context, "Enter SMALL geofencing", Toast.LENGTH_SHORT).show()
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Timber.d("EXIT from SMALL geofence")
                Toast.makeText(context, "EXIT SMALL geofencing", Toast.LENGTH_SHORT).show()
            }
        }
    }
}