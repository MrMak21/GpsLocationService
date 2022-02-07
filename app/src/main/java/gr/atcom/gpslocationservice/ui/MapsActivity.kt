package gr.atcom.gpslocationservice.ui

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gr.atcom.gpslocationservice.MainActivity
import gr.atcom.gpslocationservice.R
import gr.atcom.gpslocationservice.application.GpsLocationApplication
import gr.atcom.gpslocationservice.databinding.ActivityMapsBinding
import gr.atcom.gpslocationservice.definitions.Definitions
import gr.atcom.gpslocationservice.listener.GeofenceListener
import gr.atcom.gpslocationservice.service.GpsIntentService
import gr.atcom.gpslocationservice.viewModel.MapsViewModel
import gr.atcom.gpslocationservice.viewModel.MapsViewModelImpl
import timber.log.Timber

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GeofenceListener {

    companion object {
        lateinit var instance: MapsActivity
    }

    private lateinit var vm: MapsViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val REQUEST_PERMISSIONS_CODE: Int = 15
    private val homeLocation = LatLng(Definitions.HOME_LATITUDE, Definitions.HOME_LONGITUDE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        instance = this
        requestLocationPermissions()

        vm = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(GpsLocationApplication.get()))[MapsViewModelImpl::class.java]
        GpsIntentService.startService(this)

        initLayout()
        setUpObservers()
    }

    private fun initLayout() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setUpObservers() {
        val notificationObserver = Observer<Pair<String, Int>> { pair ->
            val geofenceId = pair.first
            val geofenceTransition = pair.second
            showNotification(geofenceId, geofenceTransition)
        }

        vm.notificationData.observe(this, notificationObserver)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_PERMISSIONS_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var permissionsGranted = true

        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                permissionsGranted = false
                break
            }
        }

        if (permissionsGranted) {
            initLayout()
        } else {
            //Show the dialog
        }
    }

    private fun showNotification(geofenceId: String, geofenceTransition: Int) {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(getString(R.string.second_notification_channel_id), "Gps Location channel")
        } else {
            getString(R.string.second_notification_channel_id)
        }

        val text = getNotificationText(geofenceId, geofenceTransition)

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("GPS Tracker")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification_action)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    private fun getNotificationText(geofenceId: String, geofenceTransition: Int): String {
        if (geofenceId == Definitions.GEOFENCE_ID) {
            return when (geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    "Enter in SMALL geofence"
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    "EXIT from SMALL geofence"
                }
                else -> ""
            }
        } else {
            return when (geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    "Enter in BIG geofence"
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                   "EXIT from BIG geofence"
                }
                else -> ""
            }
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in HomeLocation and move the camera
        mMap.addMarker(MarkerOptions().position(homeLocation).title("Here is home"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(homeLocation))
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 19.0f ))
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.setPadding(0, 200, 0, 0)
        }

        addCircleToMap()
    }

    private fun addCircleToMap() {
        val innerCircle = CircleOptions()
            .center(LatLng(37.5133989, 22.3711960))
            .radius(10.0)
            .strokeWidth(1.0f)
            .strokeColor(ContextCompat.getColor(this, R.color.map_color_fill))
            .fillColor(ContextCompat.getColor(this, R.color.map_color_fill))

        val outerCircle = CircleOptions()
            .center(LatLng(37.5133989, 22.3711960))
            .radius(60.0)
            .strokeWidth(1.0f)
            .strokeColor(ContextCompat.getColor(this, R.color.map_outer_circle_color_fill))
            .fillColor(ContextCompat.getColor(this, R.color.map_outer_circle_color_fill))

        mMap.addCircle(outerCircle) // Draw new circle.
        mMap.addCircle(innerCircle) // Draw new circle.
    }

    override fun onGeofenceTriggered(geofenceId: String, geofenceTransition: Int) {
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            vm.openDoorAsync(geofenceId, geofenceTransition)
        }
    }
}