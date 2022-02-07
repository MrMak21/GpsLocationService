package gr.atcom.gpslocationservice.listener

interface GeofenceListener {

    fun onGeofenceTriggered(geofenceId: String, geofenceTransition: Int)
}