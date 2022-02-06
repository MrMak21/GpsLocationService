package gr.atcom.gpslocationservice

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import gr.atcom.gpslocationservice.databinding.ActivityMainBinding
import gr.atcom.gpslocationservice.geofence.GeofenceUtil
import gr.atcom.gpslocationservice.service.GpsIntentService
import gr.atcom.gpslocationservice.service.GpsReceiver
import gr.atcom.gpslocationservice.ui.MapsActivity
import timber.log.Timber
import timber.log.Timber.Forest.plant


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        plant(Timber.DebugTree())


        GpsIntentService.startService(this)

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)

        binding.btnStop.setOnClickListener {
            GpsIntentService.stopService(this)
        }
    }
}