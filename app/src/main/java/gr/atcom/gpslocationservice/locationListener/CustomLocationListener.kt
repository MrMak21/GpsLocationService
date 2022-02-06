package gr.atcom.gpslocationservice.locationListener

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.widget.Toast
import timber.log.Timber

class CustomLocationListener(): LocationListener {

    private lateinit var context: Context

    override fun onLocationChanged(location: Location) {
        Timber.d("Current location lat: " + location.latitude + " lon: " + location.longitude )
        println("Current location lat: " + location.latitude + " lon: " + location.longitude)
//        Toast.makeText(context, "Current location lat: " + location.latitude + " lon: " + location.longitude, Toast.LENGTH_LONG)
        sendBroad(location)
    }

    fun sendBroad(location: Location) {
        Intent().also { intent ->
            intent.action = "com.example.broadcast.MY_NOTIFICATION"
            intent.putExtra("data", location)
            context.sendBroadcast(intent)
        }
    }

    fun setContext(context: Context) {
        this.context = context
    }
}