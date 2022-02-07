package gr.atcom.gpslocationservice.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gr.atcom.gpslocationservice.R
import gr.atcom.gpslocationservice.databinding.ActivityMapsBinding
import gr.atcom.gpslocationservice.service.GpsIntentService
import timber.log.Timber

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val homeLocation = LatLng(37.5133989, 22.3711960)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        GpsIntentService.startService(this)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in HomeLocation and move the camera
        mMap.addMarker(MarkerOptions().position(homeLocation).title("Here is home"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(homeLocation))
        mMap.setMinZoomPreference(19f)
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
}