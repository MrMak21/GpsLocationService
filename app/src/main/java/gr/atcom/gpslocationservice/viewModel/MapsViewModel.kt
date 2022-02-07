package gr.atcom.gpslocationservice.viewModel

import androidx.lifecycle.MutableLiveData

interface MapsViewModel {

    val notificationData: MutableLiveData<Pair<String, Int>>
    fun openDoorAsync(geofenceId: String, geofenceTransition: Int)
}