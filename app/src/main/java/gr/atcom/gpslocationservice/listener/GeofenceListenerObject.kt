package gr.atcom.gpslocationservice.listener

class GeofenceListenerObject(listener: GeofenceListener) {

    private val listener = listener

    fun geofenceTriggered(geofenceId: String, geofenceTransition: Int) {
        listener.onGeofenceTriggered(geofenceId, geofenceTransition)
    }
}