package gr.atcom.gpslocationservice.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import gr.atcom.gpslocationservice.broadcastReceiver.GeofenceBroadcastReceiver
import timber.log.Timber

class GeofenceUtil(context: Context) {

    companion object {
        const val GEOFENCE_ID: String = "geofenceId"
        const val GEOFENCE_BIG_RADIUS_ID: String = "GEOFENCE_BIG_RADIUS_ID"
        const val GEOFENCE_ID_DWELL: String = "GEOFENCE_ID_DWELL"
    }

    private var geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    private var geofenceList: List<Geofence> = arrayListOf(
        Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(
                37.5133989, 22.3711960, 10f
            )
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setNotificationResponsiveness(1000)
            .build()
        ,
        Geofence.Builder()
            .setRequestId(GEOFENCE_BIG_RADIUS_ID)
            .setCircularRegion(
                37.5133989, 22.3711960, 60f
            )
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setNotificationResponsiveness(1000)
            .build()
    )

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER )
            addGeofences(geofenceList)
        }.build()
    }

    init {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
                addOnSuccessListener {
                    Timber.d("Geofence added successful")
                }
                addOnFailureListener {
                    Timber.d("Geofence not added")
                    Timber.d(it.message)

                }
            }
        }

    }


}