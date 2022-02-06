package gr.atcom.gpslocationservice.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.widget.Toast

class GpsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val location: Location = intent.getParcelableExtra("data")!!
        println("Received broadcast with location inside location " + location.latitude + " lon: " + location.longitude)

        Toast.makeText(context, "Receive lat: " + location.latitude + " lon: " + location.longitude, Toast.LENGTH_SHORT).show()
    }
}